package nu.tengstrand.tetrisanalyzer.gui.rankedmove

import nu.tengstrand.tetrisanalyzer.move.{Move, MoveEquity}

class RankedMoves(maxX: Int, maxY: Int) extends AlignRight {
  var moves: List[RankedMove] = List.empty[RankedMove]
  var equityMoves = List.empty[MoveEquity]

  var selectedRow = 0

  private case class VX(rotation:Int, x:Int)

  private def vxList = equityMoves.map(m => VX(m.pieceMove.move.rotation, m.pieceMove.move.x))

  private def calculateRankedMoves(equityMoves: List[MoveEquity]) = {
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

  def setMoves(equityMoves: List[MoveEquity], keepSelectedMove: Boolean = false): Unit = {
    this.equityMoves = equityMoves
    if (keepSelectedMove && moves.size > 0) {
      val selectedMove = moves(selectedRow)
      moves = calculateRankedMoves(equityMoves)
      selectMove(selectedMove.moveEquity.pieceMove.move)
    } else {
      moves = calculateRankedMoves(equityMoves)
      selectedRow = 0
    }
  }

  private def selectMove(move: Move): Unit = {
    selectedRow = moves.map(_.moveEquity.pieceMove.move).indexWhere(m => { m == move } )
    if (selectedRow < 0)
      selectedRow = 0
  }

  def isEmpty = moves.size == 0

  def selectedPieceMove = moves(selectedRow).moveEquity.pieceMove

  def selectPreviousMove(): Unit = {
    if (selectedRow > 0)
      selectedRow -= 1
    else
      selectedRow = moves.size - 1
  }

  def selectNextMove(): Unit = {
    if (selectedRow < moves.size - 1)
      selectedRow += 1
    else
      selectedRow = 0
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