package io.github.matgalv2.employment.model

sealed trait TeamType
object TeamType {
  final case object International extends TeamType
  final case object Local extends TeamType
}
