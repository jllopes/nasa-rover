package io.github.jllopes.nasarover

import cats.effect.{ExitCode, IO, IOApp}

object Main extends IOApp {
  def run(args: List[String]) =
    NasaRoverServer.stream[IO].compile.drain.as(ExitCode.Success)
}
