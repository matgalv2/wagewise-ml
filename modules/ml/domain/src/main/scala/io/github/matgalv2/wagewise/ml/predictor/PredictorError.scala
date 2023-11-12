package io.github.matgalv2.wagewise.ml.predictor

sealed trait PredictorError

object PredictorError {
  final case object WrongValues extends PredictorError
}
