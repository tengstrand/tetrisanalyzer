package com.github.tetrisanalyzer.gui

import java.awt.{Color, Dimension, Graphics}
import com.github.tetrisanalyzer.settings.GameSettings
import com.github.tetrisanalyzer.game.{PositionModel, Position, PlayerEventReceiver}

class PositionView(settings: GameSettings) extends DoubleBufferedComponent with PlayerEventReceiver {
  private val margin = 10
  private var rows = Seq.empty[Int]
  private var columns = Seq.empty[Int]
  private var positionModel: PositionModel = null

  private var receivePosition = true

  def readyToReceivePosition = receivePosition || isStepMode
  private var isStepMode = false

  def toggleStepMode() { isStepMode = !isStepMode }

  def positionReceived(positionModel: PositionModel) {
    receivePosition = false
    this.positionModel = positionModel
  }

  override def preparePaintGraphics = {
    if (positionModel != null) {
      val squareSize = calculateSquareSize
      rows = (0 to positionModel.height).map(y => margin + (y * squareSize).intValue)
      columns = (0 to positionModel.width).map(x => margin + (x * squareSize).intValue)

      new Dimension(columns(positionModel.width)-columns(0)+margin, rows(positionModel.height)+margin-rows(0))
    } else
      new Dimension(0,0)
  }

  override def paintGraphics(g: Graphics) {
    if (positionModel != null) {
      for (iy <- 0 until positionModel.height) {
        for (ix <- 0 until positionModel.width) {
          val color = positionModel.colorValue(ix, iy)
          drawSquare(columns(ix), rows(iy), columns(ix+1), rows(iy+1),
            settings.squareColor(color),
            settings.lineColor(color), g)
        }
      }
      g.drawLine(columns(positionModel.width)-1, rows(0), columns(positionModel.width)-1, rows(positionModel.height))
      g.drawLine(columns(0), rows(positionModel.height)-1, columns(positionModel.width), rows(positionModel.height)-1)
      receivePosition = true
    }
  }

  def drawSquare(x1: Int, y1: Int, x2: Int, y2: Int, squareColor: Color, lineColor: Color, g: Graphics) {
    g.setColor(squareColor)
    g.fillRect(x1, y1, x2, y2)
    g.setColor(lineColor)
    g.drawLine(x1, y1, x1, y2)
    g.drawLine(x1, y1, x2, y1)
  }

  private def calculateSquareSize: Double = {
    val width = this.size.width - 20.0
    val height = this.size.height - 20.0

    if (width <= 0 || height <= 0 || positionModel.height == 0|| positionModel.width == 0)
      0

    if (height / width > 1.2)
      width / positionModel.width
    else
      height / positionModel.height
  }
}