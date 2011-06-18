package nu.tengstrand.tetrisanalyzer.game

import swing.UIElement

class Timer(mainFrame: UIElement, gameInfoReceiver: GameInfoReceiver) {
  var sleepMs = 20 // 50 times per second
  var paused: Boolean = true

  private var timeStarted = 0L;
  private var totalTimeMs = 0L;

  def setPaused(paused: Boolean) {
    this.paused = paused

    val currentTimeMs = System.currentTimeMillis

    if (paused) {
      if (timeStarted != 0) {
        totalTimeMs += currentTimeMs - timeStarted
      }
    } else {
      timeStarted = currentTimeMs
    }
  }

  private def secondsPassed = {
    if (paused)
      totalTimeMs / 1000.0;
    else
      (totalTimeMs + System.currentTimeMillis - timeStarted) / 1000.0
  }

  def start() {
    while (true) {
      gameInfoReceiver.setTimePassed(secondsPassed)
      mainFrame.repaint
      Thread.sleep(sleepMs)
    }
  }

  def reset() {
    timeStarted = System.currentTimeMillis
    totalTimeMs = 0
  }
}