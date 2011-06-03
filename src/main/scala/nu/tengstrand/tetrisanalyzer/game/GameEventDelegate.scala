package nu.tengstrand.tetrisanalyzer.game

class GameEventDelegate(playerEventReceiver: PlayerEventReceiver, gameInfoReceiver: GameInfoReceiver) extends GameEventReceiver {
  def setPosition(positionModel: PositionModel) { playerEventReceiver.setPosition(positionModel) }
  def isReadyToReceivePosition: Boolean = { playerEventReceiver.isReadyToReceivePosition }

  def setPaused(paused: Boolean) { gameInfoReceiver.setPaused(paused) }
  def setNumberOfPieces(pieces: Long) { gameInfoReceiver.setNumberOfPieces(pieces) }
  def setTotalNumberOfPieces(pieces: Long) { gameInfoReceiver.setTotalNumberOfPieces(pieces) }
  def setNumberOfClearedLines(lines: Long) { gameInfoReceiver.setNumberOfClearedLines(lines)}
  def setTotalNumberOfClearedLines(lines: Long) { gameInfoReceiver.setTotalNumberOfClearedLines(lines)}
  def setNumberOfGamesAndLinesInLastGame(games: Int, lines: Long) { gameInfoReceiver.setNumberOfGamesAndLinesInLastGame(games, lines) }
  def setTimePassed(seconds: Double) { gameInfoReceiver.setTimePassed(seconds) }
  def updateGui() { gameInfoReceiver.updateGui() }
}