package com.github.tetrisanalyzer.piecegenerator

import com.github.tetrisanalyzer.piece.{PieceO, Piece}

/**
 * Generates a predictable sequence of pieces.
 */
class PredictablePieceGenerator(pieceSequence: List[Piece]) extends PieceGenerator {
  private val pieceSequenceIterator = pieceSequence.iterator

  def nextPieceNumber = {
    if (pieceSequenceIterator.hasNext)
      pieceSequenceIterator.next.number
    else
      new PieceO().number
  }
}