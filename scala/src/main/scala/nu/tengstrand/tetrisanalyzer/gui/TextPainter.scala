package nu.tengstrand.tetrisanalyzer.gui

import java.awt.{Color, Font, Graphics2D}

class TextPainter {
  private var origoX = 0
  private var origoY = 10
  private var spacing = 14

  private var textFont = defaultFont

  def setDefaultFont(g: Graphics2D) { textFont = defaultFont; g.setFont(textFont)  }
  def setMediumFont(g: Graphics2D) { textFont = new Font("Monospaced", Font.PLAIN, 14); g.setFont(textFont) }
  def setBigFont(g: Graphics2D) { textFont = new Font("Monospaced", Font.PLAIN, 18); g.setFont(textFont) }

  private def defaultFont = new Font("Monospaced", Font.PLAIN, 12)

  def prepareDrawText(origoX: Int, g: Graphics2D, origoY: Int = 10, color: Color = Color.BLACK) {
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

  def drawText(text: Any, pixelX: Int, pixelY: Int, g: Graphics2D) {
    g.drawString(text.toString, origoX + pixelX, pixelY)
  }

  def getY(row: Int) = origoY + row * spacing
}