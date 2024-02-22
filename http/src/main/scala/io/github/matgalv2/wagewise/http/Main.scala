package io.github.matgalv2.wagewise.http

import com.mongodb.{ ServerApi, ServerApiVersion }
import io.github.matgalv2.wagewise.logging.Logger
import io.github.matgalv2.wagewise.ml.SalaryPredictorRandomForest
import org.http4s.HttpApp
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.{ ConnectionString, MongoClient, MongoClientSettings }
import zio.config.typesafe.TypesafeConfig
import zio.magic._
import zio.{ &, App, ExitCode, RIO, Runtime, URIO, ZEnv, ZIO, ZManaged }

object Main extends App {

  org.apache.log4j.Logger.getRootLogger.setLevel(org.apache.log4j.Level.OFF)

  def run(args: List[String]): URIO[ZEnv, ExitCode] =
    Controller.server
      .injectCustom(
        TypesafeConfig.fromDefaultLoader(Config.descriptor).orDie,
        Logger.layer,
        SalaryPredictorRandomForest.layer,
        database,
        DummyImpl.layer,
        HttpServer.live
      )
      //      .provideLayer(dependencies)
      .exitCode

  /*
  def run(args: List[String]) = {
    for {
      config <- ZManaged.service[Config]
      logger <- ZManaged.service[Logging]
      routes <- combinedRoutes.toManaged_
//      server <- server(config, routes)
      server <- HttpServer.bindServer(routes).toManaged_
      host   <- HttpServer.host.toManaged_
      port   <- HttpServer.port.toManaged_
      _      <- logger.info(f"Starting server at $host:$port").toManaged_
    } yield server
  }.useForever
    .injectCustom(
      TypesafeConfig.fromDefaultLoader(Config.descriptor).orDie,
      Logger.layer,
      SalaryPredictorRandomForest.layer,
      database,
      DummyImpl.layer,
      HttpServer.live
    )
    .exitCode
   */

  private def server[R](config: Config, http: HttpApp[RIO[R, *]]) = {
    import cats.effect.Timer
    import org.http4s.ember.server.EmberServerBuilder
    import zio.interop.catz._
    import zio.interop.catz.implicits._

    implicit val timer: Timer[RIO[R, *]] = ioTimer[R, Throwable]

    ZIO.runtime.toManaged_.flatMap { implicit r: Runtime[R] =>
      EmberServerBuilder
        .default[RIO[R, *]]
        .withHost(config.http.host.toString)
        .withPort(config.http.port.value)
        .withHttpApp(http)
        .build
        .toManagedZIO
    }
  }

  private val database = {
    for {
      config <- ZIO.service[Config]
      connectionString =
        f"mongodb://${config.mongo.user}:${config.mongo.password}@${config.mongo.url}/?authSource=admin"
      serverApi = ServerApi.builder.version(ServerApiVersion.V1).build()
      settings = MongoClientSettings
        .builder()
        .applyConnectionString(ConnectionString(connectionString))
        .serverApi(serverApi)
        .build()
      mongoClient   = MongoClient(settings)
      database_test = mongoClient.getDatabase("test")
      _             = database_test.runCommand(Document("ping" -> 1)).head()
      _ <- Logger.info(f"Successfully connected to database (URL: ${config.mongo.url})")
      database = mongoClient.getDatabase("wagewise")
    } yield database
  }.toLayer

  private val dependencies =
    ZEnv.live ++ Logger.layer ++ TypesafeConfig
      .fromDefaultLoader(Config.descriptor)
      .orDie >+> SalaryPredictorRandomForest.layer ++ database ++ DummyImpl.layer ++ HttpServer.live

}
