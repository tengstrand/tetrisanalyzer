package nu.tengstrand.tetrisanalyzer

object Stopwatch {
  var timer1 = new Stopwatch("timer1")

  def printAllResult(): Unit = {
    timer1.printResult
  }
}

class Stopwatch(message: String) {
  private var startMillis: Long = 0L
  private var passed: Long = 0

  def start: Unit = {
    startMillis = System.currentTimeMillis
  }

  def stop: Unit = {
    var stop: Long = System.currentTimeMillis
    passed += (stop - startMillis)
    startMillis = stop
  }

  def printResult: Unit = {
    System.out.println(message + ": " + (passed / 1000.0) + " sec")
  }
}