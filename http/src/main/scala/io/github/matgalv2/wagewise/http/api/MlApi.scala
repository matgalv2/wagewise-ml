package io.github.matgalv2.wagewise.http.api

import http.generated.definitions.{ ErrorResponse, PredictedSalaries, Programmer, Programmers }
import http.generated.ml.{ MlHandler, PredictSalaryResponse }
import io.github.matgalv2.wagewise.http.aplication.SalaryPredictorService
import io.github.matgalv2.wagewise.http.converters.programmer.EmploymentModelOps
import io.github.matgalv2.wagewise.http.repository.repository.Repository
import zio.{ Has, RIO, UIO, ZIO }

class MlApi extends MlHandler[RIO[Repository, *]] {
  override def predictSalary(respond: PredictSalaryResponse.type)(body: Programmers): UIO[PredictSalaryResponse] =
    ZIO
      .foreach(body.value)(programmer =>
        SalaryPredictorService.predictSalary(programmer.id, programmer.toModelFeatures)
      )
      .map(predictedSalaries => respond.Ok(PredictedSalaries(predictedSalaries)))
      .mapError(_ => respond.BadRequest(ErrorResponse(Vector(body.value.mkString("Programmer(", ", ", ")")))))
      .merge
}
object MlApi {
  type Environment = Has[Nothing]
}
