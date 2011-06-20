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

  def animateClearedRows(position: Position, pieceY: Int, pieceHeight: Int, gameEventReceiver: GameEventReceiver) {
    val copyPosition = Position(position)
    val clearedRows = for {y <- pieceY until pieceY + pieceHeight if (position.isCompleteRow(y))} yield y

    clearRows(clearedRows, position, gameEventReceiver)
    showClearedRows(clearedRows, position, copyPosition, if (fastAnimation) 20 else 100, gameEventReceiver)
    clearRows(clearedRows, position, gameEventReceiver)
    showClearedRows(clearedRows, position, copyPosition, if (fastAnimation) 10 else 50, gameEventReceiver)
  }

  private def clearRows(clearedRows: IndexedSeq[Int], position: Position, gameEventReceiver: GameEventReceiver) {
    clearedRows.foreach(y => position.clearRow(y))
    gameEventReceiver.setPosition(position)
    Thread.sleep(100)
  }

  private def showClearedRows(clearedRows: IndexedSeq[Int], position: Position, copyPosition: Position, pauseMilisec: Int, gameEventReceiver: GameEventReceiver) {
    clearedRows.foreach(y => position.copyRow(y, copyPosition))
    gameEventReceiver.setPosition(position)
    Thread.sleep(pauseMilisec)
  }
}