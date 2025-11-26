package nu.tengstrand.tetrisanalyzer.gui

import java.awt.{Graphics2D, Font}

class FontChooser {
  private val fonts = Array.tabulate(5) (((size) => new Font("Serif", Font.PLAIN, size + 8)))

  def setFont(squareHeight: Int, g: Graphics2D): Unit = {
    var fontIndex = (squareHeight - 10) / 2

    if (fontIndex < 0)
      fontIndex = 0

    if (fontIndex > fonts.size - 1)
      fontIndex = fonts.size - 1

    g.setFont(fonts(fontIndex))
  }
}