package io.github.matgalv2.employment.model

sealed abstract class Speciality(val value: String)

object Speciality {
  final case object SystemsAnalyst extends Speciality("Systems analyst")
  final case object Backend extends Speciality("Backend")
  final case object SoftwareEngineer extends Speciality("Software Engineer")
  final case object TechLead extends Speciality("Tech lead")
  final case object DbAdministrator extends Speciality("DB Administrator")
  final case object DataQualityManager extends Speciality("Data quality manager")
  final case object ItSecuritySpecialist extends Speciality("IT Security specialist")
  final case object ComputerScientist extends Speciality("Computer scientist")
  final case object WebAdministrator extends Speciality("Web administrator")
  final case object Other extends Speciality("Other")
  final case object ApplicationsEngineer extends Speciality("Applications engineer")
  final case object DataScientist extends Speciality("Data scientist")
  final case object CloudSystemEngineer extends Speciality("Cloud system engineer")
  final case object Frontend extends Speciality("Frontend")
}
