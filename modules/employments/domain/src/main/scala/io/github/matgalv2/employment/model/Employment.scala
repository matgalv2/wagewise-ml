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
  academicTitle: AcademicTitle,
  educationTowardsIt: Boolean,
  companyCountry: String,
  companyType: CompanyType,
  workForm: WorkForm,
  teamSize: BigInt,
  teamType: TeamType,
  formOfEmployment: FormOfEmployment,
  fullTime: Boolean,
  paidDaysOff: Boolean,
  insurance: Boolean,
  trainingSessions: Boolean
)
