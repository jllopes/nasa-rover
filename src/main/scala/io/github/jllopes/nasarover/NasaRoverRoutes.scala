package io.github.jllopes.nasarover

import cats.effect.Sync
import cats.implicits._
import io.github.jllopes.nasarover.controllers.Navigation
import io.github.jllopes.nasarover.domain.Command
import io.github.jllopes.nasarover.domain.Position._
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.circe.toMessageSynax
import org.http4s.dsl.Http4sDsl

object NasaRoverRoutes {

  def navigationRoutes[F[_]: Sync](navigation: Navigation[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case req @ POST -> Root / "navigate" =>
        req.decodeJson[Command].flatMap { command =>
          Ok(navigation.navigate(command.value))
        }
    }
  }
}