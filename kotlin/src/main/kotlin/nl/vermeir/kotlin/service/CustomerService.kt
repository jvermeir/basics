package nl.vermeir.kotlin.service

import nl.vermeir.kotlin.controller.Customer
import nl.vermeir.kotlin.repository.CustomerRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomerService(val db: CustomerRepository) {

    fun findCustomers(): List<Customer> = db.findAll().toList()

    fun findCustomerById(id: String): Customer =  db.findById(id).orElseThrow()

    fun save(customer: Customer) = db.save(customer)

    fun deleteCustomer(id: String) {
        db.deleteById(id)
    }

    fun updateCustomer(customer: Customer): Customer {
        return db.save(customer)
    }

    fun <T : Any> Optional<out T>.toList(): List<T> =
        if (isPresent) listOf(get()) else emptyList()
}