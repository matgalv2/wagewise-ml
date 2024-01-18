package io.github.matgalv2.wagewise.http.middleware

import cats.data.{ Kleisli, OptionT }
import cats.effect.Async
import org.http4s.util.CaseInsensitiveString
import org.http4s.{ HttpRoutes, Request, Response, Status }
import scalaj.http.Http

import scala.io.Source
import scala.util.{ Failure, Success, Try }

object AuthorizationMiddleware {
  private val bearerToken = CaseInsensitiveString("bearer_token")
  private val keycloakURL = Try(System.getenv("KEYCLOAK_URL")).getOrElse("No keycloak URL found")
  private val realm       = Try(System.getenv("KEYCLOAK_REALM")).getOrElse("No keycloak realm found")
  private val clientId    = Try(System.getenv("KEYCLOAK_CLIENT_ID")).getOrElse("No keycloak clientId found")

  private def clientSecret = {
    val file = Try(Source.fromFile(System.getenv("KEYCLOAK_CONF_PATH")))
    file match {
      case Success(value)     => value.getLines().toList.headOption.getOrElse("No keycloak client secret found")
      case Failure(exception) => exception.toString
    }
  }

  /*
  private def clientSecret = {
    val config: Config = ConfigFactory.load()
    Try(config.getString("keycloak.clientSecret")).getOrElse("No keycloak client secret found")
  }
   */

  private def verifyToken(bearer_token: String) = {
    val tokenVerificationUrl = f"$keycloakURL/auth/realms/$realm/protocol/openid-connect/token/introspect"

    val patternActive = """.+"active":true.+""".r
    val response = Try(
      Http(tokenVerificationUrl)
        .postForm(Seq("client_id" -> clientId, "client_secret" -> clientSecret, "token" -> bearer_token))
        .asString
    )

    response match {
      case Success(_) => true
      case Failure(_) => false
    }
  }

  def authorize[F[_]: Async](service: HttpRoutes[F]): HttpRoutes[F] = Kleisli { req: Request[F] =>
    val unauthorized = OptionT.pure(Response[F](Status.Unauthorized))

    req.headers.get(bearerToken) match {
      case Some(token) if verifyToken(token.value) => service(req)
      case _                                       => unauthorized
    }
  }
}
