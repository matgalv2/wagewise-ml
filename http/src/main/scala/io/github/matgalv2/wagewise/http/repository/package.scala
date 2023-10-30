package io.github.matgalv2.wagewise.http.repository

import http.generated.definitions.{ Programmer, Programmers }
import zio._

package object repository {
  type Repository = Has[Repository.Service]

  /** Accessor proxies
    */
  def getProgrammers = ZIO.accessM[Repository](_.get.getProgrammers)

  object Repository {
    trait Service {
      def getProgrammers: UIO[Programmers]
    }

    /** Simple in-memory implementation
      */
    val inMemory = ZLayer.fromService[Ref[Vector[Programmer]], Service](programmers =>
      new Service {
        def getProgrammers = programmers.get.map(Programmers.apply)

      }
    )
  }
}
