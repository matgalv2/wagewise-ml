package io.github.matgalv2.wagewise.ml

import io.github.matgalv2.wagewise.ml.Processing.ProgrammerFeatures
import org.apache.hadoop.shaded.org.eclipse.jetty.websocket.common.frames.DataFrame
import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.regression.{ GBTRegressor, RandomForestRegressor }
import org.apache.spark.ml.feature.{ StringIndexer, VectorAssembler }
import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.sql.functions._
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.OneHotEncoder
import org.apache.spark.sql
import org.apache.spark.sql.types.{ BooleanType, DateType, FloatType, IntegerType }

object RandomForestRegression {
  // Step 1: Create a SparkSession

  import org.apache.log4j.Logger
  import org.apache.log4j.Level
  Logger.getRootLogger.setLevel(Level.OFF)

  private def castFieldsType(dataFrame: sql.DataFrame) =
    dataFrame
      .withColumn("rate_per_hour", col("rate_per_hour").cast(FloatType))
      .withColumn("age", col("age").cast(IntegerType))
      .withColumn("experience_years_it", col("experience_years_it").cast(IntegerType))
      .withColumn("team_size", col("team_size").cast(IntegerType))
      .withColumn("full_time", col("full_time").cast(BooleanType))
      .withColumn("paid_days_off", col("paid_days_off").cast(BooleanType))
      .withColumn("insurance", col("insurance").cast(BooleanType))
      .withColumn("training_sessions", col("training_sessions").cast(BooleanType))
      .withColumn("date_of_employment", col("date_of_employment").cast(DateType))

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
      .csv("modules/ml/src/main/resources/employments.csv")
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
    .setNumTrees(150)

  private val model = rf.fit(trainingData)

  // Step 5: Evaluate the model
  private val predictions = model.transform(testData)
  private val evaluator = new RegressionEvaluator()
    .setLabelCol("rate_per_hour")
    .setPredictionCol("prediction")
    .setMetricName("rmse") // You can choose a different metric if needed

  private val rmse = evaluator.evaluate(predictions)
  println(s"Root Mean Squared Error (RMSE): $rmse")

//  predictions.show()

  def makePrediction(row: Seq[ProgrammerFeatures]): Double = {
    val df = spark
      .createDataFrame(spark.sparkContext.parallelize(Processing.exampleEmployments))
      .toDF(Processing.columns: _*)
    val unknown_df = spark.createDataFrame(row)
    val unionised  = castFieldsType(df.union(unknown_df))

    val dataCleaned   = unionised.na.fill(0)
    val assembledData = assembleData(dataCleaned)

    val predictions = model.transform(assembledData)

    predictions.show()

    predictions.collect().last.getAs[Double]("prediction")
  }

}
