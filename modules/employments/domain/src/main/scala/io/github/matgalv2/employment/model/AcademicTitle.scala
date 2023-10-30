package io.github.matgalv2.employment.model

sealed trait AcademicTitle
object AcademicTitle {
  final case object Licence extends AcademicTitle
  final case object NoDegree extends AcademicTitle
  final case object Master extends AcademicTitle
  final case object Bachelor extends AcademicTitle
  final case object Doctorate extends AcademicTitle
}
