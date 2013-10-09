package nu.tengstrand.tetrisanalyzer.game

import nu.tengstrand.tetrisanalyzer.gui.rankedmove.RankedMoves

trait RankedMovesReceiver {
  def setRankedMoves(rankedMoves: RankedMoves)
  def selectNextRankedMove()
}