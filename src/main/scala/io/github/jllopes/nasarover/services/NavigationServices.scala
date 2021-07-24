package io.github.jllopes.nasarover.services

import cats.effect.Effect
import cats.effect.concurrent.Ref
import cats.implicits._
import io.github.jllopes.nasarover.domain._

trait NavigationServices[F[_]] {
  def navigate(commandList: List[Char]): F[Position]
}

object NavigationServices {
  implicit def apply[F[_]](implicit ev: NavigationServices[F]): NavigationServices[F] = ev

  def impl[F[_] : Effect](positionRef: F[Ref[F, Position]]): NavigationServices[F] = new NavigationServices[F] {
    def navigate(commandList: List[Char]): F[Position] = {
      Position(Coordinates(0,1), North).pure[F]
    }
  }
}
