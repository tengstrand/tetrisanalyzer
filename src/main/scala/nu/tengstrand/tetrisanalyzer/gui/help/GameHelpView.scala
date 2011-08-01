package nu.tengstrand.tetrisanalyzer.gui.help

import java.awt.{Color, Graphics2D}
import nu.tengstrand.tetrisanalyzer.gui.TextDrawer

class GameHelpView extends TextDrawer with HelpPainter {
  private def version = "2.0"

  def paintGraphics(origoX: Int, g: Graphics2D) {
    prepareDrawText(origoX, g, 10, new Color(0, 120, 0))

    drawText("p = Pause (toggle)", 1, g)

    drawText("right = Show ranked moves", 3, g)

    drawText("up = Select previous ranked move", 5, g)

    drawText("down = Move next piece (if paused) or", 7, g)
    drawText("       increase speed if not paused", 8, g)

    drawText("[Ctrl] + left/right/up/down = Change board size", 10, g)
    drawText("                              + restart game", 11, g)

    drawText("s / S = Change random generator seed", 13, g)

    drawText("l = Toggle sliding", 15, g)

    drawText("[Shift] + left = Decrease animation speed", 17, g)
    drawText("[Shift] + right = Increase animation speed", 18, g)

    drawText("m = Max speed (toggle)", 20, g)


    drawText("Tetris Analyzer " + version, 32, g)
    drawText("AI: JTengstrand 1.1.01", 33, g)
    drawText("tetrisanalyzer@tengstrand.nu", 34, g)
    drawText("https://github.com/tengstrand/tetrisanalyzer", 35, g)
  }
}