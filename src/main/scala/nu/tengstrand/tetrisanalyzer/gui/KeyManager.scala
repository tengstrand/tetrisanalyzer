package nu.tengstrand.tetrisanalyzer.gui

import java.awt.{KeyEventPostProcessor, KeyboardFocusManager}
import java.awt.event.KeyEvent
import nu.tengstrand.tetrisanalyzer.game.Game

class KeyManager(game: Game) {
  KeyboardFocusManager.getCurrentKeyboardFocusManager.addKeyEventPostProcessor(new KeyEventPostProcessor {
    def postProcessKeyEvent(e: KeyEvent): Boolean = {
      if (e.getID() == KeyEvent.KEY_PRESSED) {
        e.getKeyCode match {
          case 40 => // Down
            if (e.getModifiers == 2)
              game.increaseBoardHeight
            else
              game.performMove()
          case 80 => // P = Pause
            game.togglePause()
          case 37 =>
            if (e.getModifiers == 2)  // <CTRL> + Left
              game.decreaseBoardWidth
          case 38 =>
            if (e.getModifiers == 2)  // <CTRL> + Up
              game.decreaseBoardHeight
          case 39 => // Right
            if (e.getModifiers == 2)  // <CTRL> + Right
              game.increaseBoardWidth
          case _ =>
            println("key=" + e.getKeyCode + " (" + KeyEvent.getKeyText(e.getKeyCode) + ")");
        }
      }
      true;
    }
  });
}
