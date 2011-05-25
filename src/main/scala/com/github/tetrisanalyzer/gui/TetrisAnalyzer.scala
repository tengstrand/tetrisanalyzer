package com.github.tetrisanalyzer.gui

import scala.swing._
import com.github.tetrisanalyzer.board.Board
import com.github.tetrisanalyzer.boardevaluator.TengstrandBoardEvaluator1
import com.github.tetrisanalyzer.piecegenerator.DefaultPieceGenerator
import com.github.tetrisanalyzer.settings.DefaultGameSettings
import scala.actors.Actor._
import com.github.tetrisanalyzer.game.Timer._
import com.github.tetrisanalyzer.game._
import java.awt.{KeyEventPostProcessor, KeyboardFocusManager}
import java.awt.event.KeyEvent

object TetrisAnalyzer extends SimpleSwingApplication {

  def top = new MainFrame {
    title = "Tetris Analyzer - by Joakim Tengstrand"
    preferredSize = new Dimension(397,500)
/*
    val label = new Label {
      text = "testing"
    }
*/
    val board = Board()
    val boardEvaluator = new TengstrandBoardEvaluator1(board.width, board.height)
    val pieceGenerator = new DefaultPieceGenerator(5)
    val settings = new DefaultGameSettings
    val position = Position()
    val positionView = new PositionView(settings)

    val gameEventReceiver = new GameEventReceiver(position, settings, positionView)
    val computerPlayer = new ComputerPlayer(board, boardEvaluator, pieceGenerator, settings, gameEventReceiver)

    contents = new BoxPanel(Orientation.Vertical) {
      contents += positionView
//      contents += label
    }

    gameEventReceiver.start()
    positionView.start
    computerPlayer.start()

    KeyboardFocusManager.getCurrentKeyboardFocusManager.addKeyEventPostProcessor(new KeyEventPostProcessor {
      def postProcessKeyEvent(e: KeyEvent): Boolean = {
        if (e.getID() == KeyEvent.KEY_PRESSED) {
          e.getKeyCode match {
            case 40 => // Down
              computerPlayer.performStep()
            case 83 => // S = toggle Step mode
              computerPlayer.toggleStepMode()
              Thread.sleep(30)
              positionView.toggleStepMode()
            case _ => println("key=" + e.getKeyCode + " (" + KeyEvent.getKeyText(e.getKeyCode));
          }
        }
        true;
      }
    });

    actor {
      fiftyTimesPerSecond(() => {
        positionView.repaint
      })
    }
  }
}