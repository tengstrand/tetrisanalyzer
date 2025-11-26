package nu.tengstrand.tetrisanalyzer.gui.help

import java.awt.{Color, Graphics2D}
import nu.tengstrand.tetrisanalyzer.gui.{TetrisAnalyzer, TextPainter}

class GameHelpView extends TextPainter with HelpPainter {
  private var paused = true

  def setPaused(paused: Boolean): Unit = { this.paused = paused }

  def paintGraphics(origoX: Int, g: Graphics2D): Unit = {
    prepareDrawText(origoX, g, 10, HelpColor.Color)

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



    drawText("Tetris Analyzer " + TetrisAnalyzer.Version, 32, g)
    drawText("AI: JTengstrand 1.1.01", 33, g)
    drawText("tetrisanalyzer@tengstrand.nu", 34, g)
    drawText("http://tetrisanalyzer.tengstrand.nu", 35, g)
  }
}