package nu.tengstrand.tetrisanalyzer.game.keyevent

import java.awt.event.KeyEvent
import nu.tengstrand.tetrisanalyzer.game.Game
import nu.tengstrand.tetrisanalyzer.gui.GameView

class GameKeyReceiver(game: Game, gameView: GameView) extends KeyReceiver {

  def keyPressed(keyCode: Int, shiftKey: Boolean, ctrlKey: Boolean) {
        keyCode match {
          case 32 => // Space
            game.performMove()
          case 40 =>
            if (game.isRankedMovesVisible) // Down
              gameView.selectNextRankedMove()
            else
              game.performMove()
          case 37 =>
            if (shiftKey) game.decreaseSpeed()          // <Shift> + Left
            else game.showRankedMoves(false)            // Left
          case 38 =>
            if (game.isRankedMovesVisible)
              gameView.selectPreviousRankedMove()       // Up
          case 39 =>
            if (shiftKey) game.increaseSpeed()          // <Shift> + Right
            else {                                      // Right
              if (game.isRankedMovesVisible)
                game.performMove()
              else
                game.showRankedMoves(true)
            }
          case 76 =>  // l = Toggle sliding
            game.toggleSliding()
          case 77 =>
            game.toggleMaxSpeed() // m
          case 78 => // n
            game.toggleShowNextPiece()
          case 80 => // p
            game.togglePause()
          case 82 =>
            if (shiftKey) game.decreaseSeed() // R
            else game.increaseSeed()          // r
          case 112 => // <F1>
            gameView.toggleShowHelp()
          case 113 => // <F2>
            gameView.toggleMiniatureBoard()
          case 114 => // <F3>
            gameView.toggleShowGameInfo()
          case _ =>
            println("key=" + keyCode + " (" + KeyEvent.getKeyText(keyCode) + "), shift=" + shiftKey + ", ctrl=" + ctrlKey);
        }
  }
}