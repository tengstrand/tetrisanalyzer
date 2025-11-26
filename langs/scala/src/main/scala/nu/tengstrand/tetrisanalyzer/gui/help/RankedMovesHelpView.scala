package nu.tengstrand.tetrisanalyzer.gui.help

import java.awt.Graphics2D
import nu.tengstrand.tetrisanalyzer.gui.TextPainter

class RankedMovesHelpView extends TextPainter with HelpPainter {

  def paintGraphics(origoX: Int, g: Graphics2D): Unit = {
    prepareDrawText(origoX, g, 10, HelpColor.Color)

    drawText("left = Hide moves", 1, g)

    drawText("space / right = Perform selected move", 3, g)

    drawText("up / down = Select move", 5, g)


    drawText("============== Ranked moves ==============", 7, g)
    drawText(" v = Number of rotations for the piece.", 8, g)
    drawText(" x = X position.", 9, g)
    drawText(" y = Y position (shown only if sliding is", 10, g)
    drawText("       activated and at least one piece", 11, g)
    drawText("       can be slided).", 12, g)
    drawText(" Depth = Look ahead. A search depth of 0", 13, g)
    drawText("         means that only the current piece", 14, g)
    drawText("         and next piece (if enabled) is known.", 15, g)
  }
}