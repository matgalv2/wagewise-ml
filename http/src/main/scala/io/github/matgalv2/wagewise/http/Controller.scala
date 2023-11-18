package io.github.matgalv2.wagewise.http

import http.generated.ml.MlResource
import io.github.matgalv2.wagewise.http.api.MlApi
import io.github.matgalv2.wagewise.ml.predictor.SalaryPredictor
import zio.logging.Logging
import zio.{&, Has, RIO, Runtime, ZIO}

object Controller {

  /** An effect which, when executed, gives a StoreResource (capable of transforming a StoreHandler into something
    * bindable)
    */
  val makeMlResource: RIO[
    Has[SalaryPredictor] with Has[DummyService] with Logging,
    MlResource[
      RIO[Has[SalaryPredictor] with Has[DummyService] with Logging, *]]] = {
    import zio.interop.catz._
    ZIO.runtime[Has[SalaryPredictor] with Has[DummyService] with Logging].map {
      implicit r: Runtime[
        Has[SalaryPredictor] with Has[DummyService] with Logging] =>
        new MlResource[
          RIO[Has[SalaryPredictor] with Has[DummyService] with Logging, *]
        ]
    }
  }

  /** Our HTTP server implementation, utilizing the Repository Layer
    */
  private val handler = new MlApi()

  private val combineRoutes = {
    import org.http4s.implicits._
    import zio.interop.catz._

    for {
      mlResource <- makeMlResource
    } yield mlResource.routes(handler).orNotFound
  }

  val server
    : ZIO[Has[HttpServer] & Has[SalaryPredictor] & Has[DummyService] & Logging,
          Throwable,
          Nothing] =
    for {
      combinedRoutes <- combineRoutes
      binding        <- HttpServer.bindServer(combinedRoutes)
      res            <- binding.use(_ => ZIO.never)
    } yield res

}
