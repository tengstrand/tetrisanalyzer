package nu.tengstrand.tetrisanalyzer.settings

class SpecifiedGameSettings(slidingEnabled: Boolean, seed: Long) extends DefaultGameSettings {
  override def isSlidingEnabled = slidingEnabled
  override def pieceGeneratorSeed = seed
}