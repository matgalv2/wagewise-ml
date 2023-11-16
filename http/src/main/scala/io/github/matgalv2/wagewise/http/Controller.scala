package io.github.matgalv2.wagewise.http


import http.generated.ml.MlResource
import io.github.matgalv2.wagewise.http.api.MlApi
import zio.{&, Has, RIO, Runtime, ZEnv, ZIO}


object Controller {

  /** An effect which, when executed, gives a StoreResource (capable of transforming a StoreHandler into something
    * bindable)
    */
  val makeMlResource: RIO[Has[DummyService], MlResource[RIO[Has[DummyService], *]]] = {
    import zio.interop.catz._
    ZIO.runtime[Has[DummyService]].map { implicit r: Runtime[Has[DummyService]] =>
      new MlResource[RIO[Has[DummyService], *]]
    }
  }

  /** Our HTTP server implementation, utilizing the Repository Layer
    */
  val handler = new MlApi()


  val combineRoutes = {
    import org.http4s.implicits._
    import zio.interop.catz._

    for {
      mlResource <- makeMlResource
    } yield mlResource.routes(handler).orNotFound
  }

  val server: ZIO[Has[HttpServer] with Has[DummyService], Throwable, Nothing] =
    for {
      combinedRoutes <- combineRoutes
      binding        <- HttpServer.bindServer(combinedRoutes)
      res            <- binding.use(_ => ZIO.never)
    } yield res

//  val inMemoryProg: ZIO[zio.ZEnv & Has[HttpServer], Throwable, Nothing] =
//    prog
//      .provideSomeLayer[ZEnv & Has[HttpServer]](DummyImpl.layer)

}
