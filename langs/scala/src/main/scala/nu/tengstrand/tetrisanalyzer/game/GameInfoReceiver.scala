package nu.tengstrand.tetrisanalyzer.game

trait GameInfoReceiver {
  def setPaused(paused: Boolean): Unit
  def setSeed(seed: Long): Unit
  def setSliding(enabled: Boolean): Unit
  def setBoardSize(width: Int, height: Int): Unit
  def setNumberOfPieces(pieces: Long): Unit
  def setTotalNumberOfPieces(pieces: Long): Unit
  def setNumberOfGamesAndRowsInLastGame(games: Long, rows: Long, totalClearedRows: Long, minRows: Long, maxRows: Long): Unit
  def setNumberOfClearedRows(rows: Long): Unit
  def setTotalNumberOfClearedRows(rows: Long): Unit
  def setTimePassed(seconds: Double): Unit
  def setSpeed(speedIndex: Int, isMaxSpeed: Boolean): Unit
}