package io.github.matgalv2.wagewise.http

import io.github.matgalv2.wagewise.ml.RandomForestRegression
import zio.{App, ExitCode, URIO, ZEnv}


object Main extends App {

  def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    val _ = RandomForestRegression
    print("Starting server at localhost:8080\n")
    Controller.inMemoryProg.exitCode
      .provideSomeLayer[ZEnv](HttpServer.live)
  }

}
