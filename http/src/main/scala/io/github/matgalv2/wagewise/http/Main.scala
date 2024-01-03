package io.github.matgalv2.wagewise.http

import com.mongodb.{ ServerApi, ServerApiVersion }
import io.github.matgalv2.wagewise.logging.Logger
import io.github.matgalv2.wagewise.ml.SalaryPredictorRandomForest
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.{ ConnectionString, MongoClient, MongoClientSettings }
import zio.{ App, ExitCode, URIO, ZEnv, ZIO }

import scala.util.Try

object Main extends App {

  org.apache.log4j.Logger.getRootLogger.setLevel(org.apache.log4j.Level.OFF)

  def run(args: List[String]): URIO[ZEnv, ExitCode] =
    Controller.server
      .provideLayer(dependencies)
      .exitCode

  private val database = {
    for {
      mongoURL <- ZIO.fromTry(Try(System.getenv("MONGO_URL")))
      user     <- ZIO.fromTry(Try(System.getenv("MONGO_USER")))
      password <- ZIO.fromTry(Try(System.getenv("MONGO_PASSWORD")))
      connectionString = f"mongodb://$user:$password@$mongoURL/?authSource=admin"
      serverApi        = ServerApi.builder.version(ServerApiVersion.V1).build()
      settings = MongoClientSettings
        .builder()
        .applyConnectionString(ConnectionString(connectionString))
        .serverApi(serverApi)
        .build()
      mongoClient   = MongoClient(settings)
      database_test = mongoClient.getDatabase("test")
      _             = database_test.runCommand(Document("ping" -> 1)).head()
      _ <- Logger.info(f"Successfully connected to database (URL: $mongoURL)")
      database = mongoClient.getDatabase("wagewise")
    } yield database
  }.toLayer

  private val dependencies =
    ZEnv.live ++ Logger.layer >+> SalaryPredictorRandomForest.layer ++ database ++ DummyImpl.layer ++ HttpServer.live
}
