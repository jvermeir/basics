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

/*
grpcurl -d '{"name": "foo"}' -plaintext \
    -import-path ~/dev/basics/scala/src/main/protobuf \
    -proto ticker.proto \
    localhost:8090 ticker.TickerService.MonitorSymbol
    nl.vermeir.scala.grpc

grpcurl -d '{"name": "foo"}' -plaintext \
    -import-path /home/aengelen/dev/akka-grpc-intro-video/src/main/protobuf  \
    -proto ticker.proto \
   localhost:8080 ticker.TickerService.MonitorSymbol

grpcurl -d '{"id": "562efef1-b78e-4db2-875b-a014facbf0d4"}' -plaintext \
    -import-path ~/dev/basics/scala/src/main/protobuf \
        -proto customer.proto \
            localhost:8090 nl.vermeir.scala.grpc.CustomerService.GetCustomerById

grpcurl -d '{"name": "name1", "email":"email1"}' -plaintext \
    -import-path ~/dev/basics/scala/src/main/protobuf \
        -proto customer.proto \
            localhost:8090 nl.vermeir.scala.grpc.CustomerService.CreateCustomer

grpcurl -d '{}' -plaintext \
    -import-path ~/dev/basics/scala/src/main/protobuf \
        -proto customer.proto \
            localhost:8090 nl.vermeir.scala.grpc.CustomerService.ListCustomers

 */