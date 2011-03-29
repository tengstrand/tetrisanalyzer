package com.github.tetrisanalyzer.gui

import java.awt.{Color, Dimension, Graphics}

class Playfield extends DoubleBufferedComponent {
  val margin = 10
  var rows = Seq.empty[Int]
  var columns = Seq.empty[Int]

  def preparePaintGraphics = {
    val squareSize = calculateSquareSize
    rows = (0 to 20).map(y => margin + (y * squareSize).intValue)
    columns = (0 to 10).map(x => margin + (x * squareSize).intValue)

    new Dimension(columns(10)-columns(0)+margin-1, rows(20)+margin-rows(0)-1)
  }

  def paintGraphics(g: Graphics) {
    for (iy <- 0 to 19)
      for (ix <- 0 to 9)
        drawSquare(columns(ix), rows(iy), columns(ix+1), rows(iy+1), g)
  }

  def drawSquare(x1: Int, y1: Int, x2: Int, y2: Int, g: Graphics) {
    g.setColor(new Color(200, 200, 230))
    g.fillRect(x1, y1, x2, y2)
    g.setColor(new Color(230, 230, 255))
    g.drawLine(x1, y1, x1, y2)
    g.drawLine(x1, y1, x2, y1)
  }

  private def calculateSquareSize: Double = {
    val width = this.size.width - 20.0
    val height = this.size.height - 20.0

    if (width <= 0 || height <= 0)
      0

    if (height / width > 2)
      width / 10
    else
      height / 20
  }
}