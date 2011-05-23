package com.github.tetrisanalyzer.game

import actors.Actor
import com.github.tetrisanalyzer.move.Move
import com.github.tetrisanalyzer.piece.Piece
import com.github.tetrisanalyzer.settings.GameSettings

case class SetPiece(piece: Piece, move: Move)

class GameEventReceiver(position: Position, settings: GameSettings, playerEventReceiver: PlayerEventReceiver) extends Actor {
  var sendBoard: Boolean = true

  override def act() {
    loop {
      react {
        case SetPiece(piece: Piece, move: Move) =>
          if (playerEventReceiver.readyToReceivePosition) {
            val positionCopy = Position(position)
            positionCopy.setStartPieceIfFree(piece, settings)
            playerEventReceiver ! positionCopy
          }
          position.setPiece(piece, move)
          position.clearLines(move.y, piece.height(move.rotation))
      }
    }
  }
}