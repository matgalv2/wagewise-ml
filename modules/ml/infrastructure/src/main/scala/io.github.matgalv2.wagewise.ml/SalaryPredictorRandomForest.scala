package io.github.matgalv2.wagewise.ml

import io.github.matgalv2.wagewise.logging.Logger
import io.github.matgalv2.wagewise.ml.converters.employment._
import io.github.matgalv2.wagewise.ml.predictor.SalaryPredictor.ProgrammerFeatures
import io.github.matgalv2.wagewise.ml.predictor.{ PredictorError, SalaryPredictor }
import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.regression.{ RandomForestRegressionModel, RandomForestRegressor }
import org.apache.spark.sql.{ Dataset, Row, SparkSession }
import zio.{ Has, IO, ZIO, ZLayer }

final case class SalaryPredictorRandomForest(model: RandomForestRegressionModel, sparkSession: SparkSession)
    extends SalaryPredictorBase(sparkSession) {

  override def predict(programmers: Seq[ProgrammerFeatures]): IO[PredictorError, Seq[Double]] =
    for {
      df <- ZIO.succeed(
        spark
          .createDataFrame(spark.sparkContext.parallelize(Processing.exampleEmployments))
          .toDF(Processing.columns: _*)
      )
      providedEntities = spark.createDataFrame(programmers.map(_.toModelFeatures))
      unionised     <- SalaryPredictorBase.castColumns(SalaryPredictorBase.fieldsMappings, df.union(providedEntities))
      assembledData <- SalaryPredictorBase.assembleData(unionised)
      predictions = model.transform(assembledData)
    } yield predictions.tail(programmers.size).map(_.getAs[Double]("prediction"))

}

object SalaryPredictorRandomForest {

  private val EVALUATION_METRIC = "mse"

  private def evaluate(model: RandomForestRegressionModel, testData: Dataset[Row]) = {
    val predictions = model.transform(testData)
    val evaluator = new RegressionEvaluator()
      .setLabelCol(SalaryPredictorBase.TARGET_COLUMN)
      .setPredictionCol("prediction")
      .setMetricName(SalaryPredictorRandomForest.EVALUATION_METRIC)

    evaluator.evaluate(predictions)
  }

  /*
  private def saveModel(model: MLWritable, path: String = "modules/ml/infrastructure/src/main/resources/model") =
    ZIO.fromTry(Try(model.write.overwrite().save(path))).orElseFail(CannotSaveModel(path))

  private def loadModel(path: String = "modules/ml/infrastructure/src/main/resources/model") =
    ZIO.fromOption(Try(RandomForestRegressionModel.load(path)).toOption).option
   */

  private def create =
    for {
      _             <- Logger.info("Creating random forest regression model")
      data          <- SalaryPredictorBase.data
      assembledData <- SalaryPredictorBase.assembleData(data)
      Array(trainingData, testData) = assembledData.randomSplit(Array(0.8, 0.2))
      model = new RandomForestRegressor()
        .setLabelCol(SalaryPredictorBase.TARGET_COLUMN)
        .setFeaturesCol(SalaryPredictorBase.FEATURES_COLUMN)
        .setSeed(1234L)
        .setNumTrees(1)
      rfr = model.fit(trainingData)
      mse = evaluate(rfr, testData)
      _            <- Logger.info(f"Fitting model finished ${SalaryPredictorRandomForest.EVALUATION_METRIC}: $mse")
      sparkSession <- SalaryPredictorBase.spark
    } yield SalaryPredictorRandomForest(rfr, sparkSession)

  val layer: ZLayer[Has[Logger[String]], PredictorError, Has[SalaryPredictor]] =
    SalaryPredictorRandomForest.create.toLayer

  /*
  private val spark = SparkSession
    .builder()
    .master("local")
    .appName("SalaryPredictor")
    .getOrCreate()

  // Step 2: Load the data into a DataFrame
  private val dataFrameFromFile =
    spark.read
      .option("header", "true")
      .option("delimiter", ";")
      .csv(System.getenv("EMPLOYMENTS_DATASET_PATH"))
      .drop("salary_monthly", "data_source", "education")

  private val data = castFieldsType(dataFrameFromFile)

  // Step 3: Data Preprocessing
  // Encode categorical features using StringIndexer and OneHotEncoder

  def assembleData(dataset: sql.DataFrame) = {
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

    // Assemble feature vectors
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

    assembler.transform(oneHotEncodedData)
  }

  private val assembledData = assembleData(data)

  // Split the data into training and testing sets
  private val Array(trainingData, testData) =
    assembledData.randomSplit(Array(0.7, 0.3))

  // Step 4: Create and train a Random Forest Regressor model
  private val rf = new RandomForestRegressor()
    .setLabelCol("rate_per_hour")
    .setFeaturesCol("features")
    .setSeed(1234L)
    .setNumTrees(200)

  private val model = rf.fit(trainingData)

  // Step 5: Evaluate the model
  private val predictions = model.transform(testData)
  private val evaluator = new RegressionEvaluator()
    .setLabelCol("rate_per_hour")
    .setPredictionCol("prediction")
    .setMetricName("rmse")

  private val rmse = evaluator.evaluate(predictions)

//  predictions.show()

  def predict(row: Seq[SalaryPredictor.ProgrammerFeatures]): Seq[Double] = {
    val df = spark
      .createDataFrame(spark.sparkContext.parallelize(Processing.exampleEmployments))
      .toDF(Processing.columns: _*)

    val unknown_df = spark.createDataFrame(row.map(_.toModelFeatures))
    val unionised  = castFieldsType(df.union(unknown_df))

//    val dataCleaned   = unionised.na.fill(0)
    val assembledData = assembleData(unionised)

    val predictions = model.transform(assembledData)
    predictions.show()

    predictions.tail(row.size).map(_.getAs[Double]("prediction"))
  }
   */
}
