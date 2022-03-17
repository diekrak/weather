package com.gsys.services

import com.gsys.persistence.MetricRepository
import com.gsys.persistence.SensorReadingRepository
import com.gsys.persistence.SensorRepository
import com.gsys.persistence.model.Metric
import com.gsys.persistence.model.Sensor
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@Title("SensorReadingsController Specification")
@Narrative("Tests related to sensor services")
@SpringBootTest
@AutoConfigureMockMvc
class SensorReadingsControllerTest extends Specification {

    @Autowired
    SensorReadingRepository sensorReadingRepository

    @Autowired
    SensorRepository sensorRepository

    @Autowired
    MetricRepository metricRepository

    @Autowired
    MockMvc mockMvc

    def setup() {
       LocalDate date= LocalDate.now()
       metricRepository.save(new Metric(1,"Wind Speed", date))
       metricRepository.save(new Metric(2,"Temperature", date))
       sensorRepository.save(new Sensor("a1","Bta","COl",true, date, date))
       sensorRepository.save(new Sensor("a2","berl","Ger",false, date, date))
    }

    def "Add successfully a new Reading"() {
        expect: "Status is 200 and the response is 'Reading Added Successfully'"
        this.mockMvc.perform(post("/sensor_readings/add").accept(MediaType.APPLICATION_JSON)
                .content("{ \"sensorId\":\"a1\", \"metricId\":\"1\", \"value\":11, \"date\": \"2024-12-24T19:59:15\"}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(MessageResponse.READING_ADDED.getMessage())))
    }

    def "Try to add new Reading where sensor or metric are not valid"() {

        expect: "Response is 'The Sensor is inactive'"
        this.mockMvc.perform(post("/sensor_readings/add").accept(MediaType.APPLICATION_JSON)
                .content("{ \"sensorId\":\"a2\", \"metricId\":\"1\", \"value\":11, \"date\": \"2024-12-24T19:59:15\"}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString(MessageResponse.SENSOR_INACTIVE.getMessage())))
        and: "Response is 'The Sensor is inactive'"
        this.mockMvc.perform(post("/sensor_readings/add").accept(MediaType.APPLICATION_JSON)
                .content("{ \"sensorId\":\"AA2\", \"metricId\":\"1\", \"value\":11, \"date\": \"2024-12-24T19:59:15\"}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString(MessageResponse.SENSOR_NOT_EXIST.getMessage())))
        and: "Response is 'The Sensor is inactive'"
        this.mockMvc.perform(post("/sensor_readings/add").accept(MediaType.APPLICATION_JSON)
                .content("{ \"sensorId\":\"a1\", \"metricId\":\"12\", \"value\":11, \"date\": \"2024-12-24T19:59:15\"}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString(MessageResponse.METRIC_NOT_EXIST.getMessage())))

    }

}
