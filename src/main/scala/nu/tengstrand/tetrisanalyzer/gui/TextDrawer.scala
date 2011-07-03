package nu.tengstrand.tetrisanalyzer.gui

import java.awt.{Color, Font, Graphics2D}

class TextDrawer {
  private var origoX = 0
  private var origoY = 10
  private var spacing = 18

  private val smallTextFont = new Font("Monospaced", Font.PLAIN, 12);
  private val mediumTextFont = new Font("Monospaced", Font.PLAIN, 14);

  def prepareDraw(useSmallFontSize: Boolean, origoX: Int, g: Graphics2D, origoY: Int = 10, color: Color = Color.BLACK) {
    this.origoX = origoX
    this.origoY = origoY

    if (useSmallFontSize) {
      spacing = 14
      g.setFont(smallTextFont);
    } else {
      spacing = 18
      g.setFont(mediumTextFont);
    }

    g.setColor(color);
  }

  def drawInfo(label: Any, value: Any, row: Int, g: Graphics2D) {
    drawText(label + " " + value, row, g)
  }

  def drawText(text: Any, row: Int, g: Graphics2D) {
    val y = origoY + row * spacing
    g.drawString(text.toString, origoX, y)
  }
}