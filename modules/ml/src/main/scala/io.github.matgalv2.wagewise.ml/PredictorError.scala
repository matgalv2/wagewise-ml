package io.github.matgalv2.wagewise.ml

sealed trait PredictorError

object PredictorError {
  final case object WrongValues extends PredictorError
}
