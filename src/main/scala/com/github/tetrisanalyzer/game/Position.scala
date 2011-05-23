package com.github.tetrisanalyzer.game

import com.github.tetrisanalyzer.settings.GameSettings
import com.github.tetrisanalyzer.piece.{Point, Piece, PieceEmpty}
import com.github.tetrisanalyzer.move.Move

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

object Position {
  def apply(width: Int = 10, height: Int = 20): Position = {
    val playfield = Array.tabulate[Byte](height + Wall.Bottom, width + Wall.Left + Wall.Right) (
      (y,x) => if (x < Wall.Left || x >= width + Wall.Left || y >= height) Wall.Number else PieceEmpty.Number
    )
    new Position(width, height, playfield)
  }

  // Copy constructor
  def apply(position: Position): Position = {
    new Position(position.width, position.height, position.playfieldCopy)
  }
}

/**
 * A board with walls and next piece(es), used by the GUI.
 */
class Position(val width: Int, val height: Int, playfield: Array[Array[Byte]]) {
  private def setDot(dot: Point, move: Move, number: Byte) { playfield(dot.y + move.y)(dot.x + move.x + Wall.Left) = number }

  def playfieldWidth = width + Wall.Left + Wall.Right
  def playfieldHeight = height + Wall.Bottom
  def colorValue(x: Int, y: Int): Int = playfield(y)(x).toInt

  def setStartPieceIfFree(piece: Piece, settings: GameSettings) {
    val startMove = settings.pieceStartMove(width, piece)
    val isFree = piece.shape(startMove.rotation).dots.foldLeft(0) {(sum,dot) => sum + emptyOrOccupied (dot.x, dot.y)} == 0
    if (isFree)
      setPiece(piece, startMove)
  }

  def setPiece(piece: Piece, move: Move) {
    piece.shape(move.rotation).dots.foreach(dot => setDot(dot, move, piece.number))
  }

  private def isCompleteLine(y: Int): Boolean = {
    (Wall.Left until Wall.Left + width).foldLeft(0) { (sum,x) => sum + emptyOrOccupied(x,y) } == width
  }

  private def emptyOrOccupied(x: Int, y: Int) = if (playfield(y)(x) == PieceEmpty.Number) 0 else 1

  private def clearLine(y: Int) {
    (Wall.Left until Wall.Left + width).foreach(x => playfield(y)(x) = PieceEmpty.Number)
  }

  private def copyLine(fromY: Int, toY: Int) {
    (Wall.Left until Wall.Left + width).foreach(x => playfield(toY)(x) = playfield(fromY)(x))
  }

  /**
   * Clears completed lines and returns number of cleared lines.
   * This method is called after a piece has been placed on the board.
   *   pieceY: the y position of the piece.
   *   pieceHeight: height of the piece.
   */
  def clearLines(pieceY: Int, pieceHeight: Int): Int = {
    var clearedLines = 0
    var y1 = pieceY + pieceHeight

    // Find first line to clear
    do {
      y1 -= 1
      if (isCompleteLine(y1))
        clearedLines += 1
    } while (clearedLines == 0 && y1 > pieceY)

    // Clear lines
    if (clearedLines > 0) {
      var y2 = y1

      while (y1 >= 0) {
        y2 -= 1
        while (y2 >= pieceY && isCompleteLine(y2)) {
          clearedLines += 1
          y2 -= 1
        }
        if (y2 >= 0)
          copyLine(y2, y1)
        else
          clearLine(y1)

        y1 -= 1
      }
    }
    clearedLines
  }

  def playfieldCopy = {
    val newPlayfield: Array[Array[Byte]] = Array.ofDim[Byte](height + Wall.Bottom, width + Wall.Left + Wall.Right)

    for (i <- 0 until playfield.length)
      playfield(i).copyToArray(newPlayfield(i))

    newPlayfield
  }

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
