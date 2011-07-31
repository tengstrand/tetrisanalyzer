package nu.tengstrand.tetrisanalyzer.piecegenerator

import nu.tengstrand.tetrisanalyzer.piece.{PieceO, Piece}

/**
 * Generates a predictable sequence of pieces.
 */
class PredictablePieceGenerator(pieceSequence: List[Piece]) extends PieceGenerator {
  private val pieceSequenceIterator = pieceSequence.iterator

  private var number = nextPieceNumber

  private def nextPieceNumber = {
    if (pieceSequenceIterator.hasNext)
      pieceSequenceIterator.next.number
    else
      new PieceO().number
  }

  def pieceNumber = number

  def prepareNext() { number = nextPieceNumber }
}
