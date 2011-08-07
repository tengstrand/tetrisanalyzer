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
  private var minClearedrows = Long.MaxValue
  private var maxClearedrows = 0L

  def addMove() {
    moves += 1
    movesTotal += 1
  }

  def addClearedRows(clearedRows: Int) {
    this.clearedRows += clearedRows
    clearedRowsTotal += clearedRows
  }

  def hasPassedHundredPieces = movesTotal % 100 == 0

  def newGame() {
    games += 1

    totalClearedRows += clearedRows

    if (clearedRows < minClearedrows)
      minClearedrows = clearedRows

    if (clearedRows > maxClearedrows)
      maxClearedrows = clearedRows

    gameEventReceiver.setNumberOfGamesAndRowsInLastGame(games, clearedRows, totalClearedRows, minClearedrows, maxClearedrows)

    moves = 0
    clearedRows = 0
  }

  def updateAll() {
    gameEventReceiver.setBoardSize(boardSize.width, boardSize.height)
    updateGameInfo()
    gameEventReceiver.setNumberOfGamesAndRowsInLastGame(games, clearedRows, totalClearedRows, minClearedrows, maxClearedrows)
  }

  def setStartPieceAndSelectedMoveIfSelectedOnPosition(position: Position, startPiece: StartPiece, selectedRankedMove: Move, settings: GameSettings) {
    if (gameEventReceiver.isReadyToReceivePosition) {
      position.setNextPieceIfShown(startPiece)
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