package nu.tengstrand.tetrisanalyzer.gui

import java.awt.{Graphics2D, Graphics}

class HelpView extends TextDrawer {
  private def version = 1.01

  def paintGraphics(origoX: Int, graphics: Graphics) {
    val g = graphics.asInstanceOf[Graphics2D];

    prepareDraw(origoX, g)

    drawText("P               Toggle pause", 1, g)

    drawText("down            Animate next piece (if paused)", 3, g)

    drawText("left/right      Change speed", 5, g)

    drawText("[Ctrl] + left/  Change board size", 7, g)
    drawText("right/up/down", 8, g)

    drawText("e / E           Change seed", 10, g)

    drawText("l               Toggle sliding", 12, g)




    drawText("[F1]            Show game info", 18, g)

    drawText("Tetris Analyzer " + version, 20, g)
    drawText("tetrisanalyzer@tengstrand.nu", 21, g)
    drawText("https://github.com/tengstrand/tetrisanalyzer", 22, g)
  }
}