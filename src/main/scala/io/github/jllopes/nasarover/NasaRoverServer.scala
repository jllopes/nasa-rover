package io.github.jllopes.nasarover

import cats.effect.concurrent.Ref
import cats.effect.{ConcurrentEffect, Timer}
import fs2.Stream
import io.github.jllopes.nasarover.controllers.Navigation
import io.github.jllopes.nasarover.domain.{Coordinates, North, Position}
import io.github.jllopes.nasarover.services.NavigationServices
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger

import scala.concurrent.ExecutionContext.global

object NasaRoverServer {
  def stream[F[_] : ConcurrentEffect](implicit T: Timer[F]): Stream[F, Nothing] = {
    val routes = NasaRoverRoutes.navigationRoutes[F](initializeComponents)
    val httpApp = routes.orNotFound
    val finalHttpApp = Logger.httpApp(true, true)(httpApp)
    BlazeServerBuilder[F](global)
      .bindHttp(8080, "0.0.0.0")
      .withHttpApp(finalHttpApp)
      .serve
  }.drain

  private def initializeComponents[F[_] : ConcurrentEffect]: Navigation[F] = {
    val positionRef = Ref.of(Position(Coordinates(0, 0), North))
    val navigationServicesAlg = NavigationServices.impl[F](positionRef)
    Navigation.impl[F](navigationServicesAlg)
  }
}
