package io.github.matgalv2.employment.model

sealed abstract class FormOfEmployment(val value: String)

object FormOfEmployment {
  final case object Contractor extends FormOfEmployment("contractor")
  final case object Employee extends FormOfEmployment("employee")
}
