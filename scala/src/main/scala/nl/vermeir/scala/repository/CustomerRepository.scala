package nl.vermeir.scala.repository

import nl.vermeir.scala.service.CustomerService.Customer
import scalikejdbc._

import java.util.UUID

object CustomerRepository {

  scalikejdbc.config.DBs.setupAll()

  implicit val session: AutoSession.type = AutoSession

  object Customer extends SQLSyntaxSupport[Customer] {
    def apply(rs: WrappedResultSet) = new Customer(
      Some(UUID.fromString(rs.string("id"))), rs.string("name"), rs.string("email"))

    def apply(id: Option[UUID], name: String, email: String) = new Customer(id, name, email)
  }

  def save(customer: Customer): Customer = {
    val id = customer.id.getOrElse(java.util.UUID.randomUUID).toString
    sql"insert into customer (id, name, email) values ($id, ${customer.name}, ${customer.email})".update.apply()
    Customer(Option(UUID.fromString(id)), customer.name, customer.email)
  }

  def find(id: UUID): Option[Customer] = {
    val customer = Customer.syntax("c")
    withSQL {
      select.from(Customer as customer).where.eq(customer.id, id.toString)
    }.map(rs => Customer(rs)).single.apply()
  }

  def findAll(): List[Customer] = {
    sql"select * from customer".map(rs => Customer(rs)).list.apply()
  }
}