package nu.tengstrand.tetrisanalyzer.gui

import java.awt.{KeyEventPostProcessor, KeyboardFocusManager}
import java.awt.event.KeyEvent
import nu.tengstrand.tetrisanalyzer.game.Game

class KeyManager(game: Game) {
  KeyboardFocusManager.getCurrentKeyboardFocusManager.addKeyEventPostProcessor(new KeyEventPostProcessor {
    def postProcessKeyEvent(e: KeyEvent): Boolean = {
      if (e.getID() == KeyEvent.KEY_PRESSED) {
        e.getKeyCode match {
          case 40 =>
            if (e.getModifiers == 2) // <CTRL> + Down
              game.increaseBoardHeight
            else
              game.performMove()  // Down
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
          case 76 =>  // L = Toggle sliding
            game.toggleSliding()
          case 69 =>
            if (e.getModifiers == 1)
              game.decreaseSeed() // <Shift> + E = Decrease seed
            else
              game.increaseSeed() // E = Increase seed
          case 112 => // <F1> = toggle Help
              game.toggleShowHelp()
          case _ =>
            println("key=" + e.getKeyCode + " (" + KeyEvent.getKeyText(e.getKeyCode) + "), modifiers=" + e.getModifiers);
        }
      }
      true;
    }
  });
}
