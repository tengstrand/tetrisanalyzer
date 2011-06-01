package com.github.tetrisanalyzer.game

class GameEventDelegator(playerEventReceiver: PlayerEventReceiver, gameInfoReceiver: GameInfoReceiver) extends GameEventReceiver {
  def setPosition(positionModel: PositionModel) { playerEventReceiver.setPosition(positionModel) }
  def isReadyToReceivePosition: Boolean = { playerEventReceiver.isReadyToReceivePosition }

  def setPaused(paused: Boolean) { gameInfoReceiver.setPaused(paused) }
  def setNumberOfPieces(pieces: Long) { gameInfoReceiver.setNumberOfPieces(pieces) }
  def setTotalClearedLines(lines: Long) { gameInfoReceiver.setTotalClearedLines(lines)}
  def setTimePassed(seconds: Double) { gameInfoReceiver.setTimePassed(seconds) }
}