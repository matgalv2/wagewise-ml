package io.github.matgalv2.wagewise.http.aplication


import io.github.matgalv2.wagewise.ml.RandomForestRegression
import io.github.matgalv2.wagewise.ml.model.Prediction.EarningsPrediction
import io.github.matgalv2.wagewise.ml.predictor.PredictorError.WrongValues
import io.github.matgalv2.wagewise.ml.predictor.{PredictorError, SalaryPredictor}
import zio.{Has, IO, ZIO}

import scala.util.Try

object SalaryPredictorService {

  def predictSalary(programmers: Seq[SalaryPredictor.ProgrammerFeatures]): IO[PredictorError, Seq[EarningsPrediction]] = {
    val workHoursInMonth = 160
    ZIO
      .fromTry(Try(RandomForestRegression.predict(programmers)))
      .map(_.map(rate_per_hour => EarningsPrediction(rate_per_hour, rate_per_hour * workHoursInMonth)))
      .mapError(_ => WrongValues)
  }
}
