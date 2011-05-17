package com.github.tetrisanalyzer.game

/**
 * Call it like this, e.g:
 *  fiftyTimesPerSecond(() =>
 *    println("Hello!")
 *  )
 */
object Timer {
  def fiftyTimesPerSecond(callback: () => Unit): Unit =
  {
    while (true) {
      callback()
      Thread.sleep(20)
    }
  }
}