package nu.tengstrand.tetrisanalyzer.game

trait GameInfoReceiver {
  def setPaused(paused: Boolean)
  def setSeed(seed: Long)
  def setBoardSize(width: Int, height: Int)
  def setNumberOfPieces(pieces: Long)
  def setTotalNumberOfPieces(pieces: Long)
  def setNumberOfGamesAndLinesInLastGame(games: Int, lines: Long)
  def setNumberOfClearedLines(lines: Long)
  def setTotalNumberOfClearedLines(lines: Long)
  def setTimePassed(seconds: Double)
  def updateGui()
}