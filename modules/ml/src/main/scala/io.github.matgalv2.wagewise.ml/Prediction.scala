package io.github.matgalv2.wagewise.ml


sealed trait Prediction

object Prediction {
  final case class EarningsPrediction(ratePerHour: Double, salary: Double) extends Prediction
}
