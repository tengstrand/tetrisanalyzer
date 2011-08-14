package nu.tengstrand.tetrisanalyzer.gui.position

import java.awt.Graphics2D
import nu.tengstrand.tetrisanalyzer.gui.TextPainter
import nu.tengstrand.tetrisanalyzer.gui.help.HelpColor

class PositionStartupHelpPainter extends TextPainter {

  def paintGraphics(origoX: Int, g: Graphics2D) {
    setMediumFont(g)
    prepareDrawText(origoX, g, 8, HelpColor.Color)

    drawText("Press 'P' to start", 14, g)

    drawText(" or [F1] for help", 16, g)
  }
}