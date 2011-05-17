package com.github.tetrisanalyzer.piece

object Piece {
  val NumberOfPieceTypes = 7
  private val RotationModulus: Array[Int] = Array(0, 1, 0, 3)
  private val Pieces: Array[Piece] = Array(
    new PieceEmpty, new PieceI, new PieceZ, new PieceS, new PieceJ, new PieceL, new PieceT, new PieceO, new PieceAny
  )
  private val Characters = Array.tabulate(NumberOfPieceTypes + 2) {
      ((i) => (Piece(i).character.toString))
  }

  private val Numbers = Map (
    Piece(0).character -> Piece(0).number,
    Piece(1).character -> Piece(1).number,
    Piece(2).character -> Piece(2).number,
    Piece(3).character -> Piece(3).number,
    Piece(4).character -> Piece(4).number,
    Piece(5).character -> Piece(5).number,
    Piece(6).character -> Piece(6).number,
    Piece(7).character -> Piece(7).number,
    Piece(8).character -> Piece(8).number
  )

  def apply(index: Int): Piece = Pieces(index)
  def toChar(number: Int) = Characters(number)
  def toNumber(character: Char) = Numbers(character)
}

/**
 * Represents one of the pieces: I, Z, S, J, L, T or O.
 */
abstract class Piece {
  def number: Byte
  def character: Char
  def rotationsEndIndex = heights.length - 1
  def rotationModulus = Piece.RotationModulus(rotationsEndIndex)
  def width(rotation: Int) = widths(rotation)
  def height(rotation: Int) = heights(rotation)
  def shape(rotation: Int) = shapes(rotation)
  protected def widths: Array[Int]
  protected def heights: Array[Int]
  protected def shapes: Array[PieceShape]

  override def hashCode = number

  override def equals(that: Any) = that match {
    case other: Piece => number == other.number
    case _ => false
  }

  override def toString = character.toString
}
