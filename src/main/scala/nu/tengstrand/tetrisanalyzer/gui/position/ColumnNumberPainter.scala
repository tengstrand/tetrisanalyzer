package nu.tengstrand.tetrisanalyzer.gui.position

import java.awt.{Color, Graphics2D}
import nu.tengstrand.tetrisanalyzer.game.{ColoredPosition, Wall}
import nu.tengstrand.tetrisanalyzer.gui.FontChooser

class ColumnNumberPainter {
  private val fontChooser = new FontChooser

  def paintNumbers(position: ColoredPosition, rows: Seq[Int], columns: Seq[Int], g: Graphics2D) {
    g.setColor(Color.BLACK)

    val squareHeight = (rows(position.height-1) - rows(position.height-2))

    fontChooser.setFont(squareHeight, g)

    val y = rows(position.height-2) + (squareHeight-1) / 2 + 5

    for (number <- 1 to position.width - Wall.Left - Wall.Right) {
      val column = number + Wall.Left - 1
      val squareWidth = columns(column + 1) - columns(column)
      val x = columns(column) + squareWidth / 2 + (if (number < 10) -2 else -5)
      g.drawString(number.toString, x, y)
    }
  }

}