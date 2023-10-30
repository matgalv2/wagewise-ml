package io.github.matgalv2.employment.infrastructure.repository

import io.github.matgalv2.employment.model.Employment
import io.github.matgalv2.employment.repository.EmploymentRepository
import zio.IO

class EmploymentRepositoryMongo extends EmploymentRepository {
  override def insert(employment: Employment): IO[Nothing, Unit] = ???
}
