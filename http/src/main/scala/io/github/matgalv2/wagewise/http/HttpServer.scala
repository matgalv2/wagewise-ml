package io.github.matgalv2.wagewise.http

import com.comcast.ip4s.{ Host, Port }
import org.http4s.server.Server
import zio.{ Has, RIO, Runtime, URIO, URLayer, ZIO, ZManaged }

case class HttpServer(port: Port, host: Host) {
  def bindServer[R](
    httpApp: org.http4s.Http[RIO[R, *], RIO[R, *]]
  ): ZManaged[R, Throwable, org.http4s.server.Server[RIO[R, *]]] = {
    import zio.interop.catz._
    import zio.interop.catz.implicits._

    import cats.effect._
    import org.http4s.ember.server.EmberServerBuilder

    implicit val timer: Timer[RIO[R, *]] = ioTimer[R, Throwable]

    ZIO.runtime.toManaged_.flatMap { implicit r: Runtime[R] =>
      EmberServerBuilder
        .default[RIO[R, *]]
        .withHttpApp(httpApp)
        .withHost(host.toString)
        .withPort(port.value)
        .build
        .toManagedZIO
    }
  }
}
object HttpServer {
  val live: URLayer[Has[Config], Has[HttpServer]] = {
    for {
      config <- ZManaged.service[Config]
    } yield HttpServer(config.http.port, config.http.host)
  }.toLayer

  def bindServer[R](
    httpApp: org.http4s.Http[RIO[R, *], RIO[R, *]]
  ): URIO[Has[HttpServer] with R, ZManaged[R, Throwable, Server[RIO[R, *]]]] =
    ZIO.access[Has[HttpServer] with R](_.get.bindServer(httpApp))

  def host: RIO[Has[HttpServer], String] =
    ZIO.access[Has[HttpServer]](_.get.host.toString)

  def port: RIO[Has[HttpServer], String] =
    ZIO.access[Has[HttpServer]](_.get.port.toString)

}
