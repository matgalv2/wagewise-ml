package io.github.matgalv2.wagewise.http

import zio.blocking.Blocking.Service
import zio.{Has, UIO, ULayer, ZEnv, ZIO, ZLayer}
case class DummyImpl() extends DummyService {
  override def log: UIO[Unit] = ZIO.succeed(println("Dummy service is working!"))
}

object DummyImpl{
  def layer: ZLayer[Any, Nothing, Has[DummyService]] =
    ZLayer.succeed(DummyImpl())

  //    ZLayer.succeed(
//      new DummyService {
//        override def log: UIO[Unit] = ZIO.succeed(println("Dummy service is working!"))
//      }
//    )
}