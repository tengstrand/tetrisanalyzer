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
            e.getModifiers match {
              case 0 => game.showRankedMoves(false) // Left
              case 1 => game.decreaseSpeed()        // <Shift> + Left
              case 2 => game.decreaseBoardWidth()   // <CTRL> + Left
              case _ =>
            }
          case 38 =>
            if (e.getModifiers == 2)  // <CTRL> + Up
              game.decreaseBoardHeight()
            else if (gameView.isRankedMovesVisible) // Up
              game.selectPreviousRankedMove()
          case 39 =>
            e.getModifiers match {
              case 0 => // Right
                if (gameView.isRankedMovesVisible)
                  game.performMove()
                else
                  game.showRankedMoves(true)
              case 1 => game.increaseSpeed()      // <Shift> + Right
              case 2 => game.increaseBoardWidth() // <CTRL> + Right
              case _ =>
            }
          case 76 =>  // L = Toggle sliding
            game.toggleSliding()
          case 83 =>
            if (e.getModifiers == 1)
              game.decreaseSeed() // S
            else
              game.increaseSeed() // s
          case 77 =>
            game.toggleMaxSpeed() // m
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
