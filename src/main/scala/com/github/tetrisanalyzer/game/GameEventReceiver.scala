package com.github.tetrisanalyzer.game

import actors.Actor
import com.github.tetrisanalyzer.move.Move
import com.github.tetrisanalyzer.piece.Piece
import com.github.tetrisanalyzer.settings.GameSettings

case class SetPieceMessage(piece: Piece, move: Move, clearedLines: Long)

class GameEventReceiver(position: Position,
                        settings: GameSettings,
                        playerEventReceiver: PlayerEventReceiver,
                        gameInfoReceiver: GameInfoReceiver) extends Actor {

  private var pieces = 0L
  private var totalClearedLines = 0L

  override def act() {
    loop {
      react {
        // TODO: Add number of cleared lines to the message and only clear lines if needed.
        case SetPieceMessage(piece: Piece, move: Move, clearedLines: Long) =>
          if (playerEventReceiver.isReadyToReceivePosition) {
            val positionWithStartPiece = Position(position)
            positionWithStartPiece.setStartPieceIfFree(piece, settings)
            playerEventReceiver.setPosition(positionWithStartPiece)
            gameInfoReceiver.setPieces(pieces)
            gameInfoReceiver.setTotalClearedLines(totalClearedLines)
          }
          pieces += 1
          totalClearedLines += clearedLines
          position.setPiece(piece, move)
          position.clearLines(move.y, piece.height(move.rotation))
      }
    }
  }
}