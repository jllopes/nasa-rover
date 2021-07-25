package io.github.jllopes.nasarover.services

import cats.effect.IO
import cats.effect.concurrent.Ref
import io.github.jllopes.nasarover.domain.{Coordinates, North, Position}
import munit.CatsEffectSuite

class NavigationServicesSpec extends CatsEffectSuite {
  private def initializeServices[F[_]]: NavigationServices[IO] = {
    val initialPosition = Position(Coordinates(0, 0), North)
    val positionRef = Ref[IO].of(initialPosition).unsafeRunSync()
    NavigationServices.impl[IO](positionRef)
  }

  test("Navigate endpoint keeps state for multiple commands") {
    val navigationServices = initializeServices
    val forwardMove = List('F')
    val expectedPosition = Position(Coordinates(0, 2), North)
    val test = navigationServices.navigate(forwardMove).flatMap(_ =>
      navigationServices.navigate(forwardMove)
    )

    assertIO(test.map(_.toString), expectedPosition.toString)
  }
}