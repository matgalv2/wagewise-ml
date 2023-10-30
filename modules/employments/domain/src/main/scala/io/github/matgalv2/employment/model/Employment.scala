package io.github.matgalv2.employment.model

final case class Employment(
  dateOfEmployment: java.time.LocalDate,
  age: BigInt,
  sex: Sex,
  country: String,
  experienceYearsIt: BigInt,
  languages: String,
  speciality: Speciality,
  coreProgrammingLanguage: CoreProgrammingLanguage,
  academicTitle: Option[AcademicTitle] = None,
  educationTowardsIt: Boolean,
  companyCountry: String,
  companyType: CompanyType,
  workForm: WorkForm,
  teamSize: BigInt,
  teamType: Option[TeamType] = None,
  formOfEmployment: FormOfEmployment,
  fullTime: Boolean,
  paidDaysOff: Boolean,
  insurance: Boolean,
  trainingSessions: Boolean
)
