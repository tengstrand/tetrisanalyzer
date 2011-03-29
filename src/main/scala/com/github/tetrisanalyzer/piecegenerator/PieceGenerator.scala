package com.github.tetrisanalyzer.piecegenerator

import com.github.tetrisanalyzer.piece.Piece

trait PieceGenerator {
  def nextPieceNumber: Int
  def nextPiece = {
    val pieceNumber = nextPieceNumber
    require(pieceNumber >= 1 && pieceNumber <= 7, "Piece number must be in the range 1..7, found: " + pieceNumber)
    Piece(pieceNumber)
  }
}