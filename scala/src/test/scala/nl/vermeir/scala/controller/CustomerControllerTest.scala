package nl.vermeir.scala.controller

import akka.http.scaladsl.model.{HttpEntity, HttpMethods, HttpRequest, MediaTypes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.util.ByteString
import nl.vermeir.scala.controller.CustomerController.route
import nl.vermeir.scala.service.CustomerService.Customer
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import spray.json._
import DefaultJsonProtocol._
import nl.vermeir.scala.Fixture.{drop, recreate}
import org.scalatest.BeforeAndAfter

import java.util.UUID

class CustomerControllerTest extends AnyWordSpec with Matchers with ScalatestRouteTest with BeforeAndAfter {

  scalikejdbc.config.DBs.setupAll()

  before(recreate())
  after(drop())

  implicit object UuidJsonFormat extends RootJsonFormat[UUID] {
    def write(uuid: UUID): JsString = JsString(uuid.toString)

    def read(value: JsValue): UUID = value match {
      case JsString(uuid) => UUID.fromString(uuid)
      case _ => deserializationError("Expected hexadecimal UUID string")
    }
  }

  implicit val customerFormat: RootJsonFormat[Customer] = jsonFormat3(Customer.apply)

  private val testCustomer = Customer(id = Option.empty, name = "customer1", email = "customer@vermeir.nl")
  private val createCustomerRequestBody = ByteString(
    s"""{
       |"name":"${testCustomer.name}"
       |, "email": "${testCustomer.email}"
       |}
       |""".stripMargin
  )
  private val createCustomerRequest = HttpRequest(
    HttpMethods.POST,
    uri = "/create-customer",
    entity = HttpEntity(MediaTypes.`application/json`, createCustomerRequestBody))

  "Customer controller" should {
    "add a new Customer" in {
      createCustomerRequest ~> route ~> check {
        status.isSuccess() shouldEqual true
        val customer = parseCustomerFromResponse(responseAs[String])

        val uuidPattern = """\b[0-9a-f]{8}\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\b[0-9a-f]{12}\b""".r
        uuidPattern.matches(customer.id.get.toString) shouldBe true
        customer.name shouldEqual testCustomer.name
        customer.email shouldEqual testCustomer.email
      }
    }

    "return a list of Customers in response to a GET on /all" in {
      createCustomerRequest ~> route ~> check {
        status.isSuccess() shouldEqual true
      }

      Get("/all") ~> route ~> check {
        status.isSuccess() shouldEqual true
        val customerList = responseAs[String].parseJson.convertTo[List[Customer]]
        customerList.size shouldEqual 1
      }
    }

    "get a customer by id" in {
      val newCustomer = createCustomerRequest ~> route ~> check {
        status.isSuccess() shouldEqual true
        parseCustomerFromResponse(responseAs[String])
      }

      Get(s"/customer/${newCustomer.id.get.toString}") ~> route ~> check {
        status.isSuccess() shouldEqual true
        val customer = parseCustomerFromResponse(responseAs[String])
        customer.name shouldEqual newCustomer.name
      }
    }
  }

  def parseCustomerFromResponse(customerAsString: String): Customer =
    customerAsString.parseJson.convertTo[Customer]
}