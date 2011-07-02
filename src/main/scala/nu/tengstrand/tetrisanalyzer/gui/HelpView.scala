package nu.tengstrand.tetrisanalyzer.gui

import java.awt.{Graphics2D, Graphics}

class HelpView extends TextDrawer {
  private def version = 1.01

  def paintGraphics(origoX: Int, graphics: Graphics) {
    val g = graphics.asInstanceOf[Graphics2D];

    prepareDraw(origoX, g)

    drawText("P = Toggle pause", 1, g)
    drawText("down = Animate next piece (if paused)", 2, g)

    drawText("left/right = Change speed (1..10)", 4, g)

    drawText("[Ctrl] + left/right/up/down = Change board size", 6, g)

    drawText("e / E = Change seed", 8, g)
    drawText("l = Toggle sliding", 9, g)



    drawText("[F1] = Show game info", 22, g)

    drawText("Tetris Analyzer " + version, 24, g)
    drawText("tetrisanalyzer@tengstrand.nu", 25, g)
    drawText("https://github.com/tengstrand/tetrisanalyzer", 26, g)
  }
}