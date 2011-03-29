package com.github.tetrisanalyzer.piecemove

import org.junit.Test
import collection.mutable.{HashSet, Set}
import com.github.tetrisanalyzer.BaseTest
import com.github.tetrisanalyzer.board.Board
import com.github.tetrisanalyzer.move.Move
import com.github.tetrisanalyzer.piece.{PieceS, PieceO}
import com.github.tetrisanalyzer.settings.{GameSettingsSlidingOn, DefaultGameSettings}

class ValidPieceMovesTest extends BaseTest {

  @Test def getStartMove {
    val board = Board(5,5)
    val piece = new PieceO
    val validPieceMoves = new ValidPieceMovesForEmptyBoard(board, piece, new DefaultGameSettings)
    val startMove = validPieceMoves.startMove

    startMove should be (PieceMove(board, piece, Move(0,1, 0)))

    startMove.asideAndRotate should be (Set(
      PieceMove(board, piece, Move(0,0, 0)),
      PieceMove(board, piece, Move(0,2, 0))
    ))

    startMove.down should be (PieceMove(board, piece, Move(0,1, 1)))
  }

  @Test def canMoveDown {
    startMoveForDown.canMoveDown should be (true)
  }

  @Test def canMoveDown_occupied {
    startMoveForDown.down.canMoveDown should be (false)
  }

  private def startMoveForDown = {
    val board = Board(Array(
      "#--------#",
      "#--------#",
      "#--------#",
      "#--xxxx--#",
      "##########"))

    val piece = new PieceS
    val validPieceMoves = new ValidPieceMovesForEmptyBoard(board, piece, new DefaultGameSettings)
    validPieceMoves.startMove
  }

  @Test def freeAsideAndRotateMoves {
    val board = startBoardForAsideAndRotate
    val piece = new PieceS
    val validPieceMoves = new ValidPieceMovesForEmptyBoard(board, piece, new DefaultGameSettings)
    val startMove = validPieceMoves.startMove

    startMove.freeAsideAndRotateMoves should be (Set(
      PieceMove(board, piece, Move(0,4, 0)),
      PieceMove(board, piece, Move(1,3, 0))))
  }

  private def startBoardForAsideAndRotate = {
    Board(Array(
      "#--------#",
      "#xxx----x#",
      "#xxx----x#",
      "#xxx----x#",
      "##########"))
  }

  @Test def slidingOn {
    val board = slidingBoard
    val piece = new PieceS
    val validPieceMoves = new ValidPieceMovesForEmptyBoard(board, piece, new GameSettingsSlidingOn)
    val startMove = validPieceMoves.startMove

    asMoves(startMove) should be (List(
      Move(0,0, 1),
      Move(1,0, 0),
      Move(0,1, 1),
      Move(0,1, 3), // slided
      Move(1,1, 1),
      Move(0,2, 3),
      Move(1,2, 2),
      Move(0,3, 3),
      Move(1,3, 2),
      Move(0,4, 3),
      Move(1,4, 2),
      Move(0,5, 3),
      Move(1,5, 2),
      Move(1,6, 2)
    ))
  }

  @Test def slidingOff {
    val board = slidingBoard
    val piece = new PieceS
    val validPieceMoves = new ValidPieceMovesForEmptyBoard(board, piece, new DefaultGameSettings)
    val startMove = validPieceMoves.startMove

    asMoves(startMove) should be (List(
      Move(0,0, 1),
      Move(1,0, 0),
      Move(0,1, 1),
      Move(1,1, 1),
      Move(0,2, 3),
      Move(1,2, 2),
      Move(0,3, 3),
      Move(1,3, 2),
      Move(0,4, 3),
      Move(1,4, 2),
      Move(0,5, 3),
      Move(1,5, 2),
      Move(1,6, 2)
    ))
  }

  private def slidingBoard = {
    Board(Array(
      "#--------#",
      "#--------#",
      "#--------#",
      "#xx------#",
      "#x-------#",
      "##########"))
  }

  private def asMoves(pieceMove: PieceMove): List[Move] = {
    var moves = new HashSet[Move]
    var visitedMoves = new HashSet[Move]
    asMoves(pieceMove, moves, visitedMoves)
    moves.toList.sortBy{m => (m.x, m.rotation, m.y)}
  }

  private def asMoves(pieceMove: PieceMove, moves: Set[Move], visitedMoves: Set[Move]) {
    if (!visitedMoves.contains(pieceMove.move)) {
      visitedMoves += pieceMove.move
      pieceMove.freeAsideAndRotateMoves.foreach(move => asMoves(move, moves, visitedMoves))
      if (pieceMove.canMoveDown)
        asMoves(pieceMove.down, moves, visitedMoves)
      else
        moves += pieceMove.move
    }
  }

}