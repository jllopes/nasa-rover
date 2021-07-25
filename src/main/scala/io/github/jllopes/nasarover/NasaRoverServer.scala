package io.github.jllopes.nasarover

import cats.effect.concurrent.Ref
import cats.effect.{ConcurrentEffect, ExitCode, Timer}

import io.github.jllopes.nasarover.controllers.Navigation
import io.github.jllopes.nasarover.domain.{Coordinates, North, Position}
import io.github.jllopes.nasarover.services.NavigationServices
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger
import cats.implicits._

import scala.concurrent.ExecutionContext.global

object NasaRoverServer {
  def stream[F[_] : ConcurrentEffect](implicit T: Timer[F]): F[ExitCode] = {
    for {
      positionRef <- Ref.of(Position(Coordinates(0, 0), North))
      routes = NasaRoverRoutes.navigationRoutes[F](initializeComponents(positionRef))
      httpApp = routes.orNotFound
      finalHttpApp = Logger.httpApp(true, true)(httpApp)
      exitCode <- BlazeServerBuilder[F](global)
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(finalHttpApp)
        .serve.compile.lastOrError
    } yield exitCode
  }

  private def initializeComponents[F[_] : ConcurrentEffect](positionRef: Ref[F, Position]): Navigation[F] = {
    val navigationServicesAlg = NavigationServices.impl[F](positionRef)
    Navigation.impl[F](navigationServicesAlg)
  }
}
