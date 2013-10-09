package nu.tengstrand.tetrisanalyzer.gui.help

import java.awt.Graphics2D

trait HelpPainter {
  def paintGraphics(origoX: Int, g: Graphics2D)
}