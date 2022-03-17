package com.gsys.services

import com.gsys.persistence.SensorReadingRepository
import com.gsys.persistence.model.SensorReading
import com.gsys.util.MessageResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Title

import java.time.LocalDate

import static org.hamcrest.Matchers.containsString
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@Title("QueryController Specification")
@Narrative("Tests related to query services")
@SpringBootTest
@AutoConfigureMockMvc
class QueryControllerTest extends Specification {

    @Autowired
    SensorReadingRepository sensorReadingRepository

    @Autowired
    MockMvc mockMvc

    def setup() {
        sensorReadingRepository.save(new SensorReading("x1",1,1D, LocalDate.of(2020,1,12)))
        sensorReadingRepository.save(new SensorReading("x1",1,4D, LocalDate.of(2020,1,13)))
        sensorReadingRepository.save(new SensorReading("x1",2,5D, LocalDate.of(2020,1,15)))
        sensorReadingRepository.save(new SensorReading("x2",2,7D, LocalDate.of(2020,1,15)))
        sensorReadingRepository.save(new SensorReading("x3",1,3D, LocalDate.of(2020,1,19)))
    }

    def "Get query results without filter dates"() {
        expect: "Correct results when filter exclude 1 sensor (Last value not average)"
        this.mockMvc.perform(post("/query/get_data").accept(MediaType.APPLICATION_JSON)
                .content("{\"sensorIds\":[\"x1\",\"x2\"],\n" +
                        "\"metricIds\":[1 , 2] }")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(MessageResponse.RESULTS_OK.getMessage())))
                .andExpect(content().string(containsString("{\"sensorId\":\"x1\",\"metricId\":1,\"value\":4.0}")))
                .andExpect(content().string(containsString("{\"sensorId\":\"x2\",\"metricId\":2,\"value\":7.0}")))
                .andExpect(content().string(containsString("{\"sensorId\":\"x1\",\"metricId\":2,\"value\":5.0}")))
                .andExpect(jsonPath("\$.results[?(@.sensorId=='x3' && @.metricId=='1')]").doesNotExist())

