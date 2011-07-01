package nu.tengstrand.tetrisanalyzer.gui

import java.awt.{Graphics2D, Graphics}

class HelpView extends TextDrawer {
  private def version = 1.01

  def paintGraphics(origoX: Int, graphics: Graphics) {
    val g = graphics.asInstanceOf[Graphics2D];

    prepareDraw(origoX, g)

    drawText("P                Toggle pause", 1, g)

    drawText("down             Animate next piece (if paused)", 3, g)

    drawText("left/right       Increase/decrease speed", 5, g)

    drawText("[Ctrl] + down    Increase board height", 7, g)
    drawText("[Ctrl] + up      Decrease board height", 8, g)
    drawText("[Ctrl] + left    Decrease board width", 9, g)
    drawText("[Ctrl] + right   Increase board width", 10, g)

    drawText("e / E            Increase/decrease seed", 12, g)

    drawText("l                Toggle sliding", 14, g)




    drawText("[F1]             Show game info", 18, g)

    drawText("Tetris Analyzer " + version + " - by Joakim Tengstrand", 20, g)
    drawText("https://github.com/tengstrand/tetrisanalyzer", 21, g)
    drawText("Mail: tetrisanalyzer@tengstrand.nu", 22, g)
  }
}