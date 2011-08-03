package nu.tengstrand.tetrisanalyzer.gui.help

import java.awt.{Color, Graphics2D}
import nu.tengstrand.tetrisanalyzer.gui.TextDrawer

class GameHelpView extends TextDrawer with HelpPainter {
  private val version = "2.0"

  private var paused = true

  def setPaused(paused: Boolean) { this.paused = paused }

  def paintGraphics(origoX: Int, g: Graphics2D) {
    prepareDrawText(origoX, g, 10, new Color(0, 120, 0))

    drawText("p = Pause (toggle)", 1, g)

    drawText("right = Show moves", 3, g)

    val pausedText = if (paused) "Move next piece" else "Move piece faster"
    drawText("space / down = " + pausedText, 5, g)

    drawText("b = Change board size", 7, g)

    drawText("s / S = Change random generator seed", 9, g)

    drawText("l = Toggle sliding", 11, g)

    drawText("m = Max speed (toggle)", 13, g)

    drawText("[Shift] + left = Decrease animation speed", 15, g)
    drawText("[Shift] + right = Increase animation speed", 16, g)



    drawText("Tetris Analyzer " + version, 32, g)
    drawText("AI: JTengstrand 1.1.01", 33, g)
    drawText("tetrisanalyzer@tengstrand.nu", 34, g)
    drawText("https://github.com/tengstrand/tetrisanalyzer", 35, g)
  }
}