package com.github.tetrisanalyzer.gui

import java.awt.{KeyEventPostProcessor, KeyboardFocusManager}
import java.awt.event.KeyEvent
import com.github.tetrisanalyzer.game.ComputerPlayer

class KeyManager(computerPlayer: ComputerPlayer, positionView: PositionView, gameInfoView: GameInfoView) {
  private var paused: Boolean = true
  private var timeStarted = 0L;
  private var totalTimeMs = 0L;


  KeyboardFocusManager.getCurrentKeyboardFocusManager.addKeyEventPostProcessor(new KeyEventPostProcessor {
    def postProcessKeyEvent(e: KeyEvent): Boolean = {
      if (e.getID() == KeyEvent.KEY_PRESSED) {
        e.getKeyCode match {
          case 40 => // Down
            computerPlayer.performStep()
          case 80 => // P = Pause
            paused = !paused
            positionView.setPaused(paused)
            gameInfoView.setPaused(paused)
            computerPlayer.setPaused(paused)

            toggleTime

            gameInfoView.setTimePassed(totalTimeMs / 1000.0)

          case _ => println("key=" + e.getKeyCode + " (" + KeyEvent.getKeyText(e.getKeyCode) + ")");
        }
      }
      true;
    }
  });

  private def toggleTime() {
    val currentTimeMs = System.currentTimeMillis

    if (paused) {
      if (timeStarted != 0) {
        totalTimeMs += currentTimeMs - timeStarted
      }
    } else {
      timeStarted = currentTimeMs
    }
  }
}
