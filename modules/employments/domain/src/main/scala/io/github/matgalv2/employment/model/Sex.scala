package io.github.matgalv2.employment.model

sealed abstract class Sex(val value: String)

object Sex {
  final case object F extends Sex("F")
  final case object M extends Sex("M")
}
