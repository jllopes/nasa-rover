package io.github.jllopes.nasarover.services

import cats.effect.Effect
import cats.effect.concurrent.Ref
import cats.implicits._
import com.typesafe.scalalogging.LazyLogging
import io.github.jllopes.nasarover.domain._

trait NavigationServices[F[_]] {
  def navigate(commandList: List[Char]): F[Position]
}

object NavigationServices extends LazyLogging {
  implicit def apply[F[_]](implicit ev: NavigationServices[F]): NavigationServices[F] = ev

  def impl[F[_] : Effect](positionRef: F[Ref[F, Position]]): NavigationServices[F] = new NavigationServices[F] {
    def navigate(commandList: List[Char]): F[Position] = {
      for {
        position <- positionRef
        _ <- position.update {
          currentPosition =>
            commandList.headOption.fold(currentPosition) {
              nextCommand =>
                executeCommands(nextCommand, commandList.drop(1), currentPosition)
            }
        }
        result <- position.get
      } yield {
        result
      }
    }
  }

  private def executeCommands[F[_] : Effect](command: Char, commandList: List[Char], position: Position): Position = {
    val moveResult = command match {
      case 'F' => moveReturning(isForwardMove = true, position)
      case 'B' => moveReturning(isForwardMove = false, position)
      case 'R' => rotateReturning(isRightTurn = true, position)
      case 'L' => rotateReturning(isRightTurn = false, position)
    }
    moveResult match {
      case Left(error) =>
        error match {
          case e: OutOfBounds =>
            logger.error(s"Encountered obstacle at ${e.position}")
            position
        }
      case Right(resultPosition) =>
        commandList.headOption.fold(resultPosition) {
          nextCommand =>
            executeCommands(nextCommand, commandList.drop(1), resultPosition)
        }
    }
  }

  private def moveReturning[F[_] : Effect](isForwardMove: Boolean, position: Position): Either[Error, Position] = {
    val (xMovement, yMovement) = position.direction match {
      case North => if (isForwardMove) (0, 1) else (0, -1)
      case South => if (isForwardMove) (0, -1) else (0, 1)
      case East => if (isForwardMove) (1, 0) else (-1, 0)
      case West => if (isForwardMove) (-1, 0) else (1, 0)
    }
    updateCoordinates(position, xMovement, yMovement)
  }

  private def rotateReturning[F[_] : Effect](isRightTurn: Boolean, position: Position): Either[Error, Position] = {
    val newDirection = position.direction match {
      case North => if (isRightTurn) East else West
      case South => if (isRightTurn) West else East
      case East => if (isRightTurn) South else North
      case West => if (isRightTurn) North else South
    }
    position.copy(direction = newDirection).asRight
  }

  private def updateCoordinates(position: Position, xMovement: Int, yMovement: Int): Either[Error, Position] = {
    val newX = position.coordinates.x + xMovement
    val newY = position.coordinates.y + yMovement
    val resultingPosition = position.copy(coordinates = position.coordinates.copy(newX, newY))
    if (newX < 0 || newX > 100 || newY < 0 || newY > 100) {
      OutOfBounds(resultingPosition).asLeft
    } else {
      resultingPosition.asRight
    }
  }
}
