package nu.tengstrand.tetrisanalyzer.gui

import java.awt.{Graphics2D, Graphics}

class HelpView extends TextDrawer {

  def paintGraphics(origoX: Int, graphics: Graphics) {
    val g = graphics.asInstanceOf[Graphics2D];

    prepareDraw(origoX, g)

    drawText("P                Toggle pause", 1, g)

    drawText("down             Animate next piece (if paused)", 3, g)

    drawText("[Ctrl] + down    Increase board height", 5, g)
    drawText("[Ctrl] + up      Decrease board height", 6, g)
    drawText("[Ctrl] + left    Decrease board width", 7, g)
    drawText("[Ctrl] + right   Increase board width", 8, g)

    drawText("e / E            Increase/decrease seed", 10, g)

    drawText("l                Toggle sliding", 12, g)




    drawText("[F1]             Show game info", 18, g)

    drawText("Tetris Analyzer - by Joakim Tengstrand", 20, g)

    drawText("https://github.com/tengstrand/tetrisanalyzer", 22, g)
    drawText("tetrisanalyzer@tengstrand.nu", 23, g)
  }
}