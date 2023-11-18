package io.github.matgalv2.wagewise.http

import io.github.matgalv2.wagewise.ml.RandomForestRegression
import io.github.matgalv2.wagewise.ml.predictor.SalaryPredictor
import zio.logging.Logging
import zio.{&, App, ExitCode, Has, URIO, ZEnv}

object Main extends App {

  def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    val _ = RandomForestRegression
    print(s"Starting server at ${HttpServer.host}:${HttpServer.port}\n")
    DummyService.log.provideLayer(DummyImpl.layer).exitCode <&
      Controller.server
        .provideSomeLayer[ZEnv & Has[HttpServer] & Has[DummyService] & Has[
          SalaryPredictor]](Logging.console())
        .provideSomeLayer[ZEnv & Has[HttpServer] & Has[DummyService]](
          RandomForestRegression.layer)
        .provideSomeLayer[ZEnv & Has[HttpServer]](DummyImpl.layer)
        .exitCode
        .provideSomeLayer[ZEnv](HttpServer.live)
  }

}
