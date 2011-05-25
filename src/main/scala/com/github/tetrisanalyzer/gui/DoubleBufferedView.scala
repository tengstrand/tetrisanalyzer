package com.github.tetrisanalyzer.gui

import swing.Component
import java.awt._

/**
 * Use this component when you want flicker free painting.
 */
abstract class DoubleBufferedView extends Component {
  private var image: Image = _

  def preparePaintGraphics: Dimension
  def paintGraphics(g: Graphics)

  override def paintComponent(g: Graphics2D) = {
    super.paintComponent(g);

    val paintArea = preparePaintGraphics

    if (!isZeroSize(paintArea)) {
      paintOffscreenImage(paintArea, g)
    }
  }

  private def paintOffscreenImage(paintArea: Dimension, g: Graphics) {
    initializeOffscreenImageIfNeeded(paintArea)

    val imageGraphics: Graphics = image.getGraphics()
    paintBackground(imageGraphics)

    paintGraphics(imageGraphics)

    g.drawImage(image, 0, 0, null)
  }

  private def isZeroSize(area: Dimension) = {
    area.width <= 0 || area.height <= 0
  }

  def paintBackground(g: Graphics) {
    g.setColor(this.background);
    g.fillRect(0,0, this.size.width,this.size.height)
  }

  private def initializeOffscreenImageIfNeeded(area: Dimension) {
    if (image == null || image.getWidth(null) != area.width || image.getHeight(null) != area.height) {
      image = new java.awt.image.BufferedImage(area.width, area.height, java.awt.image.BufferedImage.TYPE_INT_RGB)
    }
  }
}