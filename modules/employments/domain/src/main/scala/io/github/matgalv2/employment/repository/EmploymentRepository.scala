package io.github.matgalv2.employment.repository

import io.github.matgalv2.employment.model.Employment
import zio.{ IO, UIO }

trait EmploymentRepository {
  def insert(employment: Employment): UIO[Unit]
}
