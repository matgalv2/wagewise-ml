package io.github.matgalv2.wagewise.http.converters

import http.generated.definitions.Programmer
import http.generated.definitions.Programmer._
import io.github.matgalv2.employment.model.Employment
import io.github.matgalv2.employment._

object programmer {
  implicit class ProgrammerModelOps(private val programmer: Programmer) extends AnyVal {
    def toModel: Employment = Employment(
      dateOfEmployment = programmer.dateOfEmployment,
      age = programmer.age,
      rate_per_hour = 0.0f,
      sex = programmer.sex.toModel,
      country = programmer.country,
      experienceYearsIt = programmer.experienceYearsIt,
      languages = programmer.languages,
      speciality = programmer.speciality.toModel,
      coreProgrammingLanguage = programmer.coreProgrammingLanguage.toModel,
      academicTitle = programmer.academicTitle.toModel,
      educationTowardsIt = programmer.educationTowardsIt,
      companyCountry = programmer.companyCountry,
      companyType = programmer.companyType.toModel,
      workForm = programmer.workForm.toModel,
      teamSize = programmer.teamSize,
      teamType = programmer.teamType.toModel,
      formOfEmployment = programmer.formOfEmployment.toModel,
      fullTime = programmer.fullTime,
      paidDaysOff = programmer.paidDaysOff,
      insurance = programmer.insurance,
      trainingSessions = programmer.trainingSessions
    )
  }
  implicit class SexModelOps(private val sex: Sex) extends AnyVal {
    def toModel: model.Sex = sex match {
      case Sex.members.F => model.Sex.F
      case Sex.members.M => model.Sex.M
    }
  }

  implicit class SpecialityModelOps(private val speciality: Speciality) extends AnyVal {
    def toModel: model.Speciality = speciality match {
      case Speciality.members.SystemsAnalyst => model.Speciality.SystemsAnalyst
      case Speciality.members.Backend => model.Speciality.Backend
      case Speciality.members.SoftwareEngineer => model.Speciality.SoftwareEngineer
      case Speciality.members.TechLead => model.Speciality.TechLead
      case Speciality.members.DbAdministrator => model.Speciality.DbAdministrator
      case Speciality.members.DataQualityManager => model.Speciality.DataQualityManager
      case Speciality.members.ItSecuritySpecialist => model.Speciality.ItSecuritySpecialist
      case Speciality.members.ComputerScientist => model.Speciality.ComputerScientist
      case Speciality.members.WebAdministrator => model.Speciality.WebAdministrator
      case Speciality.members.Other => model.Speciality.Other
      case Speciality.members.ApplicationsEngineer => model.Speciality.ApplicationsEngineer
      case Speciality.members.DataScientist => model.Speciality.DataScientist
      case Speciality.members.CloudSystemEngineer => model.Speciality.CloudSystemEngineer
      case Speciality.members.Frontend => model.Speciality.Frontend
    }
  }

  implicit class CoreProgrammingLanguageModelOps(private val coreProgrammingLanguage: CoreProgrammingLanguage) extends AnyVal {
    def toModel: model.CoreProgrammingLanguage = coreProgrammingLanguage match {
      case CoreProgrammingLanguage.members.Swift => model.CoreProgrammingLanguage.Swift
      case CoreProgrammingLanguage.members.Python => model.CoreProgrammingLanguage.Python
      case CoreProgrammingLanguage.members.Java => model.CoreProgrammingLanguage.Java
      case CoreProgrammingLanguage.members.JavaScript => model.CoreProgrammingLanguage.JavaScript
      case CoreProgrammingLanguage.members.R => model.CoreProgrammingLanguage.R
      case CoreProgrammingLanguage.members.ObjectiveC => model.CoreProgrammingLanguage.ObjectiveC
      case CoreProgrammingLanguage.members.Kotlin => model.CoreProgrammingLanguage.Kotlin
      case CoreProgrammingLanguage.members.Php => model.CoreProgrammingLanguage.Php
      case CoreProgrammingLanguage.members.Cobol => model.CoreProgrammingLanguage.Cobol
      case CoreProgrammingLanguage.members.Go => model.CoreProgrammingLanguage.Go
      case CoreProgrammingLanguage.members.Other => model.CoreProgrammingLanguage.Other
      case CoreProgrammingLanguage.members.Ruby => model.CoreProgrammingLanguage.Ruby
    }
  }

  implicit class AcademicTitleModelOps(private val academicTitle: AcademicTitle) extends AnyVal {
    def toModel: model.AcademicTitle = academicTitle match {
      case AcademicTitle.members.Licence => model.AcademicTitle.Licence
      case AcademicTitle.members.NoDegree => model.AcademicTitle.NoDegree
      case AcademicTitle.members.Master => model.AcademicTitle.Master
      case AcademicTitle.members.Bachelor => model.AcademicTitle.Bachelor
      case AcademicTitle.members.Doctorate => model.AcademicTitle.Doctorate
    }
  }

  implicit class ModelOps(private val companyType: CompanyType) extends AnyVal {
    def toModel: model.CompanyType = companyType match {
      case CompanyType.members.SoftwareHouse => model.CompanyType.SoftwareHouse
      case CompanyType.members.PublicInstitution => model.CompanyType.PublicInstitution
      case CompanyType.members.Corporation => model.CompanyType.Corporation
      case CompanyType.members.Company => model.CompanyType.Company
      case CompanyType.members.BigTech => model.CompanyType.BigTech
      case CompanyType.members.Startup => model.CompanyType.Startup
      case CompanyType.members.Other => model.CompanyType.Other
    }
  }

  implicit class WorkFormModelOps(private val workForm: WorkForm) extends AnyVal {
    def toModel: model.WorkForm = workForm match {
      case WorkForm.members.Remote => model.WorkForm.Remote
      case WorkForm.members.Hybrid => model.WorkForm.Hybrid
      case WorkForm.members.Stationary => model.WorkForm.Stationary
    }
  }

  implicit class TeamTypeModelOps(private val teamType: TeamType) extends AnyVal {
    def toModel: model.TeamType = teamType match {
      case TeamType.members.International => model.TeamType.International
      case TeamType.members.Local => model.TeamType.Local
    }
  }

  implicit class FormOfEmploymentModelOps(private val formOfEmployment: FormOfEmployment) extends AnyVal {
    def toModel: model.FormOfEmployment = formOfEmployment match {
      case FormOfEmployment.members.Contractor => model.FormOfEmployment.Contractor
      case FormOfEmployment.members.Employee => model.FormOfEmployment.Employee
    }
  }


}
