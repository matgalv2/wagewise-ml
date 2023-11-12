package io.github.matgalv2.wagewise.ml.converters

import io.github.matgalv2.employment.model.Employment
import io.github.matgalv2.wagewise.ml.model.Prediction

object employment {
  implicit class EmploymentModelOps(private val employment: Employment) extends AnyVal {
    def toModelFeatures: Prediction.EarningsPrediction.ModelFeatures = (
      employment.dateOfEmployment.toString,
      employment.age,
      employment.sex.value,
      employment.country,
      employment.experienceYearsIt,
      employment.languages,
      employment.speciality.value,
      employment.coreProgrammingLanguage.value,
      employment.academicTitle.value,
      employment.educationTowardsIt.toString,
      0.0f,
      employment.companyCountry,
      employment.companyType.value,
      employment.workForm.value,
      employment.teamSize,
      employment.teamType.value,
      employment.formOfEmployment.value,
      employment.fullTime,
      employment.paidDaysOff,
      employment.insurance,
      employment.trainingSessions
    )
  }
}
