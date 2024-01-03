package io.github.matgalv2.wagewise.http.application

import io.github.matgalv2.wagewise.ml.model.Prediction.EarningsPrediction
import io.github.matgalv2.wagewise.ml.predictor.{ PredictorError, SalaryPredictor }
import zio.{ Has, ZIO }

object SalaryPredictorService {

  def predictSalary(
    programmers: Seq[SalaryPredictor.ProgrammerFeatures]
  ): ZIO[Has[SalaryPredictor], PredictorError, Seq[EarningsPrediction]] = {
    val workHoursInMonth = 160
    SalaryPredictor
      .predict(programmers)
      .map(_.map(rate_per_hour => EarningsPrediction(rate_per_hour, rate_per_hour * workHoursInMonth)))
  }
}
