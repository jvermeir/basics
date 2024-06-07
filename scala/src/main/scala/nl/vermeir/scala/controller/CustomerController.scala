package nl.vermeir.scala.controller

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import nl.vermeir.scala.service.CustomerService.{Customer, addCustomer, findCustomerById, getCustomers}
import spray.json.DefaultJsonProtocol._
import spray.json.{JsString, JsValue, RootJsonFormat, deserializationError}

import java.util.UUID
import scala.concurrent.Future

object CustomerController {
  implicit object UuidJsonFormat extends RootJsonFormat[UUID] {
    def write(uuid: UUID): JsString = JsString(uuid.toString)

    def read(value: JsValue): UUID = value match {
      case JsString(uuid) => UUID.fromString(uuid)
      case _ => deserializationError("Expected hexadecimal UUID string")
    }
  }

  implicit val customerFormat: RootJsonFormat[Customer] = jsonFormat3(Customer.apply)

  val route: Route =
    concat(
      getAllCustomers,
      getCustomerById,
      newCustomer
    )

  private def getAllCustomers = {
    path("customers") {
      get {
        val maybeCustomers: Future[List[Customer]] = getCustomers

        onSuccess(maybeCustomers) {
          case customers: List[Customer] => complete(customers)
          case x => {
            println(x)
            complete(StatusCodes.NotFound)
          }
        }
      }
    }
  }

  private def getCustomerById = {
    get {
      pathPrefix("customers" / JavaUUID) { id =>
        val maybeCustomer = findCustomerById(id)

        onSuccess(maybeCustomer) {
          case Some(customer) => complete(customer)
          case None => complete(StatusCodes.NotFound)
        }
      }
    }
  }

  private def newCustomer = {
    path("customers") {
      post {
        entity(as[Customer]) { customer =>
          val saved = addCustomer(customer)
          onSuccess(saved) { _ =>
            complete(saved)
          }
        }
      }
    }
  }
}