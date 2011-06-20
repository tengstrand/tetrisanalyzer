package nu.tengstrand.tetrisanalyzer.game

class GameEventDelegate(playerEventReceiver: PlayerEventReceiver, gameInfoReceiver: GameInfoReceiver) extends GameEventReceiver {
  def setPosition(positionModel: PositionModel) { playerEventReceiver.setPosition(positionModel) }
  def isReadyToReceivePosition: Boolean = { playerEventReceiver.isReadyToReceivePosition }

  def setPaused(paused: Boolean) { gameInfoReceiver.setPaused(paused) }
  def setSeed(seed: Long) { gameInfoReceiver.setSeed(seed) }
  def setSliding(enabled: Boolean) { gameInfoReceiver.setSliding(enabled) }
  def setBoardSize(width: Int, height: Int) { gameInfoReceiver.setBoardSize(width, height) }
  def setNumberOfPieces(pieces: Long) { gameInfoReceiver.setNumberOfPieces(pieces) }
  def setTotalNumberOfPieces(pieces: Long) { gameInfoReceiver.setTotalNumberOfPieces(pieces) }
  def setNumberOfClearedRows(rows: Long) { gameInfoReceiver.setNumberOfClearedRows(rows)}
  def setTotalNumberOfClearedRows(rows: Long) { gameInfoReceiver.setTotalNumberOfClearedRows(rows)}
  def setTimePassed(seconds: Double) { gameInfoReceiver.setTimePassed(seconds) }
  def updateGui() { gameInfoReceiver.updateGui() }
  def setNumberOfGamesAndRowsInLastGame(games: Long, rows: Long, totalClearedRows: Long, minRows: Long, maxRows: Long) {
    gameInfoReceiver.setNumberOfGamesAndRowsInLastGame(games, rows, totalClearedRows, minRows, maxRows)
  }
}