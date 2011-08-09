package nu.tengstrand.tetrisanalyzer.gui.help

import java.awt.{Color, Graphics2D}
import nu.tengstrand.tetrisanalyzer.gui.TextDrawer

class RankedMovesHelpView extends TextDrawer with HelpPainter {

  def paintGraphics(origoX: Int, g: Graphics2D) {
    prepareDrawText(origoX, g, 10, new Color(0, 120, 0))

    drawText("left = Hide moves", 1, g)

    drawText("space / right = Perform selected move", 3, g)

    drawText("up = Select previous move", 5, g)

    drawText("down = Select next move", 7, g)


    drawText("============== Ranked moves ==============", 9, g)
    drawText(" v = Number of rotations for the piece.", 10, g)
    drawText(" x = X position.", 11, g)
    drawText(" y = Y position (shown only if sliding is", 12, g)
    drawText("       activated and at least one piece", 13, g)
    drawText("       can be slided).", 14, g)
    drawText(" Depth = Look ahead. A search depth of 0", 15, g)
    drawText("         means that only the current piece", 16, g)
    drawText("         and next piece (if enabled) is known.", 17, g)
  }
}