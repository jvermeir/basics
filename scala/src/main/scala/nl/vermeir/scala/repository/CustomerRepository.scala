package nl.vermeir.scala.repository

import nl.vermeir.scala.service.CustomerService.Customer
import scalikejdbc._

object CustomerRepository {

  // TODO: use property so we can test
  Class.forName("org.h2.Driver")
  ConnectionPool.singleton("jdbc:h2:mem:hello", "user", "pass")

  // ad-hoc session provider on the REPL
  implicit val session = AutoSession

  sql"""
create table customers (
id serial not null primary key,
name varchar(64),
email varchar(64)
)
""".execute.apply()

  object Customer extends SQLSyntaxSupport[Customer] {
    override val tableName = "customers"

    def apply(rs: WrappedResultSet) = new Customer(
      rs.long("id"), rs.string("name"), rs.string("email"))
  }

  // TODO: allow empty id so we can generate a new one
  def save(customer: Customer): Customer = {
    sql"insert into customers (id, name, email) values (${customer.id}, ${customer.name}, ${customer.email})".update.apply()
    customer
  }

  def find(id: Long): Option[Customer] = {
    val customer = Customer.syntax("c")
    withSQL {
      select.from(Customer as customer).where.eq(customer.id, id)
    }.map(rs => Customer(rs)).single.apply()
  }

  def findAll(): List[Customer] = {
    sql"select * from customers".map(rs => Customer(rs)).list.apply()
  }
}