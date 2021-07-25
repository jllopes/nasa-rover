package io.github.jllopes.nasarover.controllers

import cats.effect.IO
import cats.effect.concurrent.Ref
import io.github.jllopes.nasarover.NasaRoverRoutes
import io.github.jllopes.nasarover.domain.{Coordinates, North, Position}
import io.github.jllopes.nasarover.services.NavigationServices
import munit.CatsEffectSuite
import org.http4s._
import org.http4s.implicits._

class NavigationSpec extends CatsEffectSuite {
  private def initializeRoutes: HttpRoutes[IO] = {
    val initialPosition = Position(Coordinates(0,0), North)
    val positionRef = Ref[IO].of(initialPosition)
    val navigationServices = NavigationServices.impl[IO](positionRef)
    val navigation = Navigation.impl[IO](navigationServices)
    NasaRoverRoutes.navigationRoutes(navigation)
  }

  test("Navigate endpoint returns 0,1,N after moving forward once") {
    val body = """{"command": "F"}"""
    val call = Request[IO](Method.POST, uri"/navigate").withEntity(body)
    val test = initializeRoutes.orNotFound(call)
    assertIO(test.flatMap(_.as[String]), """"0,1,N"""")
  }

  test("Navigate endpoint returns 1,0,E after rotating right and moving forward once") {
    val body = """{"command": "RF"}"""
    val call = Request[IO](Method.POST, uri"/navigate").withEntity(body)
    val test = initializeRoutes.orNotFound(call)
    assertIO(test.flatMap(_.as[String]), """"1,0,E"""")
  }

  test("Navigate endpoint returns last possible position 0,0,W after receiving out of bounds command") {
    val body = """{"command": "LF"}"""
    val call = Request[IO](Method.POST, uri"/navigate").withEntity(body)
    val test = initializeRoutes.orNotFound(call)
    assertIO(test.flatMap(_.as[String]), """"0,0,W"""")
  }
}