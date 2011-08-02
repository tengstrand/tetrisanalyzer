package nu.tengstrand.tetrisanalyzer.gui.keyevent

trait KeyReceiver {
  def keyPressed(keyCode: Int, shiftKey: Boolean, ctrlKey: Boolean)
}