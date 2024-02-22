package io.github.matgalv2.wagewise.http

import http.generated.ml.MlResource
import io.github.matgalv2.wagewise.http.api.MlApi
import io.github.matgalv2.wagewise.http.middleware.AuthorizationMiddleware
import io.github.matgalv2.wagewise.ml.predictor.SalaryPredictor
import zio.{ &, Has, RIO, Runtime, ZIO }
import io.github.matgalv2.wagewise.logging._

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

  val combinedRoutes = {
    import org.http4s.implicits._
    import zio.interop.catz._

    for {
      mlResource <- makeMlResource
    } yield mlResource.routes(handler).orNotFound
  }

  private val authedRoutes = {
    import org.http4s.implicits._
    import zio.interop.catz._

    for {
      mlResource <- makeMlResource
    } yield AuthorizationMiddleware.authorization(mlResource.routes(handler)).orNotFound
  }

  val server =
    for {
      combinedRoutes <- combinedRoutes
      binding        <- HttpServer.bindServer(combinedRoutes)
      host           <- HttpServer.host
      port           <- HttpServer.port
      _              <- Logger.info(f"Starting server at $host:$port")
      res            <- binding.useForever
    } yield res
}
