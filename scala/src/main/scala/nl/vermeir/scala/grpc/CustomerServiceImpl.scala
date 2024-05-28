package nl.vermeir.scala.grpc

import nl.vermeir.scala.service.CustomerService.{Customer, addCustomer, findCustomerById, getCustomers}

import java.util.UUID
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}
import scala.language.postfixOps

class CustomerServiceImpl extends CustomerService {
  override def getCustomerById(in: GetCustomerByIdRequest): Future[GetCustomerByIdResponse] = {
    val maybeCustomer = findCustomerById(UUID.fromString(in.id))
    val customer = Await.result(maybeCustomer, 1 second)
    // TODO: how do we handle errors?
    customer.getOrElse(return null)
    Future.successful(GetCustomerByIdResponse(
      customer.get.id.get.toString,
      customer.get.name,
      customer.get.email
    ))
  }

  override def listCustomers(in: ListCustomerRequest): Future[ListCustomerResponse] = {
    val maybeCustomers = getCustomers
    val customers =
      Await.result(maybeCustomers, 1 second)
        .map(c => GetCustomerByIdResponse(c.id.get.toString, c.name, c.email))
    Future.successful(ListCustomerResponse(customers))
  }

  override def createCustomer(in: CreateCustomerRequest): Future[GetCustomerByIdResponse] = {
    val newCustomer = Customer(id = Option.empty, name = in.name, email = in.email)
    val maybeCustomer = addCustomer(newCustomer)
    val customer = Await.result(maybeCustomer, 1 second)
    Future.successful(GetCustomerByIdResponse(
      customer.id.get.toString,
      customer.name,
      customer.email
    ))
  }
}
