package io.github.matgalv2.employment.model

sealed abstract class CompanyType(val value: String)
object CompanyType {
  final case object SoftwareHouse extends CompanyType("Software house")
  final case object PublicInstitution extends CompanyType("Public institution")
  final case object Corporation extends CompanyType("Corporation")
  final case object Company extends CompanyType("Company")
  final case object BigTech extends CompanyType("Big tech")
  final case object Startup extends CompanyType("Startup")
  final case object Other extends CompanyType("Other")
}