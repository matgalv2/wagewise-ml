package io.github.matgalv2.employment.model

sealed abstract class WorkForm(val value: String)

object WorkForm {
  final case object Remote extends WorkForm("remote")
  final case object Hybrid extends WorkForm("hybrid")
  final case object Stationary extends WorkForm("stationary")
}
