package nu.tengstrand.tetrisanalyzer.gui

import swing.Component
import java.awt._

/**
 * Use this component when you want flicker free painting.
 */
abstract class DoubleBufferedView extends Component {
  private var image: Image = _

  def preparePaintGraphics: Dimension
  def paintGraphics(g: Graphics): Unit

  override def paintComponent(g: Graphics2D): Unit = {
    super.paintComponent(g);

    val paintArea = preparePaintGraphics

    if (!isZeroSize(paintArea)) {
      paintOffscreenImage(paintArea, g)
    }
  }

  private def paintOffscreenImage(paintArea: Dimension, g: Graphics): Unit = {
    initializeOffscreenImageIfNeeded(paintArea)

    val imageGraphics: Graphics = image.getGraphics

    paintGraphics(imageGraphics)

    g.drawImage(image, 0, 0, null)
  }

  private def isZeroSize(area: Dimension) = {
    area.width <= 0 || area.height <= 0
  }

  private def initializeOffscreenImageIfNeeded(area: Dimension): Unit = {
    if (image == null || image.getWidth(null) != area.width || image.getHeight(null) != area.height) {
      image = new java.awt.image.BufferedImage(area.width, area.height, java.awt.image.BufferedImage.TYPE_INT_RGB)
    }
  }
}