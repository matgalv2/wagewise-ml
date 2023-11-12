package io.github.matgalv2.wagewise.http.api

import http.generated.definitions.{ErrorResponse, PredictedSalaries, Programmer, Programmers}
import http.generated.ml.{MlHandler, PredictSalaryResponse}
import io.github.matgalv2.wagewise.http.aplication.SalaryPredictorService
import io.github.matgalv2.wagewise.http.converters.prediction.PredictionToApiOps
import io.github.matgalv2.wagewise.http.converters.programmer.EmploymentModelOps
import io.github.matgalv2.wagewise.http.repository.repository.Repository
import zio.{Has, RIO, UIO}

class MlApi extends MlHandler[RIO[Repository, *]] {
  override def predictSalary(respond: PredictSalaryResponse.type)(body: Programmers): UIO[PredictSalaryResponse] =
    SalaryPredictorService
      .predictSalary(body.value.map(_.toModelFeatures))
      .map(predictedSalaries => respond.Ok(PredictedSalaries(predictedSalaries.zip(body.value).map(earnings_ids => earnings_ids._1.toApi(earnings_ids._2.id)).toVector)))
      .mapError(_ => respond.BadRequest(ErrorResponse(Vector(body.value.mkString("Programmer(", ", ", ")")))))
      .merge
}
object MlApi {
  type Environment = Has[Nothing]
}
