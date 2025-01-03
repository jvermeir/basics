package nl.vermeir.java.controller;

import com.google.gson.Gson;
import nl.vermeir.java.model.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    Gson gson = new Gson();

    private Customer saveSampleCustomer() throws Exception {
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders
                        .post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John\",\"email\":\"test@test.com\"}"))
                .andDo(print())
                .andExpect(status().isOk());
        return gson.fromJson(resultActions.andReturn().getResponse().getContentAsString(), Customer.class);
    }

    @Test
    public void getAllCustomerRecordsAPI() throws Exception {
        saveSampleCustomer();

        mvc.perform(MockMvcRequestBuilders
                        .get("/customers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("John"))
                .andExpect(jsonPath("$.[0].email").value("test@test.com"));
    }

    @Test
    public void getACustomerById() throws Exception {
        Customer customer = saveSampleCustomer();

        mvc.perform(MockMvcRequestBuilders
                        .get("/customers/" + customer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("test@test.com"));
    }

}