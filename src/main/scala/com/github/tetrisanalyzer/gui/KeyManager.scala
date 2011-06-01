package com.github.tetrisanalyzer.gui

import java.awt.{KeyEventPostProcessor, KeyboardFocusManager}
import java.awt.event.KeyEvent
import com.github.tetrisanalyzer.game.ComputerPlayer
import com.github.tetrisanalyzer.game.Timer

class KeyManager(computerPlayer: ComputerPlayer, positionView: PositionView, timer: Timer) {
  KeyboardFocusManager.getCurrentKeyboardFocusManager.addKeyEventPostProcessor(new KeyEventPostProcessor {
    def postProcessKeyEvent(e: KeyEvent): Boolean = {
      if (e.getID() == KeyEvent.KEY_PRESSED) {
        e.getKeyCode match {
          case 40 => // Down
            computerPlayer.performStep()
          case 80 => // P = Pause
            timer.togglePause
            positionView.setPaused(timer.paused)
            computerPlayer.setPaused(timer.paused)
          case _ => println("key=" + e.getKeyCode + " (" + KeyEvent.getKeyText(e.getKeyCode) + ")");
        }
      }
      true;
    }
  });
}
