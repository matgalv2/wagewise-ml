package io.github.matgalv2.employment.model

sealed trait FormOfEmployment
object FormOfEmployment {
  final case object Contractor extends FormOfEmployment
  final case object Employee extends FormOfEmployment
}
