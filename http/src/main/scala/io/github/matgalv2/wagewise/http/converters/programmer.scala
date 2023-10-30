package io.github.matgalv2.wagewise.http.converters

import http.generated.definitions.Programmer
import io.github.matgalv2.wagewise.ml.Processing.ProgrammerFeatures

object programmer {
  implicit class EmploymentModelOps(private val programmer: Programmer) extends AnyVal {
    def toModelFeatures: ProgrammerFeatures = (
      programmer.dateOfEmployment.toString,
      programmer.age,
      programmer.sex.value,
      programmer.country,
      programmer.experienceYearsIt,
      programmer.languages,
      programmer.speciality.value,
      programmer.coreProgrammingLanguage.value,
      programmer.academicTitle.value,
      programmer.educationTowardsIt.toString,
      0.0f,
      programmer.companyCountry,
      programmer.companyType.value,
      programmer.workForm.value,
      programmer.teamSize,
      programmer.teamType.value,
      programmer.formOfEmployment.value,
      programmer.fullTime,
      programmer.paidDaysOff,
      programmer.insurance,
      programmer.trainingSessions
    )
  }

}
