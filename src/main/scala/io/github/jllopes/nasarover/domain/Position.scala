package io.github.jllopes.nasarover.domain

import io.circe.{Encoder, Json}

case class Position(coordinates: Coordinates, direction: Direction)

object Position {
  implicit val positionEncoder: Encoder[Position] = new Encoder[Position] {
    final def apply(p: Position): Json = {
      val direction = p.direction match {
        case North => "N"
        case West => "W"
        case South => "S"
        case East => "E"
      }
      val position = s"${p.coordinates.x},${p.coordinates.y},$direction"
      Json.fromString(position)
    }
  }
}
