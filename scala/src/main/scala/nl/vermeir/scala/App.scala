package nl.vermeir.scala

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import nl.vermeir.scala.controller.CustomerController.route

import scala.concurrent.ExecutionContext
import scala.io.StdIn

object App {
  implicit val system: ActorSystem[_] = ActorSystem(Behaviors.empty, "ScalaCustomerService")
  implicit val executionContext: ExecutionContext = system.executionContext

  def main(args: Array[String]): Unit = {
    val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}
