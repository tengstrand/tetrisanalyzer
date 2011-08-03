package nu.tengstrand.tetrisanalyzer.game.keyevent

trait KeyReceiver {
  def keyPressed(keyCode: Int, shiftKey: Boolean, ctrlKey: Boolean)
}