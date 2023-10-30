package io.github.matgalv2.wagewise.http

import io.github.matgalv2.wagewise.ml.RandomForestRegression
import zio.{ App, ZEnv }

import java.time.LocalDate

object Main extends App {

  def run(args: List[String]) = {

    print("Starting server at localhost:8080\n")
    val regressor = RandomForestRegression
    Controller.inMemoryProg.exitCode
      .provideSomeLayer[ZEnv](httpServer.httpServer.HttpServer.live)
  }

}
