package nu.tengstrand.tetrisanalyzer.game

trait GameInfoReceiver {
  def setPaused(paused: Boolean)
  def setSeed(seed: Long)
  def setSliding(enabled: Boolean)
  def setBoardSize(width: Int, height: Int)
  def setNumberOfPieces(pieces: Long)
  def setTotalNumberOfPieces(pieces: Long)
  def setNumberOfGamesAndLinesInLastGame(games: Long, lines: Long, totalClearedLines: Long, minLines: Long, maxLines: Long)
  def setNumberOfClearedLines(lines: Long)
  def setTotalNumberOfClearedLines(lines: Long)
  def setTimePassed(seconds: Double)
  def updateGui()
}