package nu.tengstrand.tetrisanalyzer.game

import java.awt.Dimension
import nu.tengstrand.tetrisanalyzer.settings.GameSettings
import nu.tengstrand.tetrisanalyzer.move.Move
import startpiece.StartPiece

class GameStatistics(boardSize: Dimension, gameEventReceiver: GameEventReceiver) {
  private var moves = 0L
  private var movesTotal = 0L
  private var clearedRows = 0L
  private var clearedRowsTotal = 0L
  private var games = 0L
  private var totalClearedRows = 0L
  private var minClearedRows = Long.MaxValue
  private var maxClearedRows = 0L

  def addMove() {
    moves += 1
    movesTotal += 1
  }

  def addClearedRows(clearedRows: Int) {
    this.clearedRows += clearedRows
    clearedRowsTotal += clearedRows
  }

  def isTimeToUpdateGui(showNextPiece: Boolean) = {
    val updateEveryXMove = if (showNextPiece) 10 else 100
    movesTotal % updateEveryXMove == 0
  }

  def reset() {
    moves = 0L
    movesTotal = 0L
    clearedRows = 0L
    clearedRowsTotal = 0L
    games = 0L
    totalClearedRows = 0L
    minClearedRows = Long.MaxValue
    maxClearedRows = 0L

    updateGameInfo()
    gameEventReceiver.setNumberOfGamesAndRowsInLastGame(0L, 0L, 0L, 0L, 0L)
  }

  def newGame() {
    games += 1

    totalClearedRows += clearedRows

    if (clearedRows < minClearedRows)
      minClearedRows = clearedRows

    if (clearedRows > maxClearedRows)
      maxClearedRows = clearedRows

    gameEventReceiver.setNumberOfGamesAndRowsInLastGame(games, clearedRows, totalClearedRows, minClearedRows, maxClearedRows)

    moves = 0
    clearedRows = 0
  }

  def updateAll() {
    gameEventReceiver.setBoardSize(boardSize.width, boardSize.height)
    updateGameInfo()
    gameEventReceiver.setNumberOfGamesAndRowsInLastGame(games, clearedRows, totalClearedRows, minClearedRows, maxClearedRows)
  }

  def setStartPieceAndSelectedMove(position: Position, startPiece: StartPiece, selectedRankedMove: Move, settings: GameSettings) {
    if (gameEventReceiver.isReadyToReceivePosition) {
      val positionWithStartPiece = Position(position)
      positionWithStartPiece.setStartPieceIfFree(startPiece, settings)
      if (selectedRankedMove != null)
        positionWithStartPiece.setSelectedMove(startPiece.firstPiece, selectedRankedMove)
      gameEventReceiver.setPosition(positionWithStartPiece)
    }
  }

  def updateGameInfo() {
    gameEventReceiver.setNumberOfPieces(moves)
    gameEventReceiver.setTotalNumberOfPieces(movesTotal)
    gameEventReceiver.setNumberOfClearedRows(clearedRows)
    gameEventReceiver.setTotalNumberOfClearedRows(clearedRowsTotal)
  }
}