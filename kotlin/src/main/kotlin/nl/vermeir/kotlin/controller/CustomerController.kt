package nl.vermeir.kotlin.controller

import jakarta.persistence.*
import nl.vermeir.kotlin.service.CustomerService
import org.springframework.web.bind.annotation.*

@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["name"])])
@jakarta.persistence.Entity
data class Customer(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) var id: String?,
    @Column(nullable = false, unique = true)
    val name: String,
    val email: String?
)

@RestController
@CrossOrigin
@RequestMapping("/customers")
class CustomerController(val service: CustomerService) {

    val logger = org.slf4j.LoggerFactory.getLogger(CustomerController::class.java)

    @GetMapping
    fun all(): List<Customer> {
        logger.info("all customers")
        return service.findCustomers()
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): Customer {
        logger.info("find customer by id: $id")
        return service.findCustomerById(id)
    }

    @PostMapping
    fun post(@RequestBody customer: Customer): Customer {
        logger.info("post customer $customer")
        return service.saveCustomer(customer)
    }

    @PutMapping
    @ResponseBody
    fun updateCustomer(@RequestBody customer: Customer): Customer {
        logger.info("put customer ${customer.id}")
        return service.saveCustomer(customer)
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    fun deleteCustomer(@PathVariable id: String) {
        logger.info("delete customer $id")
        service.deleteCustomer(id)
    }
}
