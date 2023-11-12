package io.github.matgalv2.employment.model

sealed abstract class TeamType(val value: String)

object TeamType {
  final case object International extends TeamType("international")
  final case object Local extends TeamType("local")
}