package nl.vermeir.kotlin.service

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import nl.vermeir.kotlin.controller.Customer
import nl.vermeir.kotlin.repository.CustomerRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@AutoConfigureMockMvc
@SpringBootTest
class CustomerServiceTest() {

    @Autowired
    lateinit var customerService: CustomerService
    @MockkBean
    lateinit var customerRepository: CustomerRepository

    @Test
    fun givenCustomerRecordObject_whenSaveCustomerRecord_thenReturnCustomerRecordObject() {
        // given
        val customer = Customer(UUID.randomUUID().toString(),"John","x@y.com")
        every { customerRepository.save(customer) } returns customer

        // when
        val savedCustomer: Customer = customerService.save(customer)

        // then
        assertThat(savedCustomer).isEqualTo(customer)
    }

    @Test
    fun givenCustomerRecordObject_whenListCustomerRecords_thenReturnListOfCustomerRecordObject() {
        // given
        val customer = Customer(UUID.randomUUID().toString(),"John","x@y.com")
        every { customerRepository.findAll() } returns listOf(customer)

        // when
        val allCustomers = customerService.findCustomers()

        // then
        assertThat(allCustomers).contains(customer)
        assertThat(allCustomers.size).isEqualTo(1)
    }
}