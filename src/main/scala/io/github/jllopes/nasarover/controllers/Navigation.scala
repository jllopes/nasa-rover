package io.github.jllopes.nasarover.controllers

import cats.Applicative
import io.github.jllopes.nasarover.domain.Position
import io.github.jllopes.nasarover.services.NavigationServices

trait Navigation[F[_]] {
  def navigate(commands: String): F[Position]
}

object Navigation {
  implicit def apply[F[_]](implicit ev: Navigation[F]): Navigation[F] = ev

  def impl[F[_]: Applicative](navigationServices: NavigationServices[F]): Navigation[F] = new Navigation[F] {
    override def navigate(commands: String): F[Position] = {
      // TODO: Filter invalid instructions
      navigationServices.navigate(commands.toList)
    }
  }
}