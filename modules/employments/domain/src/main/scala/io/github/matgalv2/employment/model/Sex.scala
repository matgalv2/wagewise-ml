package io.github.matgalv2.employment.model

sealed trait Sex
object Sex {
  final case object F extends Sex
  final case object M extends Sex
}
