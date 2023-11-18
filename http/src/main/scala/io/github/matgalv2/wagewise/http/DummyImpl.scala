package io.github.matgalv2.wagewise.http

import zio.{Has, UIO, ULayer, ZIO, ZLayer}
case class DummyImpl() extends DummyService {
  override def log: UIO[Unit] = ZIO.succeed(println("Dummy service is working!"))
}

object DummyImpl{
  val layer: ULayer[Has[DummyService]] =
    ZLayer.succeed(DummyImpl())

//  def layer =
  //    ZLayer.succeed(
//      new DummyService {
//        override def log: UIO[Unit] = ZIO.succeed(println("Dummy service is working!"))
//      }
//    )
}