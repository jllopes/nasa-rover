package io.github.jllopes.nasarover.domain

sealed trait Error

case class OutOfBounds(position: Position) extends Error