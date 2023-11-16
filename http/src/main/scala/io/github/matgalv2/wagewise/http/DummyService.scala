package io.github.matgalv2.wagewise.http

import zio.{Has, UIO, ZIO}

trait DummyService {
  def log: UIO[Unit]
}
object DummyService{
  def log: ZIO[Has[DummyService], Nothing, Unit] = {
    ZIO.serviceWith[DummyService](_.log)
  }
}
