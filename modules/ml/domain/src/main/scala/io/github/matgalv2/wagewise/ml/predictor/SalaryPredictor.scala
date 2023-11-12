package io.github.matgalv2.wagewise.ml.predictor

import io.github.matgalv2.employment.model.Employment
import zio.{Has, UIO, ZIO}

trait SalaryPredictor {
  def predict(programmers: Seq[SalaryPredictor.ProgrammerFeatures]): UIO[Seq[Double]]
}
object SalaryPredictor{
  type ProgrammerFeatures = Employment
  def predict(programmers: Seq[SalaryPredictor.ProgrammerFeatures]): ZIO[Has[SalaryPredictor], Throwable, Seq[Double]] =
    ZIO.serviceWith[SalaryPredictor](_.predict(programmers))
}
