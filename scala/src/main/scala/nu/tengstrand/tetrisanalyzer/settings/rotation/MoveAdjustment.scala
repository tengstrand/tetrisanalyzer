package nu.tengstrand.tetrisanalyzer.settings.rotation

case class MoveAdjustment(x: Int, y: Int) {
  def -(that: MoveAdjustment): MoveAdjustment = MoveAdjustment(x - that.x, y - that.y)
}
