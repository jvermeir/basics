package nl.vermeir.kotlin.repository

import nl.vermeir.kotlin.controller.Customer
import org.springframework.data.repository.CrudRepository

interface CustomerRepository : CrudRepository<Customer, String>