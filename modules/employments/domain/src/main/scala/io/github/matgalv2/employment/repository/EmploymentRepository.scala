package io.github.matgalv2.employment.repository

import io.github.matgalv2.employment.model.Employment
import zio.{Task, ZIO}
import zio._

//@accessible //generates accessors in companion object
trait EmploymentRepository {
  def insert(employment: Employment): Task[Unit]
}
object EmploymentRepository {
  def insert(employment: Employment): ZIO[Has[EmploymentRepository], Throwable, Unit] = {
    ZIO.serviceWith[EmploymentRepository](_.insert(employment))
  }
}
