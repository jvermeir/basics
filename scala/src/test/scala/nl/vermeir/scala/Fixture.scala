package nl.vermeir.scala

import scalikejdbc.{AutoSession, scalikejdbcSQLInterpolationImplicitDef}

object Fixture {
  implicit val session: AutoSession.type = AutoSession

  def recreate(): Unit = {
    sql"""
              drop table if exists CUSTOMER
         """.execute.apply()

    sql"""
              create table CUSTOMER (
              id varchar(64) not null primary key,
              name varchar(64),
              email varchar(64)
              )
         """.execute.apply()
  }

  def drop(): Boolean = {
    sql"""
             drop table if exists CUSTOMER
         """.execute.apply()
  }

}