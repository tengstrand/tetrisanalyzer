package nu.tengstrand.tetrisanalyzer.gui.help

import java.awt.Graphics2D
import nu.tengstrand.tetrisanalyzer.gui.TextDrawer

object HelpView {
  val gameHelpView = new GameHelpView
  val rankedMovesHelpView = new RankedMovesHelpView
}

class HelpView extends TextDrawer {
  private var showView = false
  private var helpPainter: HelpPainter = HelpView.gameHelpView

  def showRankedMoves(show: Boolean) {
    if (show)
      helpPainter = HelpView.rankedMovesHelpView
    else
      helpPainter = HelpView.gameHelpView
  }
  def toggleShowView() { showView = !showView }

  def paintHelp(origoX: Int, g: Graphics2D) {
    if (showView)
      paintGraphics(origoX, g)
  }

  private def paintGraphics(origoX: Int, g: Graphics2D) {
    helpPainter.paintGraphics(origoX, g)
  }
}