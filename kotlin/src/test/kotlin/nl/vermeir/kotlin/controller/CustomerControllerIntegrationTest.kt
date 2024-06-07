package nl.vermeir.kotlin.controller

import com.google.gson.Gson
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql("classpath:/schema.sql")
class CustomerControllerIntegrationTest {
    @Autowired
    lateinit var mvc: MockMvc

    val gson: Gson = Gson()

    @Throws(Exception::class)
    private fun saveSampleCustomer(): Customer {
        val response = mvc.perform(
            MockMvcRequestBuilders
                .post("/customers/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"John\",\"email\":\"test@test.com\"}")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn().response.contentAsString

        return gson.fromJson(response, Customer::class.java)
    }

    @Test
    fun `all customers are returned by customer slash all endpoint`() {
        saveSampleCustomer()
        mvc.perform(
            MockMvcRequestBuilders
                .get("/customers/")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value("John"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].email").value("test@test.com"))
    }

    @Test
    fun `a customer is returned by customer slash id endpoint`() {
        val (id) = saveSampleCustomer()
        mvc.perform(
            MockMvcRequestBuilders
                .get("/customers/$id")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test@test.com"))
    }
}

