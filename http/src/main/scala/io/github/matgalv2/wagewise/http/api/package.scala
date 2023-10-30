package io.github.matgalv2.wagewise.http

import cats.data.NonEmptyList
import http.generated.definitions.ErrorResponse
import zio._
import zio.blocking.Blocking
import zio.clock.Clock

package object api {
  type HttpIO[R, A] = RIO[Clock & Blocking & R, A]

//  implicit class ErrorResponseOps(private val companion: ErrorResponse.type) extends AnyVal {
//    def fromValidationErrors(errors: NonEmptyList[Error]): ErrorResponse =
//      companion(errors.map(err => s"${err.message} @ field: ${err.field.fieldName}").toList.toVector)
//
//    def single(message: String): ErrorResponse = companion(Vector(message))
//  }
}
