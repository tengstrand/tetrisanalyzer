package com.github.tetrisanalyzer.game

import actors.Actor
import com.github.tetrisanalyzer.move.Move
import com.github.tetrisanalyzer.piece.Piece
import com.github.tetrisanalyzer.settings.GameSettings

case class SetPiece(piece: Piece, move: Move)

class GameEventReceiver(position: Position, settings: GameSettings, playerEventReceiver: PlayerEventReceiver) extends Actor {

  override def act() {
    loop {
      react {
        case SetPiece(piece: Piece, move: Move) =>
          if (playerEventReceiver.readyToReceivePosition) {
            val positionWithStartPiece = Position(position)
            positionWithStartPiece.setStartPieceIfFree(piece, settings)
            playerEventReceiver ! positionWithStartPiece
          }
          position.setPiece(piece, move)
          position.clearLines(move.y, piece.height(move.rotation))
      }
    }
  }
}