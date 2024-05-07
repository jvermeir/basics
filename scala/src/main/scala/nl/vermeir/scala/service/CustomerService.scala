package nl.vermeir.scala.service

import nl.vermeir.scala.App.executionContext
import nl.vermeir.scala.repository.CustomerRepository

import java.util.UUID
import scala.concurrent.Future

object CustomerService {

  final case class Customer(id: Option[UUID], name: String, email: String)

  def findCustomerById(id: UUID): Future[Option[Customer]] = Future {
    CustomerRepository.find(id)
  }

  def getCustomers: Future[List[Customer]] = Future {
    CustomerRepository.findAll()
  }

  def addCustomer(customer: Customer): Future[Customer] = {
    val newCustomer = CustomerRepository.save(customer)
    Future {
      newCustomer
    }
  }
}