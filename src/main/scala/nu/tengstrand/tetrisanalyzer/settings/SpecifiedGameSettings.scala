package nu.tengstrand.tetrisanalyzer.settings

class SpecifiedGameSettings(slidingEnabled: Boolean) extends DefaultGameSettings {
  override def isSlidingEnabled = slidingEnabled
}