package nu.tengstrand.tetrisanalyzer.gui

import java.awt.{Color, Font, Graphics2D}

class TextDrawer {
  private var origoX = 0
  private var origoY = 10
  private var spacing = 14

  private val textFont = new Font("Monospaced", Font.PLAIN, 12);

  def prepareDraw(origoX: Int, g: Graphics2D, origoY: Int = 10, color: Color = Color.BLACK) {
    this.origoX = origoX
    this.origoY = origoY
    g.setFont(textFont);
    g.setColor(color);
  }

  def drawInfo(label: Any, value: Any, row: Int, g: Graphics2D) {
    drawText(label + " " + value, row, g)
  }

  def drawText(text: Any, row: Int, g: Graphics2D) {
    val y = getY(row)
    g.drawString(text.toString, origoX, y)
  }

  def getY(row: Int) = origoY + row * spacing
}