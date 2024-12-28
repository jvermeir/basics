package nl.vermeir.kotlin.controller

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import nl.vermeir.kotlin.service.CustomerService
import org.springframework.web.bind.annotation.*

@Table
@jakarta.persistence.Entity
data class Customer(@Id
                    @GeneratedValue(strategy = GenerationType.UUID) var id: String?,
                    val name: String,
                    val email: String?)

@RestController
@CrossOrigin
@RequestMapping("/customers")
class CustomerController (val service: CustomerService) {

    @GetMapping()
    fun all() = service.findCustomers()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): Customer =
        service.findCustomerById(id)

    @PostMapping("/")
    fun post(@RequestBody customer: Customer) =
        service.save(customer)

    @PutMapping
    @ResponseBody
    fun updateCustomer(@RequestBody customer: Customer): Customer {
        return service.updateCustomer(customer)
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    fun deleteCustomer(@PathVariable id: String) {
        service.deleteCustomer(id)
    }

}
