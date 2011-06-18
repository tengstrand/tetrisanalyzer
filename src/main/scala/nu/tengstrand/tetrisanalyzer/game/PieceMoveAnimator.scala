package nu.tengstrand.tetrisanalyzer.game

import nu.tengstrand.tetrisanalyzer.piecemove.PieceMove

class PieceMoveAnimator(gameEventReceiver: GameEventReceiver) {
  var fastAnimation = false

  def animateMove(position: Position, startPieceMove: PieceMove, pieceMove: PieceMove) {
    startPieceMove.prepareAnimatedPath
    startPieceMove.calculateAnimatedPath(null, 0, 0)

    val animatedPosition = Position(position)

    var step = pieceMove
    var steps = List.empty[PieceMove]
    while (step != null) {
      steps = step :: steps
      step = step.animatedPath
    }

    steps.foreach(step => {
      val animatedPosition = Position(position)
      animatedPosition.setPiece(step.piece, step.move)
      gameEventReceiver.setPosition(animatedPosition)
      Thread.sleep(if (fastAnimation) 5 else 20)
    })
  }

  def animateClearedLines(position: Position, pieceY: Int, pieceHeight: Int, gameEventReceiver: GameEventReceiver) {
    val copyPosition = Position(position)
    val clearedLines = for {y <- pieceY until pieceY + pieceHeight if (position.isCompleteLine(y))} yield y

    clearLines(clearedLines, position, gameEventReceiver)
    showClearedLines(clearedLines, position, copyPosition, if (fastAnimation) 20 else 100, gameEventReceiver)
    clearLines(clearedLines, position, gameEventReceiver)
    showClearedLines(clearedLines, position, copyPosition, if (fastAnimation) 10 else 50, gameEventReceiver)
  }

  private def clearLines(clearedLines: IndexedSeq[Int], position: Position, gameEventReceiver: GameEventReceiver) {
    clearedLines.foreach(y => position.clearLine(y))
    gameEventReceiver.setPosition(position)
    Thread.sleep(100)
  }

  private def showClearedLines(clearedLines: IndexedSeq[Int], position: Position, copyPosition: Position, pauseMilisec: Int, gameEventReceiver: GameEventReceiver) {
    clearedLines.foreach(y => position.copyLine(y, copyPosition))
    gameEventReceiver.setPosition(position)
    Thread.sleep(pauseMilisec)
  }
}