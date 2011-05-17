package com.github.tetrisanalyzer.game

import com.github.tetrisanalyzer.settings.GameSettings
import com.github.tetrisanalyzer.piece.{Point, PieceAny, Piece, PieceEmpty}

object Wall {
  val Number: Byte = 9.toByte
  val Character = '#'
  val Left = 6
  val Right = 2
  val Bottom = 2
}

object Dot {
  val Characters = Array[Char] (
    Piece(0).character,
    Piece(1).character,
    Piece(2).character,
    Piece(3).character,
    Piece(4).character,
    Piece(5).character,
    Piece(6).character,
    Piece(7).character,
    Piece(8).character,
    Wall.Character
  )
}

/**
 * A board with walls and next piece(es), used by the GUI.
 */
class Position(settings: GameSettings, currentPiece: Piece, val width: Int = 10, val height: Int = 20) {
  private val playfield = Array.tabulate[Byte](height + Wall.Bottom, width + Wall.Left + Wall.Right) (
    (y,x) => if (x < Wall.Left || x >= width + Wall.Left || y >= height) Wall.Number else PieceEmpty.Number
  )
  private val currentMove = settings.pieceStartMove(width, currentPiece)

  private def setCurrentPiece { currentPiece.shape(currentMove.rotation).dots.foreach(dot => set(dot, currentPiece.number)) }
  private def set(dot: Point, number: Byte) { playfield(dot.y)(dot.x + currentMove.x + Wall.Left) = number }

  setCurrentPiece

  override def toString = {
    var result = "";
    var newLine = "";
    for (y <- 0 until height + Wall.Bottom) {
      result += newLine;
      for (x <- 0 until width + Wall.Left + Wall.Right) {
        result += Dot.Characters(playfield(y)(x))
      }
      newLine = "\n"
    }
    result
  }
}
