package io.github.matgalv2.employment.model

sealed abstract class AcademicTitle(val value: String)

object AcademicTitle {
  final case object Licence extends AcademicTitle("Licence")
  final case object NoDegree extends AcademicTitle("No degree")
  final case object Master extends AcademicTitle("Master")
  final case object Bachelor extends AcademicTitle("Bachelor")
  final case object Doctorate extends AcademicTitle("Doctorate")
}