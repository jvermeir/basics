package nl.vermeir.scala.service

import akka.Done
import nl.vermeir.scala.App.executionContext
import nl.vermeir.scala.repository.CustomerRepository
import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

import scala.concurrent.Future

object CustomerService {
  var customers = scala.collection.mutable.Map[Long, Customer]()

  final case class Customer(id: Long ,name: String, email: String)

  implicit val customerFormat: RootJsonFormat[Customer] = jsonFormat3(Customer.apply)

  def findCustomerById(id: Long): Future[Option[Customer]] = Future {
//    customers.get(id)
    CustomerRepository.find(id)
  }

  def getCustomers(): Future[List[Customer]] = Future {
//    customers.values.toList
    CustomerRepository.findAll()
  }

  def addCustomer(customer: Customer): Future[Done] = {
//    customers.addOne(customer.id, customer)
    CustomerRepository.save(customer)
    Future {
      Done
    }
  }
}