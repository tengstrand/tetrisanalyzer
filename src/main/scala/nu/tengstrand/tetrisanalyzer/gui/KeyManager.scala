package nu.tengstrand.tetrisanalyzer.gui

import java.awt.{KeyEventPostProcessor, KeyboardFocusManager}
import java.awt.event.KeyEvent
import nu.tengstrand.tetrisanalyzer.game.Game

class KeyManager(game: Game, gameView: GameView) {

  KeyboardFocusManager.getCurrentKeyboardFocusManager.addKeyEventPostProcessor(new KeyEventPostProcessor {
    def postProcessKeyEvent(e: KeyEvent): Boolean = {
      if (e.getID == KeyEvent.KEY_PRESSED) {
        e.getKeyCode match {
          case 40 =>
            if (e.getModifiers == 2) // <CTRL> + Down
              game.increaseBoardHeight()
            else
              if (gameView.isRankedMovesVisible) // Down
                game.selectNextRankedMove()
              else
                game.performMove()
          case 80 => // P
            game.togglePause()
          case 37 =>
            if (e.getModifiers == 0)  // Left
              game.showRankedMoves(false)
            if (e.getModifiers == 2)  // <CTRL> + Left
              game.decreaseBoardWidth()
          case 38 =>
            if (e.getModifiers == 2)  // <CTRL> + Up
              game.selectNextRankedMove()
            else if (gameView.isRankedMovesVisible) // Up
                game.selectPreviousRankedMove()
          case 39 =>
            if (e.getModifiers == 0) // Right
              if (gameView.isRankedMovesVisible)
                game.performMove()
              else
                game.showRankedMoves(true)
            else if (e.getModifiers == 2)  // <CTRL> + Right
              game.increaseBoardWidth()
          case 76 =>  // L = Toggle sliding
            game.toggleSliding()
          case 69 =>
            if (e.getModifiers == 1)
              game.decreaseSeed() // E
            else
              game.increaseSeed() // e
          case 83 => // s
            if (e.getModifiers == 0)
              game.increaseSpeed() // S
            else if (e.getModifiers == 1)
              game.decreaseSpeed() // s
            else if (e.getModifiers == 2) // <CTRL> + s
              game.toggleMaxSpeed()
          case 112 => // <F1>
            game.toggleSmallBoard()
          case 113 => // <F2>
            game.toggleShowGameInfo()
          case 114 => // <F3>
            game.toggleShowHelp()

          case _ =>
            println("key=" + e.getKeyCode + " (" + KeyEvent.getKeyText(e.getKeyCode) + "), modifiers=" + e.getModifiers);
        }
      }
      true;
    }
  });
}
