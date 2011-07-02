package nu.tengstrand.tetrisanalyzer.gui

import java.awt.{Color, Font, Graphics2D}

class TextDrawer {
  private var origoX = 0
  private var spacing = 18

  private val smallTextFont = new Font("Monospaced", Font.PLAIN, 12);
  private val mediumTextFont = new Font("Monospaced", Font.PLAIN, 14);

  def prepareDraw(useSmallFontSize: Boolean, origoX: Int, g: Graphics2D) {
    this.origoX = origoX

    if (useSmallFontSize) {
      spacing = 14
      g.setFont(smallTextFont);
    } else {
      spacing = 18
      g.setFont(mediumTextFont);
    }

    g.setColor(Color.BLACK);
  }

  def drawInfo(label: Any, value: Any, row: Int, g: Graphics2D) {
    drawText(label + " " + value, row, g)
  }

  def drawText(text: String, row: Int, g: Graphics2D) {
    val y = 10 + row * spacing
    g.drawString(text, origoX, y)
  }
}