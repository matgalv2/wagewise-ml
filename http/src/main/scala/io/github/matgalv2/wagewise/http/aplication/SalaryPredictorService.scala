package io.github.matgalv2.wagewise.http.aplication

import io.github.matgalv2.wagewise.ml.Prediction.EarningsPrediction
import io.github.matgalv2.wagewise.ml.PredictorError.WrongValues
import io.github.matgalv2.wagewise.ml.Processing.ProgrammerFeatures
import io.github.matgalv2.wagewise.ml.{PredictorError, RandomForestRegression}
import zio.{IO, ZIO}

import scala.util.Try

object SalaryPredictorService {

  def predictSalary(programmers: Seq[ProgrammerFeatures]): IO[PredictorError, Seq[EarningsPrediction]] = {
    val workHoursInMonth = 160
    ZIO
      .fromTry(Try(RandomForestRegression.makePrediction(programmers)))
      .map(_.map(rate_per_hour => EarningsPrediction(rate_per_hour, rate_per_hour * workHoursInMonth)))
      .mapError(_ => WrongValues)
  }
}
