package io.github.matgalv2.wagewise.http.aplication

import http.generated.definitions.PredictedSalary
import io.github.matgalv2.wagewise.ml.PredictorError.WrongValues
import io.github.matgalv2.wagewise.ml.Processing.ProgrammerFeatures
import io.github.matgalv2.wagewise.ml.{ PredictorError, RandomForestRegression }
import zio.{ IO, ZIO }

import java.util.UUID
import scala.util.Try

object SalaryPredictorService {

  def predictSalary(programmerId: UUID, programmer: ProgrammerFeatures): IO[PredictorError, PredictedSalary] = {
    val workHoursInMonth = 160
    ZIO
      .fromTry(Try(RandomForestRegression.makePrediction(Seq(programmer))))
//    val k: IO[SthWentWrong.type, Double] = ZIO
//      .succeed(RandomForestRegression.makePrediction(Seq(programmer)))
//    k
      .map(rate_per_hour =>
        PredictedSalary(programmerId, salaryMonthly = rate_per_hour * workHoursInMonth, ratePerHour = rate_per_hour)
      )
      .mapError(_ => WrongValues)
  }
}