        and: "Correct results when filter exclude 1 sensor and request 1 metric (Last value not average)"
        this.mockMvc.perform(post("/query/get_data").accept(MediaType.APPLICATION_JSON)
                .content("{\"sensorIds\":[\"x1\",\"x2\"],\n" +
                        "\"metricIds\":[1] }")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(MessageResponse.RESULTS_OK.getMessage())))
                .andExpect(content().string(containsString("{\"sensorId\":\"x1\",\"metricId\":1,\"value\":4.0}")))
                .andExpect(jsonPath("\$.results[?(@.sensorId=='x3' && @.metricId=='1')]").doesNotExist())
                .andExpect(jsonPath("\$.results[?(@.sensorId=='x1' && @.metricId=='2')]").doesNotExist())

        and:  "Correct results when filter does not have sensor ids and request 1 metric (Last value not average)"
        this.mockMvc.perform(post("/query/get_data").accept(MediaType.APPLICATION_JSON)
                .content("{\"metricIds\":[1] }")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(MessageResponse.RESULTS_OK.getMessage())))
                .andExpect(content().string(containsString("{\"sensorId\":\"x1\",\"metricId\":1,\"value\":4.0}")))
                .andExpect(content().string(containsString("{\"sensorId\":\"x3\",\"metricId\":1,\"value\":3.0}")))
                .andExpect(jsonPath("\$.results[?(@.sensorId=='x1' && @.metricId=='2')]").doesNotExist())
    }


    def "Get query results with filter dates"() {
        expect: "Correct results when filter exclude 1 sensor"
        this.mockMvc.perform(post("/query/get_data").accept(MediaType.APPLICATION_JSON)
                .content("{\"sensorIds\":[\"x1\",\"x2\"],\n" +
                        "\"metricIds\":[1,2],\n" +
                        "\"startDate\": \"2020-01-10T17:02:15\",\n" +
                        "\"endDate\": \"2020-01-18T17:02:15\"}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(MessageResponse.RESULTS_OK.getMessage())))
                .andExpect(content().string(containsString("{\"sensorId\":\"x1\",\"metricId\":1,\"value\":2.5}")))
                .andExpect(content().string(containsString("{\"sensorId\":\"x2\",\"metricId\":2,\"value\":7.0}")))
                .andExpect(content().string(containsString("{\"sensorId\":\"x1\",\"metricId\":2,\"value\":5.0}")))
                .andExpect( jsonPath("\$.results[?(@.sensorId=='x3' && @.metricId=='1')]" ).doesNotExist())

        and: "Correct results when filter does not have sensor id (get all)"
        this.mockMvc.perform(post("/query/get_data").accept(MediaType.APPLICATION_JSON)
                .content("{\"metricIds\":[1,2],\n" +
                        "\"startDate\": \"2020-01-10T17:02:15\",\n" +
                        "\"endDate\": \"2020-01-20T17:02:15\"}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(MessageResponse.RESULTS_OK.getMessage())))
                .andExpect(content().string(containsString("{\"sensorId\":\"x1\",\"metricId\":1,\"value\":2.5}")))
                .andExpect(content().string(containsString("{\"sensorId\":\"x2\",\"metricId\":2,\"value\":7.0}")))
                .andExpect(content().string(containsString("{\"sensorId\":\"x1\",\"metricId\":2,\"value\":5.0}")))
                .andExpect(content().string( containsString("{\"sensorId\":\"x3\",\"metricId\":1,\"value\":3.0}")))

        and: "Correct results when filter does not have sensor id (get all) but exclude 1 due to date filter"
        this.mockMvc.perform(post("/query/get_data").accept(MediaType.APPLICATION_JSON)
                .content("{\"metricIds\":[1,2],\n" +
                        "\"startDate\": \"2020-01-10T17:02:15\",\n" +
                        "\"endDate\": \"2020-01-18T17:02:15\"}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(MessageResponse.RESULTS_OK.getMessage())))
                .andExpect(content().string(containsString("{\"sensorId\":\"x1\",\"metricId\":1,\"value\":2.5}")))
                .andExpect(content().string(containsString("{\"sensorId\":\"x2\",\"metricId\":2,\"value\":7.0}")))
                .andExpect(content().string(containsString("{\"sensorId\":\"x1\",\"metricId\":2,\"value\":5.0}")))
                .andExpect( jsonPath("\$.results[?(@.sensorId=='x3' && @.metricId=='1')]" ).doesNotExist())
    }

    def "Request is not valid "() {
        expect: "Request Validation Error with message 'At least 1 metric ID must be provided'"
        this.mockMvc.perform(post("/query/get_data").accept(MediaType.APPLICATION_JSON)
                .content("{\"sensorIds\":[\"x1\",\"x2\"]," +
                        "\"startDate\": \"2022-03-15T17:02:15\"," +
                        "\"endDate\": \"2022-03-18T17:02:15\"}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString(MessageResponse.METRIC_REQUIRED.getMessage())))

        and: "Request Validation Error with message 'At least 1 metric ID must be provided' when empty list"
        this.mockMvc.perform(post("/query/get_data").accept(MediaType.APPLICATION_JSON)
                .content("{\"sensorIds\":[\"x1\",\"x2\"],\n" +
                        "\"metricIds\":[],\n" +
                        "\"startDate\": \"2022-03-15T17:02:15\",\n" +
                        "\"endDate\": \"2022-03-18T17:02:15\"}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString(MessageResponse.METRIC_REQUIRED.getMessage())))

        and: "Request Validation Error with message 'The date range must not be higher than 30 Days'"
        this.mockMvc.perform(post("/query/get_data").accept(MediaType.APPLICATION_JSON)
                .content("{\"sensorIds\":[\"x1\",\"x2\"],\n" +
                        "\"metricIds\":[1,2,3],\n" +
                        "\"startDate\": \"2022-03-15T17:02:15\",\n" +
                        "\"endDate\": \"2022-05-18T17:02:15\"}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString(MessageResponse.DATE_OUT_RANGE.getMessage())))

        and: "Request Validation Error with message 'The start date must be before the end date'"
        this.mockMvc.perform(post("/query/get_data").accept(MediaType.APPLICATION_JSON)
                .content("{\"sensorIds\":[\"x1\",\"x2\"],\n" +
                        "\"metricIds\":[1,2],\n" +
                        "\"startDate\": \"2023-03-15T17:02:15\",\n" +
                        "\"endDate\": \"2022-05-18T17:02:15\"}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString(MessageResponse.DATE_RANGE_INVALID.getMessage())))
    }

}
