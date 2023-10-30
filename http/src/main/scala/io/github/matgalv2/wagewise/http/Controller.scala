package io.github.matgalv2.wagewise.http

import http.generated.definitions.Programmer
import http.generated.ml.MlResource
import io.github.matgalv2.wagewise.http.api.MlApi
import io.github.matgalv2.wagewise.http.httpServer.httpServer
import io.github.matgalv2.wagewise.http.repository.repository
import zio.{ RIO, Ref, Runtime, ZEnv, ZIO }

import java.time.LocalDate
import java.util.UUID

object Controller {

  /** An effect which, when executed, gives a StoreResource (capable of transforming a StoreHandler into something
    * bindable)
    */
  val makeMlResource: RIO[repository.Repository, MlResource[RIO[repository.Repository, *]]] = {
    import zio.interop.catz._
    ZIO.runtime[repository.Repository].map { implicit r: Runtime[repository.Repository] =>
      new MlResource[RIO[repository.Repository, *]]
    }
  }

  /** Our HTTP server implementation, utilizing the Repository Layer
    */
  val handler = new MlApi()

  val programmer = Programmer(
    id                      = UUID.fromString("95fe99be-29c7-4a7c-880f-fb31d92ce3a8"),
    dateOfEmployment        = LocalDate.of(2000, 3, 29),
    age                     = 23,
    sex                     = Programmer.Sex.F,
    country                 = "Poland",
    experienceYearsIt       = 0,
    languages               = "Polish,English",
    speciality              = Programmer.Speciality.ComputerScientist,
    coreProgrammingLanguage = Programmer.CoreProgrammingLanguage.Python,
    academicTitle           = Programmer.AcademicTitle.Bachelor,
    educationTowardsIt      = false,
    companyCountry          = "Poland",
    companyType             = Programmer.CompanyType.SoftwareHouse,
    workForm                = Programmer.WorkForm.Stationary,
    teamSize                = 10,
    teamType                = Programmer.TeamType.Local,
    formOfEmployment        = Programmer.FormOfEmployment.Employee,
    fullTime                = true,
    paidDaysOff             = true,
    insurance               = true,
    trainingSessions        = true
  )

  val initialProgrammers = Vector(programmer)
  val programmersLayer   = Ref.make(initialProgrammers).toManaged_.toLayer

  val combineRoutes = {
    import org.http4s.implicits._
    import zio.interop.catz._

    for {
      mlResource <- makeMlResource
    } yield mlResource.routes(handler).orNotFound
  }

  val inMemoryLayer = programmersLayer >>> repository.Repository.inMemory

  val prog =
    for {
      combinedRoutes <- combineRoutes
      binding        <- httpServer.bindServer(combinedRoutes)
      res            <- binding.use(_ => ZIO.never)
    } yield res

  val inMemoryProg: ZIO[zio.ZEnv with httpServer.HttpServer, Throwable, Nothing] =
    prog
      .provideSomeLayer[ZEnv with httpServer.HttpServer](inMemoryLayer)
}
