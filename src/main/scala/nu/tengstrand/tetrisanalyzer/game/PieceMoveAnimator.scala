package nu.tengstrand.tetrisanalyzer.game

import nu.tengstrand.tetrisanalyzer.piecemove.PieceMove
import startpiece.StartPiece

class PieceMoveAnimator(speed: Speed, gameEventReceiver: GameEventReceiver) {
  var quit = false
  var fastAnimation = false

  def isMaxSpeed = speed.isMaxSpeed
  def getSpeedIndex = speed.getSpeedIndex
  def increaseSpeed() { speed.increaseSpeed() }
  def decreaseSpeed() { speed.decreaseSpeed() }
  def toggleMaxSpeed() { speed.toggleMaxSpeed() }

  def continueDoStep(paused: Boolean) = fastAnimation || (!paused && !speed.isMaxSpeed)

  def animateMove(position: Position, startPiece: StartPiece, startPieceMove: PieceMove, pieceMove: PieceMove) {
    startPieceMove.prepareAnimatedPath()
    startPieceMove.calculateAnimatedPath(null, 0, 0)

    var step = pieceMove
    var steps = List.empty[PieceMove]
    while (step != null) {
      steps = step :: steps
      step = step.animatedPath
    }

    steps.foreach(step => {
      if (!quit) {
        val animatedPosition = Position(position)
        animatedPosition.setNextPieceIfShown(startPiece)
        animatedPosition.setPiece(step.piece, step.move)
        gameEventReceiver.setPosition(animatedPosition)
        Thread.sleep(speed.pieceDelay(fastAnimation))
      }
    })
  }

  def animateClearedRows(position: Position, pieceY: Int, pieceHeight: Int, gameEventReceiver: GameEventReceiver) {
    val copyPosition = Position(position)
    val clearedRows = for {y <- pieceY until pieceY + pieceHeight if (position.isCompleteRow(y))} yield y

    clearRows(clearedRows, position, gameEventReceiver)
    showClearedRows(clearedRows, position, copyPosition, speed.clearRowDelay(fastAnimation), gameEventReceiver)
    clearRows(clearedRows, position, gameEventReceiver)
    showClearedRows(clearedRows, position, copyPosition, speed.clearRowDelay(fastAnimation) / 2, gameEventReceiver)
  }

  private def clearRows(clearedRows: IndexedSeq[Int], position: Position, gameEventReceiver: GameEventReceiver) {
    clearedRows.foreach(y => position.clearRow(y))
    gameEventReceiver.setPosition(position)
    Thread.sleep(speed.clearRowDelay(fastAnimation))
  }

  private def showClearedRows(clearedRows: IndexedSeq[Int], position: Position, copyPosition: Position, pauseMilisec: Int, gameEventReceiver: GameEventReceiver) {
    clearedRows.foreach(y => position.copyRow(y, copyPosition))
    gameEventReceiver.setPosition(position)
    Thread.sleep(pauseMilisec)
  }
}