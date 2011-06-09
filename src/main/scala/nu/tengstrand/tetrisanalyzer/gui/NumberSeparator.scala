package nu.tengstrand.tetrisanalyzer.gui

class NumberSeparator {

  /**
   * Separate numbers with spaces, e.g:
   *   12345678 will be 12 345 678.
   */
  def withSpaces(number: Long) = {
    val stringNumber = number.toString
    var result = ""

    val endIndex = stringNumber.length-1

    for (i <- 0 to endIndex)
      result += stringNumber(i) + (if ((endIndex-i)%3 == 0 && endIndex-i >= 3) " " else "")

    result
  }
}