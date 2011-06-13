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
            game.performMove()
          case 80 => // P = Pause
            game.pause()
          case 37 => // Left
            println("modifiers=" + e.getModifiers)
          case 39 => // Right
            println("modifiers=" + e.getModifiers)
          case _ =>
            //println(e)
            println("key=" + e.getKeyCode + " (" + KeyEvent.getKeyText(e.getKeyCode) + ")");
        }
      }
      true;
    }
  });
}
