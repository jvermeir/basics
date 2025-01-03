package nl.vermeir.kotlin.service

import nl.vermeir.kotlin.controller.Customer
import nl.vermeir.kotlin.repository.CustomerAlreadyExistsException
import nl.vermeir.kotlin.repository.CustomerRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomerService(val db: CustomerRepository) {

    fun findCustomers(): List<Customer> = db.findAll().toList()

    fun findCustomerById(id: String): Customer = db.findById(id).orElseThrow()

    fun saveCustomer(customer: Customer): Customer {
        try {
            return db.save(customer)
        } catch (e: DataIntegrityViolationException) {
            throw CustomerAlreadyExistsException("Customer with the same name or email already exists.")
        } catch (e: Exception) {
            throw e
        }
    }

    fun deleteCustomer(id: String) {
        db.deleteById(id)
    }

    fun <T : Any> Optional<out T>.toList(): List<T> = if (isPresent) listOf(get()) else emptyList()

}