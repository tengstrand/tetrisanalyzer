package nu.tengstrand.tetrisanalyzer.game

import java.awt.Dimension
import nu.tengstrand.tetrisanalyzer.piece.Piece
import nu.tengstrand.tetrisanalyzer.settings.GameSettings

class GameStatistics(boardSize: Dimension, seed: Long, gameEventReceiver: GameEventReceiver) {
  private var moves = 0L
  private var movesTotal = 0L
  private var clearedLines = 0L
  private var clearedLinesTotal = 0L
  private var games = 0L
  private var totalClearedLines = 0L
  private var minClearedLines = 0L
  private var maxClearedLines = 0L

  def addMove() {
    moves += 1
    movesTotal += 1
  }

  def addClearedLines(clearedLines: Int) {
    this.clearedLines += clearedLines
    clearedLinesTotal += clearedLines
  }

  def hasPassedHundredPieces = movesTotal % 100 == 0

  def newGame() {
    games += 1

    totalClearedLines += clearedLines

    if (minClearedLines == 0 || clearedLines < minClearedLines)
      minClearedLines = clearedLines

    if (maxClearedLines == 0 || clearedLines > maxClearedLines)
      maxClearedLines = clearedLines

    gameEventReceiver.setNumberOfGamesAndLinesInLastGame(games, clearedLines, totalClearedLines, minClearedLines, maxClearedLines)

    moves = 0
    clearedLines = 0
  }

  def updateAll() {
    gameEventReceiver.setSeed(seed)
    gameEventReceiver.setBoardSize(boardSize.width, boardSize.height)
    updateGameInfo()
    gameEventReceiver.setNumberOfGamesAndLinesInLastGame(games, clearedLines, totalClearedLines, minClearedLines, maxClearedLines)
  }

  def updatePosition(position: Position, piece: Piece, settings: GameSettings) {
    val positionWithStartPiece = Position(position)
    positionWithStartPiece.setStartPieceIfFree(piece, settings)
    gameEventReceiver.setPosition(positionWithStartPiece)
  }

  def updateGameInfo() {
    gameEventReceiver.setNumberOfPieces(moves)
    gameEventReceiver.setTotalNumberOfPieces(movesTotal)
    gameEventReceiver.setNumberOfClearedLines(clearedLines)
    gameEventReceiver.setTotalNumberOfClearedLines(clearedLinesTotal)
    gameEventReceiver.updateGui()
  }
}