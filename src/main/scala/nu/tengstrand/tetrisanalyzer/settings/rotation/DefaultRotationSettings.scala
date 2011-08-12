package nu.tengstrand.tetrisanalyzer.settings.rotation

import nu.tengstrand.tetrisanalyzer.piece._
import nu.tengstrand.tetrisanalyzer.move.rotation.{AnticlockwiseRotation, RotationDirection}

object Adjustment {
  def adjustment(adjust: Tuple2[Int, Int]*) = {
    Array.tabulate(adjust.size) (
      (i) => MoveAdjustment(adjust(i)._1, adjust(i)._2)
    )
  }
}

trait DefaultRotationSettings extends RotationSettings {
  private val adjustmentsDelta = calculateAdjustmentsDelta

  val rotationDirection = new AnticlockwiseRotation

  def adjustment(piece: Piece, fromRotation: Int) = {
    adjustmentsDelta(rotationDirection.index)(piece.number)(fromRotation)
  }

  private def calculateAdjustmentsDelta = {
      val pieceAdjustments = Array(
      Adjustment.adjustment((0,0)), // 0 = empty
      Adjustment.adjustment((0,0), (1,0)), // 1 = I
      Adjustment.adjustment((0,0), (0,0)), // 2 = Z
      Adjustment.adjustment((0,0), (0,0)), // 3 = S
      Adjustment.adjustment((0,0), (0,0), (0,0), (0,0)), // 4 = J
      Adjustment.adjustment((0,0), (0,0), (0,0), (0,0)), // 5 = L
      Adjustment.adjustment((0,0), (0,0), (0,0), (0,0)), // 6 = T
      Adjustment.adjustment((0,0)), // 7 = O
      Adjustment.adjustment((0,0)) // 8 = any
    )

    val direction = Array(1, -1)
    Array.tabulate(RotationDirection.NumberOfDirections, pieceAdjustments.size) (
      (rotation,pieceNumber) => {
        val adjustments = pieceAdjustments(pieceNumber)
        val rotationModulus = adjustments.size - 1

        Array.tabulate(adjustments.size) (
          (rotationIndex) => {
            val toRotationIndex = (rotationIndex + direction(rotation)) & rotationModulus
            adjustments(toRotationIndex) - adjustments(rotationIndex)
          }
        )
      }
    )
  }
}
