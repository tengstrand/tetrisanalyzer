package nu.tengstrand.tetrisanalyzer.gui

import java.awt.{Color, Graphics2D}

class HelpView extends TextDrawer {
  private def version = "2.0"

  private var showView = false

  def toggleShowView() { showView = !showView }

  def paintHelp(origoX: Int, g: Graphics2D) {
    if (showView)
      paintGraphics(origoX, g)
  }

  private def paintGraphics(origoX: Int, g: Graphics2D) {
    prepareDrawText(origoX, g, 10, new Color(0, 120, 0))

    drawText("p = Pause (toggle)", 1, g)

    drawText("right = Show ranked moves", 3, g)

    drawText("left = Hide ranked moves", 5, g)

    drawText("up = Select previous ranked move", 7, g)

    drawText("down = If ranked moves is hidden:", 9, g)
    drawText("         Move next piece (if paused) or", 10, g)
    drawText("         increase speed if not paused", 11, g)
    drawText("       If ranked moves is visible:", 12, g)
    drawText("         Select next ranked move", 13, g)

    drawText("space = Move next piece (if paused)", 15, g)


    drawText("[Ctrl] + left/right/up/down = Change board size", 17, g)
    drawText("                              + restart game", 18, g)

    drawText("e / E = Change random seed + restart game", 20, g)
    drawText("l = Toggle sliding + restart game",21, g)

    drawText("s / S = Change animation speed", 23, g)
    drawText("[Ctrl] + s = Max speed (toggle)", 24, g)


    drawText("Tetris Analyzer " + version, 32, g)
    drawText("AI: JTengstrand 1.1.01", 33, g)
    drawText("tetrisanalyzer@tengstrand.nu", 34, g)
    drawText("https://github.com/tengstrand/tetrisanalyzer", 35, g)
  }
}