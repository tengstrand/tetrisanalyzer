package nu.tengstrand.tetrisanalyzer.gui.position

class SquareSizeCalculator {

  def calculateSquareSize(smallBoard: Boolean, pixelsWidth: Int, pixelsHeight: Int, numberOfSquaresX: Int, numberOfSquaresY: Int): Double = {
    if (smallBoard)
      6
    else {
      if (pixelsWidth <= 0 || pixelsHeight <= 0 || numberOfSquaresX == 0 || numberOfSquaresY == 0)
        0
      else {
        val heightSize = pixelsHeight.toDouble / numberOfSquaresY
        val widthSize = pixelsWidth.toDouble / numberOfSquaresX

        if (heightSize < widthSize)
          heightSize
        else
          widthSize
      }
    }
  }
}
