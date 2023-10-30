package io.github.matgalv2.employment.model

trait WorkForm
object WorkForm {
  final case object Remote extends WorkForm
  final case object Hybrid extends WorkForm
  final case object Stationary extends WorkForm
}
