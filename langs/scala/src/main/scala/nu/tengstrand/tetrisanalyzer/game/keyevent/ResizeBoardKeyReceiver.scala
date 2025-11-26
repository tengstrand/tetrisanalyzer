package nu.tengstrand.tetrisanalyzer.game.keyevent

import nu.tengstrand.tetrisanalyzer.game.Game

class ResizeBoardKeyReceiver(game: Game) extends KeyReceiver {
  def keyPressed(keyCode: Int, shiftKey: Boolean, ctrlKey: Boolean): Unit = {
    if (!shiftKey) {
      keyCode match {
        case 37 => game.decreaseBoardWidthKeepRatio() // Left
        case 39 => game.increaseBoardWidthKeepRatio() // Right
        case 38 => game.decreaseBoardHeight()         // Up
        case 40 => game.increaseBoardHeight()         // Down
        case _ =>
      }
    } else {
      keyCode match {
        case 37 => game.decreaseBoardWidth()  // Left
        case 39 => game.increaseBoardWidth()  // Right
        case 38 => game.decreaseBoardHeight() // Up
        case 40 => game.increaseBoardHeight() // Down
        case _ =>
      }
    }
  }
}