package nl.vermeir.kotlin.controller

import com.google.gson.Gson
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
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
                .post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"John\",\"email\":\"test@test.com\"}")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn().response.contentAsString

        return gson.fromJson(response, Customer::class.java)
    }

    @Test
    fun `all customers are returned by customers endpoint`() {
        saveSampleCustomer()
        mvc.perform(
            MockMvcRequestBuilders
                .get("/customers")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value("John"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].email").value("test@test.com"))
    }

    @Test
    fun `a customer is returned by customers slash id endpoint`() {
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

    // TODO: this test should fail but doesn't when run against h2
    fun `an error is returned if customer name or email are not unique`() {
        val sampleCustomer = saveSampleCustomer()
        val duplicateCustomer = sampleCustomer.copy(id="test")
        mvc.perform(
            MockMvcRequestBuilders
                .post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(duplicateCustomer))
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
    }
}

