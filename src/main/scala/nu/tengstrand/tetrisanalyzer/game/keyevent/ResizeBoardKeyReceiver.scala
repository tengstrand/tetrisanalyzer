package nu.tengstrand.tetrisanalyzer.game.keyevent

import nu.tengstrand.tetrisanalyzer.game.Game

class ResizeBoardKeyReceiver(game: Game) extends KeyReceiver {
  def keyPressed(keyCode: Int, shiftKey: Boolean, ctrlKey: Boolean) {
    if (shiftKey) {
      keyCode match {
        case 37 => game.decreaseBoardWidth()  // Left
        case 38 => game.decreaseBoardHeight() // Up
        case 39 => game.increaseBoardWidth()  // Right
        case 40 => game.increaseBoardHeight() // Down
        case _ =>
      }
    } else {
      keyCode match {
        case 37 => game.decreaseBoardSizeByWidth()  // <Shift> + Left
        case 38 => game.decreaseBoardSizeByHeight() // <Shift> + Up
        case 39 => game.increaseBoardSizeByWidth()  // <Shift> + Right
        case 40 => game.increaseBoardSizeByHeight() // <Shift> + Down
        case _ =>
      }
    }
  }
}