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

    drawText("left / right: Change board width", 6, g)
    drawText("              (keep 1:2 ratio if possible)", 7, g)
    drawText("up / down   : Change board height", 8, g)
    drawText("              (keep 1:2 ratio if possible)", 9, g)

    drawText("[Shift] + left : Decrease board width", 11, g)
    drawText("[Shift] + right: Increase board width", 12, g)
    drawText("[Shift] + up   : Decrease board height", 13, g)
    drawText("[Shift] + down : Increase board height", 14, g)

    drawText("Press 'b' to accept or [Esc] to abort", 17, g)
  }
}