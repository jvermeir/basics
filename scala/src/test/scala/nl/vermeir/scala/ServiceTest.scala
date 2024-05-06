package nl.vermeir.scala

import nl.vermeir.scala.Fixture.{drop, recreate}
import nl.vermeir.scala.service.CustomerService
import nl.vermeir.scala.service.CustomerService.Customer
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite

import java.util.UUID
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class ServiceTest extends AnyFunSuite with BeforeAndAfter {
  scalikejdbc.config.DBs.setupAll()

  before(recreate())
  after(drop())

  test("a list of customers can be retrieved") {
    val customerId = UUID.randomUUID().toString
    val theCustomer = Customer(Option(UUID.fromString(customerId)), "n1", "e1")
    Await.result(CustomerService.addCustomer(theCustomer), Duration(5, "seconds"))

    val allCustomers = CustomerService.getCustomers
    val customers = Await.result(allCustomers, Duration(5, "seconds"))
    assert(customers === List(theCustomer))
  }

  test("a customer can be created and read") {
    val customerId = UUID.randomUUID()
    val theCustomer = Customer(Option(customerId), "n1", "e1")
    Await.result(CustomerService.addCustomer(theCustomer), Duration(5, "seconds"))

    val allCustomers = CustomerService.findCustomerById(theCustomer.id.get)
    val customers = Await.result(allCustomers, Duration(5, "seconds"))
    assert(customers === Some(theCustomer))
  }
}
