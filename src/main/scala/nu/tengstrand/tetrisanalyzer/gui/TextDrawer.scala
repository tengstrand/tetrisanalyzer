package nu.tengstrand.tetrisanalyzer.gui

import java.awt.{Color, Font, Graphics2D}

class TextDrawer {
  private var origoX = 0

  private val textFont = new Font("Monospaced", Font.PLAIN, 14);

  def prepareDraw(origoX: Int, g: Graphics2D) {
    this.origoX = origoX
    g.setFont(textFont);
    g.setColor(Color.BLACK);
  }

  def drawText(text: String, row: Int, g: Graphics2D) {
    drawInfo(text, "", row, g)
  }

  def drawInfo(label: String, value: Any, row: Int, g: Graphics2D) {
    val y = 10 + row * 20
    g.drawString(label, origoX, y)
    drawValue(value.toString, y, g)
  }

  def drawValue(value: String, y: Int, g: Graphics2D) {
    g.drawString(value, 240 - value.length * 8 + origoX, y)
  }
}