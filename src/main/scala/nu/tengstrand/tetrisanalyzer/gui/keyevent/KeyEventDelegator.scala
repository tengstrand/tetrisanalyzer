package nu.tengstrand.tetrisanalyzer.gui.keyevent

import java.awt.{KeyEventPostProcessor, KeyboardFocusManager}
import java.awt.event.KeyEvent
import nu.tengstrand.tetrisanalyzer.game.Game
import nu.tengstrand.tetrisanalyzer.gui.GameView

/**
 * Responsible for delegating key events to right key receiver.
 */
class KeyEventDelegator(game: Game, gameView: GameView) extends KeyReceiver {
  private val gameKeyReceiver = new GameKeyReceiver(game, gameView)
  private val resizeBoardKeyReceiver = new ResizeBoardKeyReceiver(game)
  private var keyReceiver: KeyReceiver = gameKeyReceiver

  KeyboardFocusManager.getCurrentKeyboardFocusManager.addKeyEventPostProcessor(new KeyEventPostProcessor {
    def postProcessKeyEvent(e: KeyEvent): Boolean = {
      if (e.getID == KeyEvent.KEY_PRESSED) {
        keyPressed(e.getKeyCode, (e.getModifiers & 1) == 1, (e.getModifiers & 2) == 2)
      }
      true;
    }
  });

  def keyPressed(keyCode: Int, shiftKey: Boolean, ctrlKey: Boolean) {
    if (keyCode == 27 && isResizingBoard) {
      keyReceiver = gameKeyReceiver
      game.abortBoardSize() // <Esc>
    } else
      keyCode match {
        case 66 => // b
          if (isResizingBoard) {
            game.acceptBoardSize()
            keyReceiver = gameKeyReceiver
          } else {
            game.startResizeBoard()
            keyReceiver = resizeBoardKeyReceiver
          }
        case _ =>
          keyReceiver.keyPressed(keyCode, shiftKey, ctrlKey)
      }
  }

  private def isResizingBoard = keyReceiver == resizeBoardKeyReceiver
}
