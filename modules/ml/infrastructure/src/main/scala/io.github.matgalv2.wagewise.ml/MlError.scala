package io.github.matgalv2.wagewise.ml

import io.github.matgalv2.wagewise.ml.predictor.PredictorError

sealed trait MlError extends PredictorError

object MlError {

  sealed trait SparkError extends MlError

  object SparkError {

    final case class MasterURLCannotBeParsed(url: String) extends SparkError
    final case class ColumnNotFound(columns: String*) extends SparkError
    final case class ColumnCannotBeCastToSpecifiedType(column: String, `type`: String) extends SparkError
    final case class CannotCastData(message: String) extends SparkError

    // too broad error - it must be divide into many smaller ones
    final case object AssemblingDataFailed extends SparkError
  }

  final case class EnvironmentVariableIsNotSet(env: String) extends MlError
  final case class DatasetCannotBeFound(path: String) extends MlError
  final case class SthWrongWithFitting(message: String) extends MlError

}
