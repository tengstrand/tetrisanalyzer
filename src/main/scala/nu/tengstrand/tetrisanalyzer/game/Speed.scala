package nu.tengstrand.tetrisanalyzer.game

object Speed {
  private var MinSpeedIndex = 0

  val PieceDelay = Array(200, 100, 60, 50, 40, 35, 30, 26, 24, 20, 16, 12, 8, 4, 2, 1)
  var MaxSpeedIndex = PieceDelay.size - 1
}

class Speed {
  private var maxSpeed = false
  private var speedIndex = 9

  def toggleMaxSpeed() { maxSpeed = !maxSpeed }
  def isMaxSpeed = maxSpeed

  def getSpeedIndex = speedIndex
  def decreaseSpeed() { maxSpeed = false; if (speedIndex > Speed.MinSpeedIndex) speedIndex -= 1 }
  def increaseSpeed() { maxSpeed = false; if (speedIndex < Speed.MaxSpeedIndex) speedIndex += 1 }

  def maxDelay = Speed.PieceDelay(1) + 20
  def pieceDelay(fastAnimation: Boolean): Int = {
    if (maxSpeed)
      2
    else
      if (fastAnimation)
        Speed.PieceDelay(speedIndex) / 2
      else
        Speed.PieceDelay(speedIndex)
  }
  def clearRowDelay(fastAnimation: Boolean): Int = {
    val delay = if (fastAnimation) (Speed.PieceDelay(speedIndex) * 2.5).toInt else Speed.PieceDelay(speedIndex) * 5
    if (delay > 200)
      200
    else
      delay
  }
}