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

}