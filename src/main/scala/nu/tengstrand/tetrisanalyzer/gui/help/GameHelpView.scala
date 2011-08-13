package nu.tengstrand.tetrisanalyzer.gui.help

import java.awt.{Color, Graphics2D}
import nu.tengstrand.tetrisanalyzer.gui.{TetrisAnalyzer, TextDrawer}

class GameHelpView extends TextDrawer with HelpPainter {
  private var paused = true

  def setPaused(paused: Boolean) { this.paused = paused }

  def paintGraphics(origoX: Int, g: Graphics2D) {
    prepareDrawText(origoX, g, 10, new Color(0, 120, 0))

    drawText("p = Pause (toggle)", 1, g)

    drawText("right = Show valid moves", 3, g)

    val pausedText = if (paused) "Move next piece" else "Increase speed"
    drawText("space / down = " + pausedText, 5, g)

    drawText("b = Change board size", 7, g)

    drawText("r / R = Change seed + restart random sequence", 9, g)

    drawText("l = Sliding (toggle)", 11, g)

    drawText("n = Show next piece (toggle)", 13, g)


    drawText("m = Max speed (toggle)", 16, g)

    drawText("[Shift] + left = Decrease animation speed", 18, g)
    drawText("[Shift] + right = Increase animation speed", 19, g)


    drawText("The game info is reseted when pressing any of: r / l or n", 23, g)



    drawText("Tetris Analyzer " + TetrisAnalyzer.version, 31, g)
    drawText("AI: JTengstrand 1.1.01", 32, g)
    drawText("tetrisanalyzer@tengstrand.nu", 33, g)
    drawText("http://tetris.tengstrand.nu or", 34, g)
    drawText("http://tetrisanalyzer.tengstrand.nu", 35, g)
  }
}