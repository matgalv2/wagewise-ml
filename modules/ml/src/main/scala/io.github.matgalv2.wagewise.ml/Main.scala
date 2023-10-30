package io.github.matgalv2.wagewise.ml

import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.feature.VectorIndexer
import org.apache.spark.ml.regression.{ RandomForestRegressionModel, RandomForestRegressor }
import org.apache.spark.mllib.clustering.{ KMeans, KMeansModel }
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.sql.{ Column, DataFrame, SparkSession }
import org.apache.spark.sql.functions._
import org.joda.time.DateTime
import org.joda.time.format.{ DateTimeFormat, DateTimeFormatter }

import scala.collection.compat.immutable.ArraySeq
import scala.util.matching.Regex

object Main {

  def main(args: Array[String]): Unit = {
//    val conf = new SparkConf().setAppName("Salary Predictor").setMaster("local")
//    val sc   = new SparkContext(conf)
//    sc.setLogLevel("OFF")

    import org.apache.log4j.Logger
    import org.apache.log4j.Level
    Logger.getRootLogger.setLevel(Level.OFF)

    val spark: SparkSession = SparkSession
      .builder()
      .master("local")
      .appName("salary predictor")
      .getOrCreate()

    import spark.implicits._

//    val rdd = spark.sparkContext.parallelize(
//      List(
//        Vectors.dense(Array(100000.0, 15000.0, 10.0)),
//        Vectors.dense(Array(1500000.0, 30000.0, 2.0)),
//        Vectors.dense(Array(1500000.0, 30000.0, 1.0))
//      )
//    )
//    val numClusters   = 2
//    val numIterations = 20
//
//    val clusters: KMeansModel = KMeans.train(rdd, numClusters, numIterations)
//    val testSet               = Vectors.dense(Array(100000.0, 15000.0, 10.0))
//    val samplePrediction      = clusters.predict(testSet)
//    println(samplePrediction.toString + "<- predicted")

    val dataset: DataFrame = spark.read
//      .options(Map("delimiter" -> "\t", "header" -> "true"))
//      .csv("modules/ml/src/main/resources/employments_processed.csv")
      .options(Map("delimiter" -> ";", "header" -> "true"))
      .csv("modules/ml/src/main/resources/employments.csv")
      .drop("salary_monthly", "data_source")

//    dataset.columns.foreach { x =>
//      println(dataset.select(x).collect())
//    }

//    val p = for {
//      columnName <- dataset.columns
//      column = dataset.col(columnName)
//      row <- column.
//    }

//      .head() // collect result of aggregation
//      .getValuesMap[Boolean](cols) // now get columns which are "true"
//      .filter { case (c, hasNulls) => hasNulls }
//      .keys
//      .toSeq // and get the name of those columns
//
//    dataset
//      .select(columnsToSelect.head, columnsToSelect.tail: _*)
//      .show()

    //    val featureIndexer = new VectorIndexer()
//      .setInputCol("sex")
//      .setOutputCol("indexedFeatures")
//      .setMaxCategories(2)
//      .fit(dataset)

//    dataset.show(10)

    val allFeatures = List(
      "date_of_employment",
      "age",
      "sex",
      "country",
      "experience_years_it",
      "languages",
      "speciality",
      "core_programming_language",
      "academic_title",
      "education",
      "education_towards_it",
      "rate_per_hour",
      "salary_monthly",
      "company_country",
      "company_type",
      "work_form",
      "team_size",
      "team_type",
      "form_of_employment",
      "full_time",
      "paid_days_off",
      "insurance",
      "training_sessions",
      "data_source"
    )

    val categoricalFeatures = List(
      "sex",
      "country",
      "languages",
      "speciality",
      "core_programming_language",
      "academic_title",
      "company_country",
      "company_type",
      "work_form",
      "team_type",
      "form_of_employment"
    )
    val map_domain_values =
      dataset
        .select(categoricalFeatures.map(x => collect_set(x)): _*)
//        .select(categoricalFeatures.map(x => countDistinct(x)): _*)
        .collect()
        .map(x => x.getValuesMap[Any](x.schema.fieldNames))
        .head

    println(map_domain_values)
    val extractor: Regex = "collect_set\\((.*)\\)".r

    val domainSizePerEnum = map_domain_values.map {
      case (key, array) if array.isInstanceOf[Iterable[_]] =>
        val extractor(columnName) = key
        (columnName, array.asInstanceOf[Iterable[_]].toList.length)
    }
    val COLUMN_POSTFIX = "_mapped"

//    def getVectorIndexers(map_column_domain: Map[String, Int]) =
//      map_column_domain.map {
//        case (columnName, size) =>
//          new VectorIndexer()
//            .setInputCol(columnName)
//            .setOutputCol(columnName + COLUMN_POSTFIX)
//            .setMaxCategories(size)
//      }.toList
//
//    val booleanFeatures = List("education_towards_it",
//                               "full_time",
//                               "paid_days_off",
//                               "insurance",
//                               "training_sessions")
//
//    val Array(trainingData, testData) = dataset.randomSplit(Array(0.7, 0.3))
//
//    // Train a RandomForest model.
//    val rf = new RandomForestRegressor()
//      .setLabelCol("rate_per_hour")
//      .setFeaturesCol("sex")
//      .setFeaturesCol("country")
//      .setFeaturesCol("languages")
//      .setFeaturesCol("speciality")
//      .setFeaturesCol("core_programming_language")
//      .setFeaturesCol("academic_title")
//      .setFeaturesCol("company_country")
//      .setFeaturesCol("company_type")
//      .setFeaturesCol("work_form")
//      .setFeaturesCol("team_type")
//      .setFeaturesCol("form_of_employment")
//
//    // Chain indexer and forest in a Pipeline.
//    println(getVectorIndexers(domainSizePerEnum))
//    val pipeline = new Pipeline()
//      .setStages((rf :: getVectorIndexers(domainSizePerEnum)).toArray)
//
//    // Train model. This also runs the indexer.
//    val model = pipeline.fit(trainingData)
//
//    // Make predictions.
//    val predictions = model.transform(testData)
//
//    // Select example rows to display.
//    predictions
//      .select("prediction", "rate_per_hour", "speciality_mapped")
//      .show(5)
//
//    // Select (prediction, true label) and compute test error.
//    val evaluator = new RegressionEvaluator()
//      .setLabelCol("rate_per_hour")
//      .setPredictionCol("prediction")
//      .setMetricName("rmse")
//    val rmse = evaluator.evaluate(predictions)
//    println(s"Root Mean Squared Error (RMSE) on test data = $rmse")
//
//    val rfModel = model.stages(1).asInstanceOf[RandomForestRegressionModel]
//    println(s"Learned regression forest model:\n ${rfModel.toDebugString}")

//    val datasetWithMappedBooleans =
//      dataset
//        .withColumn("education_towards_it", when($"education_towards_it" === true, 1))

//    dataset.select($"rate_per_hour" + 1).show(10)
//
//    import java.text.SimpleDateFormat
//
//    val format = new SimpleDateFormat("yyyy-MM-dd")
//    val date   = format.parse("2018-03-03").getTime
//
////    dataset
////      .withColumn(
////        "date_of_employment",
////        map(
////          $"date_of_employment",
////          $"date_of_employment".lit(DateTime.parse($"date_of_employment", DateTimeFormat.forPattern("yyyy-MM-dd")).getTime)
////        )
////      )
////      .show(10)
//
//    val Array(trainingDataset, testDataset) = dataset.randomSplit(Array(0.7, 0.3))
//
//    // Train a RandomForest model.
//    val rf = new RandomForestRegressor()
//      .setLabelCol("rate_per_hour")
//      .setFeaturesCol("date_of_employment")
//      .setFeaturesCol("age")
//      .setFeaturesCol("experience_years_it")
//      .setFeaturesCol("team_size")
//      .setFeaturesCol("is_sex_F")
//      .setFeaturesCol("is_sex_M")
//      .setFeaturesCol("is_country_Spain")
//      .setFeaturesCol("is_country_France")
//      .setFeaturesCol("is_country_Poland")
//      .setFeaturesCol("is_country_Greece")
//      .setFeaturesCol("is_country_Sweden")
//      .setFeaturesCol("is_country_Germany")
//      .setFeaturesCol("is_country_United Kingdom")
//      .setFeaturesCol("is_country_Russia")
//      .setFeaturesCol("is_country_Italy")
//      .setFeaturesCol("is_languages_Polish,English")
//      .setFeaturesCol("is_languages_Italian")
//      .setFeaturesCol("is_languages_Spanish,English")
//      .setFeaturesCol("is_languages_Swedish")
//      .setFeaturesCol("is_languages_Russian")
//      .setFeaturesCol("is_languages_Spanish")
//      .setFeaturesCol("is_languages_German,English")
//      .setFeaturesCol("is_languages_English")
//      .setFeaturesCol("is_languages_Greek")
//      .setFeaturesCol("is_languages_Russian,English")
//      .setFeaturesCol("is_languages_English,English")
//      .setFeaturesCol("is_languages_Italian,English")
//      .setFeaturesCol("is_languages_Swedish,English")
//      .setFeaturesCol("is_languages_French")
//      .setFeaturesCol("is_languages_French,English")
//      .setFeaturesCol("is_languages_German")
//      .setFeaturesCol("is_languages_Greek,English")
//      .setFeaturesCol("is_languages_Polish")
//      .setFeaturesCol("is_speciality_Tech lead")
//      .setFeaturesCol("is_speciality_DB Administrator")
//      .setFeaturesCol("is_speciality_Data quality manager")
//      .setFeaturesCol("is_speciality_IT Security specialist")
//      .setFeaturesCol("is_speciality_Cloud system engineer")
//      .setFeaturesCol("is_speciality_Data scientist")
//      .setFeaturesCol("is_speciality_Backend")
//      .setFeaturesCol("is_speciality_Computer scientist")
//      .setFeaturesCol("is_speciality_Applications engineer")
//      .setFeaturesCol("is_speciality_Frontend")
//      .setFeaturesCol("is_speciality_Software Engineer")
//      .setFeaturesCol("is_speciality_Other")
//      .setFeaturesCol("is_speciality_Systems analyst")
//      .setFeaturesCol("is_speciality_Web administrator")
//      .setFeaturesCol("is_core_programming_language_Kotlin")
//      .setFeaturesCol("is_core_programming_language_Swift")
//      .setFeaturesCol("is_core_programming_language_Cobol")
//      .setFeaturesCol("is_core_programming_language_Go")
//      .setFeaturesCol("is_core_programming_language_Objective-C")
//      .setFeaturesCol("is_core_programming_language_JavaScript")
//      .setFeaturesCol("is_core_programming_language_R")
//      .setFeaturesCol("is_core_programming_language_Ruby")
//      .setFeaturesCol("is_core_programming_language_PHP")
//      .setFeaturesCol("is_core_programming_language_Python")
//      .setFeaturesCol("is_core_programming_language_Java")
//      .setFeaturesCol("is_core_programming_language_Other")
//      .setFeaturesCol("is_academic_title_Master")
//      .setFeaturesCol("is_academic_title_Bachelor")
//      .setFeaturesCol("is_academic_title_Licence")
//      .setFeaturesCol("is_academic_title_Doctorate")
//      .setFeaturesCol("is_academic_title_No degree")
//      .setFeaturesCol("education_towards_it")
//      .setFeaturesCol("is_company_country_Spain")
//      .setFeaturesCol("is_company_country_France")
//      .setFeaturesCol("is_company_country_Poland")
//      .setFeaturesCol("is_company_country_Greece")
//      .setFeaturesCol("is_company_country_Sweden")
//      .setFeaturesCol("is_company_country_Germany")
//      .setFeaturesCol("is_company_country_United Kingdom")
//      .setFeaturesCol("is_company_country_Russia")
//      .setFeaturesCol("is_company_country_Italy")
//      .setFeaturesCol("is_company_type_Software house")
//      .setFeaturesCol("is_company_type_Big tech")
//      .setFeaturesCol("is_company_type_Startup")
//      .setFeaturesCol("is_company_type_Company")
//      .setFeaturesCol("is_company_type_Public institution")
//      .setFeaturesCol("is_company_type_Other")
//      .setFeaturesCol("is_company_type_Corporation")
//      .setFeaturesCol("is_work_form_hybrid")
//      .setFeaturesCol("is_work_form_stationary")
//      .setFeaturesCol("is_work_form_remote")
//      .setFeaturesCol("is_team_type_local")
//      .setFeaturesCol("is_team_type_international")
//      .setFeaturesCol("is_form_of_employment_contractor")
//      .setFeaturesCol("is_form_of_employment_employee")
//      .setFeaturesCol("full_time")
//      .setFeaturesCol("paid_days_off")
//      .setFeaturesCol("insurance")
//      .setFeaturesCol("training_sessions")
//
//    // Chain indexer and forest in a Pipeline.
//    val pipeline = new Pipeline().setStages(Array(rf))
//
//    // Train model. This also runs the indexer.
//    val model = pipeline.fit(trainingDataset)
//
//    // Make predictions.
//    val predictions = model.transform(testDataset)
//
//    // Select example rows to display.
////    predictions.select("prediction", "label", "features").show(5)
////
////    // Select (prediction, true label) and compute test error.
////    val evaluator = new RegressionEvaluator()
////      .setLabelCol("label")
////      .setPredictionCol("prediction")
////      .setMetricName("rmse")
////    val rmse = evaluator.evaluate(predictions)
////    println(s"Root Mean Squared Error (RMSE) on test data = $rmse")
////
////    val rfModel = model.stages(1).asInstanceOf[RandomForestRegressionModel]
////    println(s"Learned regression forest model:\n ${rfModel.toDebugString}")
//
//    /*
//
//    // Automatically identify categorical features, and index them.
//    // Set maxCategories so features with > 4 distinct values are treated as continuous.
//    val featureIndexer = new VectorIndexer()
//      .setInputCol("features")
//      .setOutputCol("indexedFeatures")
//      .setMaxCategories(4)
//      .fit(dataset)
//
//    // Split the data into training and test sets (30% held out for testing).
//    val Array(trainingData, testData) = dataset.randomSplit(Array(0.7, 0.3))
//
//    // Train a RandomForest model.
//    val rf = new RandomForestRegressor()
//      .setLabelCol("label")
//      .setFeaturesCol("indexedFeatures")
//
//    // Chain indexer and forest in a Pipeline.
//    val pipeline = new Pipeline()
//      .setStages(Array(featureIndexer, rf))
//
//    // Train model. This also runs the indexer.
//    val model = pipeline.fit(trainingData)
//
//    // Make predictions.
//    val predictions = model.transform(testData)
//
//    // Select example rows to display.
//    predictions.select("prediction", "label", "features").show(5)
//
//    // Select (prediction, true label) and compute test error.
//    val evaluator = new RegressionEvaluator()
//      .setLabelCol("label")
//      .setPredictionCol("prediction")
//      .setMetricName("rmse")
//    val rmse = evaluator.evaluate(predictions)
//    println(s"Root Mean Squared Error (RMSE) on test data = $rmse")
//
//    val rfModel = model.stages(1).asInstanceOf[RandomForestRegressionModel]
//    println(s"Learned regression forest model:\n ${rfModel.toDebugString}")
//
//
//     */
  }

}
