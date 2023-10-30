package io.github.matgalv2.employment.model

sealed trait Speciality
object Speciality {
  final case object SystemsAnalyst extends Speciality
  final case object Backend extends Speciality
  final case object SoftwareEngineer extends Speciality
  final case object TechLead extends Speciality
  final case object DbAdministrator extends Speciality
  final case object DataQualityManager extends Speciality
  final case object ItSecuritySpecialist extends Speciality
  final case object ComputerScientist extends Speciality
  final case object WebAdministrator extends Speciality
  final case object Other extends Speciality
  final case object ApplicationsEngineer extends Speciality
  final case object DataScientist extends Speciality
  final case object CloudSystemEngineer extends Speciality
  final case object Frontend extends Speciality
}
