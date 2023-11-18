package io.github.matgalv2.wagewise.http.api

import http.generated.definitions.{
  ErrorResponse,
  PredictedSalaries,
  PredictedSalary,
  Programmers
}
import http.generated.ml.{MlHandler, PredictSalaryResponse}
import io.github.matgalv2.wagewise.http.DummyService
import io.github.matgalv2.wagewise.http.aplication.SalaryPredictorService
import io.github.matgalv2.wagewise.http.converters.prediction.PredictionToApiOps
import io.github.matgalv2.wagewise.http.converters.programmer.ProgrammerModelOps
import io.github.matgalv2.wagewise.ml.predictor.SalaryPredictor
import zio.logging.Logging
import zio.{Has, RIO, URIO}

class MlApi extends MlHandler[RIO[MlApi.Environment, *]] {
  override def predictSalary(
      respond: PredictSalaryResponse.type
  )(body: Programmers): URIO[MlApi.Environment, PredictSalaryResponse] = {

    SalaryPredictorService
      .predictSalary(body.value.map(_.toModel))
      .map(
        predictedSalaries =>
          respond.Ok(
            PredictedSalaries(
              predictedSalaries
                .zip(body.value)
                .map(earnings_ids => earnings_ids._1.toApi(earnings_ids._2.id))
                .toVector
            )
        ))
      .orElseFail(
        respond.BadRequest(
          ErrorResponse(Vector(body.value.mkString("Programmer(", ", ", ")")))
        )
      )
      .merge
  }
}

object MlApi {
  type Environment = Has[SalaryPredictor] with Has[DummyService] with Logging
}
