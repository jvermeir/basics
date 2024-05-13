package nl.vermeir.scala.grpc

import akka.actor.ActorSystem
import akka.grpc.GrpcClientSettings
import nl.vermeir.scala.Fixture.{drop, recreate}
import nl.vermeir.scala.service.CustomerService.{Customer, addCustomer}
import org.scalatest.BeforeAndAfter
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

class CustomerGrpcTest extends AnyFlatSpec with Matchers with ScalaFutures with BeforeAndAfter {
  scalikejdbc.config.DBs.setupAll()

  before(recreate())
  after(drop())

  val host = "127.0.0.1"
  val port = 8090

  implicit protected val system: ActorSystem = ActorSystem("api-test")

  val clientSettings: GrpcClientSettings = GrpcClientSettings.connectToServiceAt(host, port).withTls(false)

  val client = new CustomerServiceImpl()

  "Customer Service" should "return a list of customers when calling find all" in {
    val newCustomer = Customer(id = Option.empty, name = "name", email = "email")
    val maybeCustomer = addCustomer(newCustomer)
    val customer = Await.result(maybeCustomer, 1 second)

    val response = client.listCustomers(ListCustomerRequest())
    val customers = Await.result(response, 5 second)
    customers.customers.size should be(1)
    val theCustomer = customers.customers.head
    theCustomer.id should be(customer.id.get.toString)
    theCustomer.name should be(customer.name)
    theCustomer.email should be(customer.email)
  }

  "Customer Service" should "return a customers by id" in {
    val newCustomer = Customer(id = Option.empty, name = "name", email = "email")
    val maybeCustomer = addCustomer(newCustomer)
    val customer = Await.result(maybeCustomer, 1 second)

    val response = client.getCustomerById(GetCustomerByIdRequest(customer.id.get.toString))
    val newNewCustomer = Await.result(response, 5 second)
    newNewCustomer.id should be(customer.id.get.toString)
    newNewCustomer.name should be(customer.name)
    newNewCustomer.email should be(customer.email)
  }
}
