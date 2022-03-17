package com.gsys.services

import com.gsys.persistence.SensorReadingRepository
import com.gsys.persistence.model.SensorReading
import com.gsys.services.jsonMessage.QueryResponse
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

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
        sensorReadingRepository.save(new SensorReading("a1",1,1D, LocalDate.of(2020,1,12)))
        sensorReadingRepository.save(new SensorReading("a1",1,3D, LocalDate.of(2020,1,13)))
        sensorReadingRepository.save(new SensorReading("a1",2,5D, LocalDate.of(2020,1,15)))
        sensorReadingRepository.save(new SensorReading("a2",2,7D, LocalDate.of(2020,1,15)))
        sensorReadingRepository.save(new SensorReading("a3",1,3D, LocalDate.of(2020,1,19)))
    }


    def "Get query results"() {
        expect: "Results when filter are valid"
        this.mockMvc.perform(post("/query/get_data").accept(MediaType.APPLICATION_JSON)
                .content("{\"sensorIds\":[\"a1\",\"a2\"],\n" +
                        "\"metricIds\":[1,2],\n" +
                        "\"startDate\": \"2020-01-10T17:02:15\",\n" +
                        "\"endDate\": \"2020-01-18T17:02:15\"}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(MessageResponse.RESULTS_OK.getMessage())))
               // .andExpect(jsonPath("\$.results[?(@.sensorId=='a1' && @.metricId=='1')].value").value("[2.0]"))
    }

    def "Request is not valid "() {
        expect: "Request Validation Error with message 'At least 1 metric ID must be provided'"
        this.mockMvc.perform(post("/query/get_data").accept(MediaType.APPLICATION_JSON)
                .content("{\"sensorIds\":[\"a1\",\"a2\"]," +
                        "\"startDate\": \"2022-03-15T17:02:15\"," +
                        "\"endDate\": \"2022-03-18T17:02:15\"}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString(MessageResponse.METRIC_REQUIRED.getMessage())))

        and: "Request Validation Error with message 'At least 1 metric ID must be provided' when empty list"
        this.mockMvc.perform(post("/query/get_data").accept(MediaType.APPLICATION_JSON)
                .content("{\"sensorIds\":[\"a1\",\"a2\"],\n" +
                        "\"metricIds\":[],\n" +
                        "\"startDate\": \"2022-03-15T17:02:15\",\n" +
                        "\"endDate\": \"2022-03-18T17:02:15\"}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString(MessageResponse.METRIC_REQUIRED.getMessage())))

        and: "Request Validation Error with message 'The date range must not be higher than 30 Days'"
        this.mockMvc.perform(post("/query/get_data").accept(MediaType.APPLICATION_JSON)
                .content("{\"sensorIds\":[\"a1\",\"a2\"],\n" +
                        "\"metricIds\":[1,2,3],\n" +
                        "\"startDate\": \"2022-03-15T17:02:15\",\n" +
                        "\"endDate\": \"2022-05-18T17:02:15\"}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString(MessageResponse.DATE_OUT_RANGE.getMessage())))

        and: "Request Validation Error with message 'The start date must be before the end date'"
        this.mockMvc.perform(post("/query/get_data").accept(MediaType.APPLICATION_JSON)
                .content("{\"sensorIds\":[\"a1\",\"a2\"],\n" +
                        "\"metricIds\":[1,2],\n" +
                        "\"startDate\": \"2023-03-15T17:02:15\",\n" +
                        "\"endDate\": \"2022-05-18T17:02:15\"}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString(MessageResponse.DATE_RANGE_INVALID.getMessage())))
    }

}
