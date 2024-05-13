package nl.vermeir.scala

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import nl.vermeir.scala.controller.CustomerController.route
import nl.vermeir.scala.grpc.{CustomerServiceHandler, CustomerServiceImpl}

import scala.concurrent.{ExecutionContext, Future}

object App {
  implicit val system: ActorSystem[_] = ActorSystem(Behaviors.empty, "ScalaCustomerService")
  implicit val executionContext: ExecutionContext = system.executionContext

  def main(args: Array[String]): Unit = {

    val service: HttpRequest => Future[HttpResponse] =
      CustomerServiceHandler(new CustomerServiceImpl())

    Http().newServerAt("localhost", 8090).bind(service)
    Http().newServerAt("localhost", 8080).bind(route)

    println(s"\nHTTP REST interface: http://localhost:8080")
    println(s"gRPC interface: http://localhost:8090")
  }
}
