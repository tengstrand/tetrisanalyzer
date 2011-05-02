package com.github.tetrisanalyzer

object Timer {
  var timer1 = new Timer("timer1")

  def printAllResult() {
    timer1.printResult
  }
}

class Timer(message: String) {
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