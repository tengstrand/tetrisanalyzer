package nu.tengstrand.tetrisanalyzer.gui.rankedmove

import nu.tengstrand.tetrisanalyzer.move.MoveEquity

class RankedMoves(val moves: List[MoveEquity]) extends AlignRight {

  def headerAsText = {
    var x = alignRight("x", maxX.toString.length()) + " "
    var y = if (hasDuplicatedVX) alignRight("y", maxY.toString.length()) + "  " else " "

    "    v " + x + y + "Depth 0"
  }

  def size = moves.size

  // TODL: Implement!
  def hasDuplicatedVX = true

  def maxX = moves.map(_.pieceMove.move.x).max + 1

  def maxY = moves.map(_.pieceMove.move.y).max

  def maxEquity = moves.map(_.equity).max
}