package com.github.tetrisanalyzer.piecegenerator

object DefaultPieceGenerator {
  val BitMask = 0x00000000FFFFFFFFL
}

/**
 * This piece generator mimics the behaviour of the C++ version,
 * that uses 32 bit unsigned integers.
 */
class DefaultPieceGenerator(var seed: Long = 0) extends PieceGenerator {

  def nextPieceNumber = {
    seed = (seed * 1664525) & DefaultPieceGenerator.BitMask
    seed = (seed + 1013904223) & DefaultPieceGenerator.BitMask

    modulus7 + 1
  }

  private def modulus7 = {
    val div: Long = seed / 7;
    (seed - div * 7).toInt
  }
}
