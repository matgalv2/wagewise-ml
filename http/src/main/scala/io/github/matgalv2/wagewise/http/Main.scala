package io.github.matgalv2.wagewise.http

import io.github.matgalv2.wagewise.logging.Logger
import io.github.matgalv2.wagewise.ml.SalaryPredictorRandomForestRegressor
import zio.{ App, ExitCode, URIO, ZEnv }
object Main extends App {

  def run(args: List[String]): URIO[ZEnv, ExitCode] =
    Controller.server
      .provideLayer(dependencies)
      .exitCode

  private val dependencies =
    ZEnv.live ++ Logger.layer >+> SalaryPredictorRandomForestRegressor.layer ++ DummyImpl.layer ++ HttpServer.live ++ Logger.layer
}
