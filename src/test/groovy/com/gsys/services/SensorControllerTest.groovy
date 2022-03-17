package com.gsys.services

import com.gsys.persistence.SensorRepository
import com.gsys.persistence.model.Sensor
import com.gsys.util.MessageResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.util.NestedServletException
import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Title

import java.time.LocalDate

import static org.hamcrest.Matchers.containsString
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@Title("SensorController Specification")
@Narrative("Tests related to sensor services")
@SpringBootTest
@AutoConfigureMockMvc
class SensorControllerTest extends Specification {

    @Autowired
    private SensorRepository sensorRepository

    @Autowired
    private MockMvc mockMvc

    def "Subscribe successfully a new sensor"() {
        expect: "Status is 200 and the response is 'The Sensor was added successfully'"
        this.mockMvc.perform(post("/sensor/register").accept(MediaType.APPLICATION_JSON)
                .content("{ \"id\": \"s1\",\"country\": \"England\"}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(MessageResponse.SENSOR_ADDED.getMessage())))
    }

    def "Try to subscribe a sensor twice"() {
        given: "A request to subscribe"
        this.mockMvc.perform(post("/sensor/register").accept(MediaType.APPLICATION_JSON)
                .content("{ \"id\": \"s11\",\"country\": \"England2\"}")
                .contentType(MediaType.APPLICATION_JSON))
        expect: "Status is 200 and the response is 'Sensor is already registered' when try to register same sensor"
        this.mockMvc.perform(post("/sensor/register").accept(MediaType.APPLICATION_JSON)
                .content("{ \"id\": \"s11\",\"country\": \"England2\"}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString(MessageResponse.SENSOR_ALREADY_REGISTERED.getMessage())))
    }

    def "Try to subscribe unsuccessfully a new sensor"() {
        when: "A request is sent"
        this.mockMvc.perform(post("/sensor/register").accept(MediaType.APPLICATION_JSON)
                .content("{ \"country\": \"England\"}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().is5xxServerError())
        then: "An error occurs due to no sensor id"
        final NestedServletException exception = thrown()
        exception.message.contains("Request processing failed; nested exception is org.springframework.dao.InvalidDataAccessApiUsageException: The given id must not be null")
    }

    def "Deactivate a sensor"() {
        given: "A request to subscribe"
        this.mockMvc.perform(post("/sensor/register").accept(MediaType.APPLICATION_JSON)
                .content("{ \"id\": \"b1\",\"country\": \"England2\"}")
                .contentType(MediaType.APPLICATION_JSON))

        expect: "Status is 200 and the response is 'The Sensor is inactive' when try to deactivate a sensor"
        this.mockMvc.perform(put("/sensor/deactivate/b1")
        ).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(MessageResponse.SENSOR_INACTIVE.getMessage())))

        and: "response is 'The Sensor is inactive' when sensor is not registered"
        this.mockMvc.perform(put("/sensor/deactivate/z1")
        ).andDo(print()).andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString(MessageResponse.SENSOR_NOT_EXIST.getMessage())))
    }

    def "Activate a sensor"() {
        given: "Inactive Sensor"
        sensorRepository.save(new Sensor("demo","Bta","COl",false, LocalDate.now(), LocalDate.now()))

        expect: "Status is 200 and the response is 'The Sensor is active' when try to activate a sensor"
        this.mockMvc.perform(put("/sensor/activate/demo")
        ).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(MessageResponse.SENSOR_ACTIVE.getMessage())))

        and: "response is 'The Sensor is inactive' when sensor is not registered"
        this.mockMvc.perform(put("/sensor/activate/demoFA")
        ).andDo(print()).andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString(MessageResponse.SENSOR_NOT_EXIST.getMessage())))
    }

}
