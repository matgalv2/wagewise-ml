package io.github.matgalv2.wagewise.http.converters

import http.generated.definitions.PredictedSalary
import io.github.matgalv2.wagewise.ml.model.Prediction.EarningsPrediction

import java.util.UUID

object prediction {

  implicit class PredictionToApiOps(private val earningsPrediction: EarningsPrediction) extends AnyVal {
    def toApi(programmerId: UUID): PredictedSalary = PredictedSalary(programmerId, earningsPrediction.salary, earningsPrediction.ratePerHour)
  }
}
