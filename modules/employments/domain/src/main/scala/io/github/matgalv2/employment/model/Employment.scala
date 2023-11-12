package io.github.matgalv2.employment.model

final case class Employment(
  dateOfEmployment: java.time.LocalDate,
  age: Int,
  rate_per_hour: Double,
  sex: Sex,
  country: String,
  experienceYearsIt: Int,
  languages: String,
  speciality: Speciality,
  coreProgrammingLanguage: CoreProgrammingLanguage,
  academicTitle: AcademicTitle,
  educationTowardsIt: Boolean,
  companyCountry: String,
  companyType: CompanyType,
  workForm: WorkForm,
  teamSize: Int,
  teamType: TeamType,
  formOfEmployment: FormOfEmployment,
  fullTime: Boolean,
  paidDaysOff: Boolean,
  insurance: Boolean,
  trainingSessions: Boolean
)
