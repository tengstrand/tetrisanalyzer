package nu.tengstrand.tetrisanalyzer.gui

import nu.tengstrand.tetrisanalyzer.settings.ColorSettings

class GameView(colorSettings: ColorSettings) extends PositionView(colorSettings) with GameInfoView {
  private var paused = false

  def isPaused: Boolean = paused
  def setPaused(paused: Boolean) { this.paused = paused }
}