package nu.tengstrand.tetrisanalyzer.gui.rankedmove

import nu.tengstrand.tetrisanalyzer.move.MoveEquity

class RankedMoves(equityMoves: List[MoveEquity], maxX: Int, maxY: Int) extends AlignRight {
  val moves = calculateRankedMoves

  var selectedRow = 0

  private case class VX(rotation:Int, x:Int)

  private def vxList = equityMoves.map(m => VX(m.pieceMove.move.rotation, m.pieceMove.move.x))

  private def calculateRankedMoves = {
    var rankedMoves = List.empty[RankedMove]
    var row = 1
    val hasDuplicates = hasDuplicatedVX

    for (moveEquity <- equityMoves) {
      val rankedMove = new RankedMove(row, moveEquity, maxX, maxEquity)

      if (!hasDuplicates)
        rankedMoves = rankedMove :: rankedMoves
      else {
        val isDuplicatedVX = vxList.count(_.equals(VX(moveEquity.pieceMove.move.rotation, moveEquity.pieceMove.move.x))) > 1
        rankedMoves = rankedMove.withYColumn(maxY, isDuplicatedVX) :: rankedMoves
      }
      row += 1
    }
    rankedMoves.reverse
  }

  def selectedMove = moves(selectedRow)

  def selectPreviousMove() {
    if (selectedRow > 0)
      selectedRow -= 1
  }

  def selectNextMove() {
    if (selectedRow < moves.size - 1)
      selectedRow += 1
  }

  def headerAsText = {
    var x = alignRight("x", maxX.toString.length()) + " "
    var y = if (hasDuplicatedVX) alignRight("y", maxY.toString.length()) + "  " else " "

    "    v " + x + y + "Depth 0"
  }

  def size = equityMoves.size

  def hasDuplicatedVX = {
    var hasDuplicates = false

    for (vx <- vxList)
      if (vxList.count(_.equals(vx)) > 1)
        hasDuplicates = true

    hasDuplicates
  }

  def maxEquity = equityMoves.map(_.equity).max
}