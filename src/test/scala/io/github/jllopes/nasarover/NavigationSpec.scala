package io.github.jllopes.nasarover

import cats.effect.IO
import cats.effect.concurrent.Ref
import io.github.jllopes.nasarover.controllers.Navigation
import io.github.jllopes.nasarover.domain.{Coordinates, North, Position}
import io.github.jllopes.nasarover.services.NavigationServices
import munit.CatsEffectSuite
import org.http4s._
import org.http4s.implicits._

class NavigationSpec extends CatsEffectSuite {

  test("Navigate endpoint returns 0,1,N after moving forward once") {
    val body = """{"command": "F"}"""
    val call = Request[IO](Method.POST, uri"/navigate").withEntity(body)
    val positionRef = Ref[IO].of(Position(Coordinates(0,0), North)) // TODO: Refactor to avoid repetition
    val navigationServices = NavigationServices.impl[IO](positionRef)
    val navigation = Navigation.impl[IO](navigationServices)
    val test = NasaRoverRoutes.navigationRoutes(navigation).orNotFound(call)
    assertIO(test.flatMap(_.as[String]), """"0,1,N"""")
  }

  test("Navigate endpoint returns 1,0,E after rotating right and moving forward once") {
    val body = """{"command": "RF"}"""
    val call = Request[IO](Method.POST, uri"/navigate").withEntity(body)
    val positionRef = Ref[IO].of(Position(Coordinates(0,0), North))
    val navigationServices = NavigationServices.impl[IO](positionRef)
    val navigation = Navigation.impl[IO](navigationServices)
    val test = NasaRoverRoutes.navigationRoutes(navigation).orNotFound(call)
    assertIO(test.flatMap(_.as[String]), """"1,0,E"""")
  }
}