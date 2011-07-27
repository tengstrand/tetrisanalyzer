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
    prepareDraw(origoX, g, 10, new Color(0, 120, 0))

    drawText("p = Pause (toggle)", 1, g)

    drawText("down = Move next piece (if paused) or", 3, g)
    drawText("       increase speed if not paused", 4, g)

    drawText("[Ctrl] + left/right/up/down = Change board size", 6, g)
    drawText("                              + restart game", 7, g)

    drawText("e / E = Change random seed + restart game", 13, g)
    drawText("l = Toggle sliding + restart game",14, g)

    drawText("s / S = Change animation speed", 16, g)
    drawText("[Ctrl] + s = Max speed (toggle)", 17, g)


    drawText("Tetris Analyzer " + version, 32, g)
    drawText("AI: JTengstrand 1.1.01", 33, g)
    drawText("tetrisanalyzer@tengstrand.nu", 34, g)
    drawText("https://github.com/tengstrand/tetrisanalyzer", 35, g)
  }
}