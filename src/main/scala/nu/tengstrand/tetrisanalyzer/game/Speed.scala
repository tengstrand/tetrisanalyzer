package nu.tengstrand.tetrisanalyzer.game

object Speed {
  var MinSpeed = 1
  var MaxSpeed = 10

  val PieceDelay = Array(0, 80, 60, 40, 28, 20, 16, 12, 8, 4, 0)
}

class Speed {
  private var speed = 5

  def isMaxSpeed = speed == Speed.MaxSpeed

  def decreaseSpeed() { if (speed > Speed.MinSpeed) speed -= 1 }
  def increaseSpeed() { if (speed < Speed.MaxSpeed) speed += 1 }

  def asName = if (isMaxSpeed) "Max" else speed.toString

  def maxDelay = 100
  def pieceDelay(fastAnimation: Boolean): Int = if (fastAnimation) Speed.PieceDelay(speed) / 2 else Speed.PieceDelay(speed)
  def clearRowDelay(fastAnimation: Boolean): Int = if (fastAnimation) (Speed.PieceDelay(speed) * 2.5).toInt else Speed.PieceDelay(speed) * 5
}