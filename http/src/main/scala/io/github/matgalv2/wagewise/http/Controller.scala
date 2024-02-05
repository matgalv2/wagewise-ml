package io.github.matgalv2.wagewise.http

import http.generated.ml.MlResource
import io.github.matgalv2.wagewise.http.api.MlApi
import io.github.matgalv2.wagewise.http.middleware.AuthorizationMiddleware
import io.github.matgalv2.wagewise.ml.predictor.SalaryPredictor
import zio.{ &, Has, RIO, Runtime, ZIO }
import io.github.matgalv2.wagewise.logging._
import org.http4s.server.middleware.{ CORS, CORSConfig }

import scala.concurrent.duration.DurationInt

object Controller {

  /** An effect which, when executed, gives a MlResource (capable of transforming a MlHandler into something
    * bindable)
    */
  val makeMlResource: RIO[Has[SalaryPredictor] with Has[DummyService] with Has[Logging], MlResource[
    RIO[Has[SalaryPredictor] with Has[DummyService] with Has[Logging], *]
  ]] = {
    import zio.interop.catz._
    ZIO.runtime[Has[SalaryPredictor] with Has[DummyService] with Has[Logging]].map {
      implicit r: Runtime[Has[SalaryPredictor] with Has[DummyService] with Has[Logging]] =>
        new MlResource[RIO[Has[SalaryPredictor] with Has[DummyService] with Has[Logging], *]]
    }
  }

  /** Our HTTP server implementation, utilizing the SalaryPredictor Layer
    */
  private val handler = new MlApi()

  private val combineRoutes = {
    import org.http4s.implicits._
    import zio.interop.catz._
    val methodConfig = CORSConfig(anyOrigin = true, anyMethod = true, allowCredentials = true, maxAge = 1.day.toSeconds)
    for {
      mlResource <- makeMlResource
    } yield CORS(AuthorizationMiddleware.authorize(mlResource.routes(handler)), methodConfig).orNotFound
  }

  val server: ZIO[Has[HttpServer] & Has[SalaryPredictor] & Has[DummyService] & Has[Logging], Throwable, Nothing] =
    for {
      combinedRoutes <- combineRoutes
      binding        <- HttpServer.bindServer(combinedRoutes)
      _              <- Logger.info(f"Starting server at ${HttpServer.host}:${HttpServer.port}")
      res            <- binding.use(_ => ZIO.never)
    } yield res
}
