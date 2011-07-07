package nu.tengstrand.tetrisanalyzer.gui.rankedmove

trait AlignRight {
  def alignRight(text: String, width: Int) = {
    if (text.length >= width) text else (" " * (width-text.length)) + text
  }
}