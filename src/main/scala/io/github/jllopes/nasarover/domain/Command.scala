package io.github.jllopes.nasarover.domain

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class Command(command: String)

object Command {
  implicit val commandDecoder: Decoder[Command] = deriveDecoder
}