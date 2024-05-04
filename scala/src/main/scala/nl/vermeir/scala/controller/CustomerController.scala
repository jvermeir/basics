package nl.vermeir.scala.controller

import akka.Done
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import nl.vermeir.scala.service.CustomerService.{Customer, addCustomer, findCustomerById, getCustomers}
import spray.json.DefaultJsonProtocol._

import scala.concurrent.Future

object CustomerController {

  val route: Route =
    concat(
      getAllCustomers,
      findById,
      addNewCustomer
    )

  private def getAllCustomers = {
    get {
      path("all") {
        val maybeCustomers: Future[List[Customer]] = getCustomers()

        onSuccess(maybeCustomers) {
          case customers: List[Customer] => complete(customers)
          case _ => complete(StatusCodes.NotFound)
        }
      }
    }
  }

  private def findById = {
    get {
      pathPrefix("customer" / LongNumber) { id =>
        val maybeCustomer = findCustomerById(id)
        onSuccess(maybeCustomer) {
          case Some(customer) => complete(customer)
          case None => complete(StatusCodes.NotFound)
        }
      }
    }
  }

  private def addNewCustomer = {
    post {
      path("create-customer") {
        entity(as[Customer]) { customer =>
          val saved: Future[Done] = addCustomer(customer)
          onSuccess(saved) { _ =>
            complete("customer created")
          }
        }
      }
    }
  }
}