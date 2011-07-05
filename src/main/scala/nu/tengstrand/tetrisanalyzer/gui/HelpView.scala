package nu.tengstrand.tetrisanalyzer.gui

import java.awt.{Color, Graphics2D, Graphics}

class HelpView extends TextDrawer {
  private def version = 1.01

  def paintGraphics(origoX: Int, graphics: Graphics) {
    val g = graphics.asInstanceOf[Graphics2D];

    prepareDraw(false, origoX, g, 10, new Color(0, 150, 0))

    drawText("p = Pause (toggle)", 1, g)

    drawText("down = Animate next piece (if paused)", 3, g)

    drawText("[Ctrl] + left/right/up/down = Change board size", 6, g)
    drawText("                              + restart game", 7, g)

    drawText("e / E = Change random seed + restart game", 13, g)
    drawText("l = Toggle sliding + restart game",14, g)

    drawText("left/right = Change animation speed", 16, g)


    drawText("Tetris Analyzer " + version, 25, g)
    drawText("tetrisanalyzer@tengstrand.nu", 26, g)
    drawText("https://github.com/tengstrand/tetrisanalyzer", 27, g)
  }
}