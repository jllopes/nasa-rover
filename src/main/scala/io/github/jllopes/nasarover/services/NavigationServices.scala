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
      for {
        position <- positionRef
        _ <- commandList.traverse {
          case 'F' => move(isForwardMove = true)(position)
          case 'B' => move(isForwardMove = false)(position)
          case 'R' => rotate(isRightTurn = true)(position)
          case 'L' => rotate(isRightTurn = false)(position)
        }
        result <- position.get
      } yield {
        result
      }
    }
  }

  private def move[F[_] : Effect](isForwardMove: Boolean)(positionRef: Ref[F, Position]): F[Unit] = {
    positionRef.update {
      currentPosition =>
        val (xMovement, yMovement) = currentPosition.direction match {
          case North => if (isForwardMove) (0, 1) else (0, -1)
          case South => if (isForwardMove) (0, -1) else (0, 1)
          case East => if (isForwardMove) (1, 0) else (-1, 0)
          case West => if (isForwardMove) (-1, 0) else (1, 0)
        }
        val updatedCoordinates = currentPosition.coordinates.copy(currentPosition.coordinates.x + xMovement, currentPosition.coordinates.y + yMovement)
        currentPosition.copy(coordinates = updatedCoordinates)
    }
  }

  private def rotate[F[_] : Effect](isRightTurn: Boolean)(positionRef: Ref[F, Position]): F[Unit] = {
    positionRef.update {
      currentPosition =>
        val newDirection = currentPosition.direction match {
          case North => if (isRightTurn) East else West
          case South => if (isRightTurn) West else East
          case East => if (isRightTurn) South else North
          case West => if (isRightTurn) North else South
        }
        currentPosition.copy(direction = newDirection)
    }
  }
}
