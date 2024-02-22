package io.github.matgalv2.wagewise.http

import zio.config._
import zio.config.magnolia._
import com.comcast.ip4s.{ Host, Port }

final case class Config(http: Config.Http, mongo: Config.Mongo, keycloak: Config.Keycloak, spark: Config.Spark)

object Config {
  implicit val descriptor: ConfigDescriptor[Config] =
    (ConfigDescriptor.nested("http")(Http.httpDescriptor) |@| ConfigDescriptor.nested("mongo")(
      mongoDescriptor
    ) |@| ConfigDescriptor.nested("keycloak")(keycloakDescriptor) |@| ConfigDescriptor.nested("spark")(
      sparkDescriptor
    ))(Config.apply, Config.unapply)

  final case class Http(port: Port, host: Host)

  object Http {
    implicit val hostDescriptor: ConfigDescriptor[Host] =
      ConfigDescriptor.string.transformOrFailLeft(Host.fromString(_).toRight("No host found"))(_.toString())

    implicit val portDescriptor: ConfigDescriptor[Port] =
      ConfigDescriptor.int.transformOrFailLeft(Port.fromInt(_).toRight("No port found"))(_.value)

    implicit val httpDescriptor: ConfigDescriptor[Http] = (ConfigDescriptor.nested("port")(
      portDescriptor
    ) |@| ConfigDescriptor.nested("host")(hostDescriptor))(Http.apply, Http.unapply)
  }

  final case class Mongo(url: String, database: String, user: String, password: String)

  implicit val mongoDescriptor: ConfigDescriptor[Mongo] = DeriveConfigDescriptor.descriptor

  final case class Keycloak(url: String, realm: String, clientID: String, clientSecret: String)

  implicit val keycloakDescriptor: ConfigDescriptor[Keycloak] = DeriveConfigDescriptor.descriptor

  final case class Spark(masterURL: String, employmentsDatasetPath: String)

  implicit val sparkDescriptor: ConfigDescriptor[Spark] = DeriveConfigDescriptor.descriptor
}
