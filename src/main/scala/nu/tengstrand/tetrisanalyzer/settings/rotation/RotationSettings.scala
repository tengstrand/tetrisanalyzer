package nu.tengstrand.tetrisanalyzer.settings.rotation

import nu.tengstrand.tetrisanalyzer.piece.Piece
import nu.tengstrand.tetrisanalyzer.move.rotation.RotationDirection

trait RotationSettings {
  private val adjustmentsDelta = calculateAdjustmentsDelta

  def rotationDirection: RotationDirection

  /**
   * Returns the x and y delta adjustment for a given piece and rotation.
   */
  def rotationAdjustment(piece: Piece, fromRotation: Int) = {
    adjustmentsDelta(rotationDirection.index)(piece.number)(fromRotation)
  }

  /**
   * X and y adjustments for all pieces and their valid rotations.
   */
  def rotationAdjustments: Array[Array[MoveAdjustment]]

  /**
   * Converts an array of tuples to an array of MoveAdjustments,
   * added to increase the readability of the code.
   */
  protected def rotationAdjustment(adjust: Tuple2[Int, Int]*): Array[MoveAdjustment] = {
    Array.tabulate(adjust.size) (
      (i) => MoveAdjustment(adjust(i)._1, adjust(i)._2)
    )
  }

  /**
   * Calculates the move adjustment delta for all pieces for every given
   * rotation type (anticlockwise/clockwise), piece and move rotation.
   */
  private def calculateAdjustmentsDelta: Array[Array[Array[MoveAdjustment]]] = {
    val direction = Array(1, -1)

    Array.tabulate(RotationDirection.NumberOfDirections, rotationAdjustments.size) (
      (rotation,pieceNumber) => {
        val adjustments = rotationAdjustments(pieceNumber)
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