package io.github.matgalv2.wagewise.http

import zio.config._
import zio.config.magnolia._

final case class Config(http: Config.Http, mongo: Config.Mongo, keycloak: Config.Keycloak)

object Config {
  implicit val descriptor: ConfigDescriptor[Config] =
    (ConfigDescriptor.nested("http")(httpDescriptor) |@| ConfigDescriptor.nested("mongo")(
      mongoDescriptor
    ) |@| ConfigDescriptor.nested("keycloak")(keycloakDescriptor))(Config.apply, Config.unapply)

//  final case class Api(key: String)
//
//  object Api {
//    implicit val apiDescriptor: ConfigDescriptor[Api] = DeriveConfigDescriptor.descriptor
//  }

  final case class Http(port: String, host: String)

  implicit val httpDescriptor: ConfigDescriptor[Http] = DeriveConfigDescriptor.descriptor

  final case class Mongo(url: String, database: String)

  implicit val mongoDescriptor: ConfigDescriptor[Mongo] = DeriveConfigDescriptor.descriptor

  final case class Keycloak(clientSecret: String)

  implicit val keycloakDescriptor: ConfigDescriptor[Keycloak] = DeriveConfigDescriptor.descriptor

}
