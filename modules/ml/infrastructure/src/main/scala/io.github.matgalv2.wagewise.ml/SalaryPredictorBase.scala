package io.github.matgalv2.wagewise.ml

import io.github.matgalv2.wagewise.ml.predictor.{ PredictorError, SalaryPredictor }
import io.github.matgalv2.wagewise.ml.predictor.PredictorError.{
  DatasetCannotBeFound,
  NoSuchEnvironmentVariable,
  SparkError
}
import io.github.matgalv2.wagewise.ml.predictor.PredictorError.SparkError.{
  AssemblingDataFailed,
  ColumnCannotBeCastToSpecifiedType,
  ColumnNotFound,
  MasterURLCannotBeParsed
}
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.{ OneHotEncoder, StringIndexer, VectorAssembler }
import org.apache.spark.sql
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.{ DataFrame, SparkSession }
import org.apache.spark.sql.types.{ BooleanType, DataType, DateType, FloatType, IntegerType }
import zio.{ IO, UIO, ZIO }

import scala.util.Try

abstract class SalaryPredictorBase(protected val spark: SparkSession) extends SalaryPredictor
object SalaryPredictorBase {

  org.apache.log4j.Logger.getRootLogger.setLevel(org.apache.log4j.Level.OFF)

  private val SPARK_MASTER_URL             = "local"
  private val SPARK_APP_NAME               = "SalaryPredictor"
  private val EMPLOYMENTS_DATASET_PATH_ENV = "EMPLOYMENTS_DATASET_PATH"

  val TARGET_COLUMN   = "rate_per_hour"
  val FEATURES_COLUMN = "features"

  val fieldsMappings: Map[String, DataType] = Map(
    "rate_per_hour" -> FloatType,
    "age" -> IntegerType,
    "experience_years_it" -> IntegerType,
    "team_size" -> IntegerType,
    "full_time" -> BooleanType,
    "paid_days_off" -> BooleanType,
    "insurance" -> BooleanType,
    "training_sessions" -> BooleanType,
    "date_of_employment" -> DateType
  )

  lazy val spark: ZIO[Any, PredictorError, SparkSession] =
    ZIO
      .fromTry(
        Try(
          SparkSession
            .builder()
            .master(SPARK_MASTER_URL)
            .appName(SPARK_APP_NAME)
            .getOrCreate()
        )
      )
      .orElseFail(MasterURLCannotBeParsed(SPARK_MASTER_URL))

  lazy val data: ZIO[Any, PredictorError, DataFrame] =
    for {
      sparkSession <- spark
      readWithOptions = sparkSession.read
        .option("header", "true")
        .option("delimiter", ";")
      path <- ZIO
        .fromOption(Option(System.getenv(EMPLOYMENTS_DATASET_PATH_ENV)))
        .orElseFail(NoSuchEnvironmentVariable(EMPLOYMENTS_DATASET_PATH_ENV))
      csv <- ZIO
        .fromTry(Try(readWithOptions.csv(path)))
        .orElseFail(DatasetCannotBeFound(path))
      data <- castColumns(fieldsMappings, csv)
    } yield data

  def assembleData(dataset: sql.DataFrame): IO[SparkError, DataFrame] = {
    val indexers =
      Processing.categoricalCols.map(col => new StringIndexer().setInputCol(col).setOutputCol(col + "_index"))
    val indexersPipeline = new Pipeline().setStages(indexers)
    val dataIndexed      = indexersPipeline.fit(dataset).transform(dataset)

    val oneHotCols = indexers.map(col => col.getOutputCol)
    val oneHotEncoder = new OneHotEncoder()
      .setInputCols(oneHotCols)
      .setOutputCols(oneHotCols.map(_ + "_onehot"))
    val oneHotEncodedData =
      oneHotEncoder.fit(dataIndexed).transform(dataIndexed)

    val featureCols = Array(
      "age",
      "experience_years_it",
      "team_size",
      "full_time",
      "paid_days_off",
      "insurance",
      "training_sessions"
    ) ++ oneHotCols.map(_ + "_onehot")

    val assembler = new VectorAssembler()
      .setInputCols(featureCols)
      .setOutputCol("features")

    val transformedData = Try(assembler.transform(oneHotEncodedData))

    ZIO.fromTry(transformedData).orElseFail(AssemblingDataFailed)
  }

  def castColumns(mappings: Map[String, DataType], dataFrame: DataFrame): IO[PredictorError, DataFrame] = {
    val df: IO[ColumnCannotBeCastToSpecifiedType, DataFrame] = ZIO.succeed(dataFrame)
    val columnsUnavailableInDataFrame                        = mappings.keySet -- dataFrame.columns.toSet

    if (columnsUnavailableInDataFrame.isEmpty)
      mappings.foldLeft(df) { (dataFrameIth, mapping) =>
        val (columnName, dataType) = mapping
        for {
          df <- dataFrameIth
          newDf <- ZIO
            .fromTry(Try(df.withColumn(columnName, col(columnName).cast(dataType))))
            .orElseFail(ColumnCannotBeCastToSpecifiedType(columnName, dataType.toString))
        } yield newDf
      }
    else
      ZIO.fail(ColumnNotFound(columnsUnavailableInDataFrame.toList: _*))
  }
}
