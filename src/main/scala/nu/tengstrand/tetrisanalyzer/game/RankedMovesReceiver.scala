package nu.tengstrand.tetrisanalyzer.game

import nu.tengstrand.tetrisanalyzer.move.MoveEquity

trait RankedMovesReceiver {
  def setRankedMoves(rankedMoves: List[MoveEquity])
}