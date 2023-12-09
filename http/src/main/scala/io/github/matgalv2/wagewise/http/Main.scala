package io.github.matgalv2.wagewise.http

import io.github.matgalv2.wagewise.logging.{ Logger, Logging }
import io.github.matgalv2.wagewise.ml.SalaryPredictorRandomForestRegressor
import io.github.matgalv2.wagewise.ml.predictor.SalaryPredictor
import zio.{ &, App, ExitCode, Has, URIO, ZEnv, ZIO }
object Main extends App {

  def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    Controller.server
      .provideSomeLayer[ZEnv & Has[HttpServer] & Has[DummyService] & Has[SalaryPredictor]](Logger.layer)
      .provideSomeLayer[ZEnv & Has[HttpServer] & Has[DummyService]](SalaryPredictorRandomForestRegressor.layer)
      .provideSomeLayer[ZEnv & Has[HttpServer]](DummyImpl.layer)
      .provideSomeLayer[ZEnv](HttpServer.live)
      .exitCode

}
