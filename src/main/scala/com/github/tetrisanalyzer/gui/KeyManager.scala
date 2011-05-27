package com.github.tetrisanalyzer.gui

import java.awt.{KeyEventPostProcessor, KeyboardFocusManager}
import java.awt.event.KeyEvent
import com.github.tetrisanalyzer.game.ComputerPlayer

class KeyManager(computerPlayer: ComputerPlayer, positionView: PositionView, gameInfoView: GameInfoScalaView) {
  KeyboardFocusManager.getCurrentKeyboardFocusManager.addKeyEventPostProcessor(new KeyEventPostProcessor {
    def postProcessKeyEvent(e: KeyEvent): Boolean = {
      if (e.getID() == KeyEvent.KEY_PRESSED) {
        e.getKeyCode match {
          case 40 => // Down
            computerPlayer.performStep()
          case 80 => // P = Pause
            // TODO: Change pause handling
            computerPlayer.togglePause()
            Thread.sleep(30)
            positionView.togglePause()
            gameInfoView.setPause(computerPlayer.isPaused)
          case _ => println("key=" + e.getKeyCode + " (" + KeyEvent.getKeyText(e.getKeyCode) + ")");
        }
      }
      true;
    }
  });
}