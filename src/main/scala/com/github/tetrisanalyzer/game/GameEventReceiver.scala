package com.github.tetrisanalyzer.game

import actors.Actor
import com.github.tetrisanalyzer.move.Move
import com.github.tetrisanalyzer.piece.Piece

case class SetPiece(piece: Piece, move: Move)

class GameEventReceiver(position: Position, playerEventReceiver: PlayerEventReceiver) extends Actor {
  var sendBoard: Boolean = true

  override def act() {
    loop {
      react {
        case SetPiece(piece: Piece, move: Move) =>
          position.setPiece(piece, move)
          position.clearLines(move.y, piece.height(move.rotation))
          playerEventReceiver ! Position(position)
      }
    }
  }
}