package nu.tengstrand.tetrisanalyzer.gui

import java.awt.{KeyEventPostProcessor, KeyboardFocusManager}
import java.awt.event.KeyEvent
import nu.tengstrand.tetrisanalyzer.game.{GameInfoReceiver, ComputerPlayer, Timer}

class KeyManager(computerPlayer: ComputerPlayer, positionView: PositionView, gameInfoReceiver: GameInfoReceiver, timer: Timer) {
  KeyboardFocusManager.getCurrentKeyboardFocusManager.addKeyEventPostProcessor(new KeyEventPostProcessor {
    def postProcessKeyEvent(e: KeyEvent): Boolean = {
      if (e.getID() == KeyEvent.KEY_PRESSED) {
        e.getKeyCode match {
          case 40 => // Down
            computerPlayer.performStep()
          case 80 => // P = Pause
            timer.togglePause
            gameInfoReceiver.setPaused(timer.paused)
            positionView.setPaused(timer.paused)
            computerPlayer.setPaused(timer.paused)
          case _ => println("key=" + e.getKeyCode + " (" + KeyEvent.getKeyText(e.getKeyCode) + ")");
        }
      }
      true;
    }
  });
}
