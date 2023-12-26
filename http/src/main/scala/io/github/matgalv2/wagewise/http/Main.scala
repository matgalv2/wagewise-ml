package io.github.matgalv2.wagewise.http

import io.github.matgalv2.wagewise.logging.Logger
import io.github.matgalv2.wagewise.ml.SalaryPredictorRandomForest
import zio.{ App, ExitCode, URIO, ZEnv }
object Main extends App {

  def run(args: List[String]): URIO[ZEnv, ExitCode] =
    Controller.server
      .provideLayer(dependencies)
      .exitCode

  private val dependencies =
    ZEnv.live ++ Logger.layer >+> SalaryPredictorRandomForest.layer ++ DummyImpl.layer ++ HttpServer.live
}
