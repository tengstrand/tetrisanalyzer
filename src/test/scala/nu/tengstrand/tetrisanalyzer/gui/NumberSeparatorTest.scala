package nu.tengstrand.tetrisanalyzer.gui

import nu.tengstrand.tetrisanalyzer.BaseTest
import org.junit.Test

class NumberSeparatorTest extends BaseTest {

  @Test def separateOneDigit() {
    val numberSeparator = new NumberSeparator
    numberSeparator.withSpaces(1) should be ("1")
  }

  @Test def separateTwoDigit() {
    val numberSeparator = new NumberSeparator
    numberSeparator.withSpaces(12) should be ("12")
  }

  @Test def separateThreeDigit() {
    val numberSeparator = new NumberSeparator
    numberSeparator.withSpaces(123) should be ("123")
  }

  @Test def separateFourDigits() {
    val numberSeparator = new NumberSeparator
    numberSeparator.withSpaces(1234) should be ("1 234")
  }

  @Test def separateEightDigits() {
    val numberSeparator = new NumberSeparator
    numberSeparator.withSpaces(12345678) should be ("12 345 678")
  }
}