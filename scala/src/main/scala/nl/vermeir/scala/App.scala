package nl.vermeir.scala

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import nl.vermeir.scala.controller.CustomerController.route
import nl.vermeir.scala.grpc.{CustomerServiceHandler, CustomerServiceImpl}

import scala.concurrent.{ExecutionContext, Future}
import scala.io.StdIn

object App {
  implicit val system: ActorSystem[_] = ActorSystem(Behaviors.empty, "ScalaCustomerService")
  implicit val executionContext: ExecutionContext = system.executionContext

  def main(args: Array[String]): Unit = {

    val service: HttpRequest => Future[HttpResponse] =
      CustomerServiceHandler(new CustomerServiceImpl())

    def startServer: Future[Http.ServerBinding] =
      Http().newServerAt("127.0.0.1", 8090).bind(service)

    startServer
    println("end")

    val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }

}
