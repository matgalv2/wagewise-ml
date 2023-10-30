package io.github.matgalv2.employment.model

trait CompanyType
object CompanyType {
  final case object SoftwareHouse extends CompanyType
  final case object PublicInstitution extends CompanyType
  final case object Corporation extends CompanyType
  final case object Company extends CompanyType
  final case object BigTech extends CompanyType
  final case object Startup extends CompanyType
  final case object Other extends CompanyType
}
