package io.github.matgalv2.wagewise.ml.model

sealed trait Prediction

object Prediction {
  final case class EarningsPrediction(ratePerHour: Double, salary: Double) extends Prediction
  object EarningsPrediction{
    type ModelFeatures = (
      String,
      Int,
      String,
      String,
      Int,
      String,
      String,
      String,
      String,
      String,
      Double,
      String,
      String,
      String,
      Int,
      String,
      String,
      Boolean,
      Boolean,
      Boolean,
      Boolean
    )
  }
}
