package io.github.matgalv2.wagewise.ml.predictor

sealed trait PredictorError extends Serializable with Product

object PredictorError {

  sealed trait SparkError extends PredictorError

  object SparkError {
    final case class MasterURLCannotBeParsed(url: String) extends SparkError
    final case class ColumnNotFound(columns: String*) extends SparkError
    final case class ColumnCannotBeCastToSpecifiedType(column: String, `type`: String) extends SparkError

    final case object AssemblingDataFailed extends SparkError
    final case class ModelNotFound(path: String) extends SparkError
    final case class CannotSaveModel(path: String) extends SparkError
  }

  final case class NoSuchEnvironmentVariable(env: String) extends PredictorError
  final case class DatasetCannotBeFound(path: String) extends PredictorError
}
