package nu.tengstrand.tetrisanalyzer.gui.help

import nu.tengstrand.tetrisanalyzer.gui.TextDrawer
import java.awt.{Color, Graphics2D}

class ResizeBoardHelpView extends TextDrawer with HelpPainter {
  private var boardWidth = 10
  private var boardHeight = 20

  def setBoardSize(width: Int, height: Int) {
    boardWidth = width
    boardHeight = height
  }

  def paintGraphics(origoX: Int, g: Graphics2D) {
    // Black text
    setBigFont(g)
    prepareDrawText(origoX, g, 10)
    drawText("Size: " + boardWidth + " x " + boardHeight, 2, g)

    // Green help text
    setDefaultFont(g)
    prepareDrawText(origoX, g, 10, new Color(0, 120, 0))

    drawText("Use the arrow keys to change the board size", 4, g)
    drawText("===========================================", 5, g)

    drawText("left / right: Change width, keep 1:2 ratio", 6, g)

    drawText("up / down   : Change height", 8, g)

    drawText("[Shift] + arrow: Change size by one", 10, g)


    drawText("Press 'b' to accept or [Esc] to abort", 13, g)
  }
}