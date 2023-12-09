package io.github.matgalv2.wagewise.http

import org.http4s.server.Server
import zio.{ Has, RIO, Runtime, ULayer, URIO, ZIO, ZLayer, ZManaged }

case class HttpServer() {
  def bindServer[R](
    httpApp: org.http4s.Http[RIO[R, *], RIO[R, *]]
  ): ZManaged[R, Throwable, org.http4s.server.Server[RIO[R, *]]] = {
    import zio.interop.catz._
    import zio.interop.catz.implicits._

    import cats.effect._
//    import cats.syntax.all._
//    import org.http4s._
//    import org.http4s.dsl.io._
//    import org.http4s.implicits._
    import org.http4s.ember.server.EmberServerBuilder

    implicit val timer: Timer[RIO[R, *]] = ioTimer[R, Throwable]

    ZIO.runtime.toManaged_.flatMap { implicit r: Runtime[R] =>
      EmberServerBuilder
        .default[RIO[R, *]]
        .withHttpApp(httpApp)
        .withHost(HttpServer.host)
        .withPort(HttpServer.port)
        .build
        .toManagedZIO
    }
  }
}
object HttpServer {
  val live: ULayer[Has[HttpServer]] = ZLayer.succeed(HttpServer())

  val host = "0.0.0.0"
  val port = 8080

  def bindServer[R](
    httpApp: org.http4s.Http[RIO[R, *], RIO[R, *]]
  ): URIO[Has[HttpServer] with R, ZManaged[R, Throwable, Server[RIO[R, *]]]] =
    ZIO.access[Has[HttpServer] with R](_.get.bindServer(httpApp))

}
