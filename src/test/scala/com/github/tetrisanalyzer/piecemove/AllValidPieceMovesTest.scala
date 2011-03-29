package com.github.tetrisanalyzer.piecemove

import com.github.tetrisanalyzer.BaseTest
import org.junit.Test
import com.github.tetrisanalyzer.board.Board
import com.github.tetrisanalyzer.piece.PieceT
import com.github.tetrisanalyzer.move.Move
import com.github.tetrisanalyzer.settings.DefaultGameSettings

class AllValidPieceMovesTest extends BaseTest {

  @Test def startMoveForPiece {
    val board = Board(6,6)
    val piece = new PieceT
    val settings = new DefaultGameSettings
    val allValidPieceMoves = new AllValidPieceMovesForEmptyBoard(board, settings)

    val startMove = allValidPieceMoves.startMoveForPiece(piece)

    startMove should be (PieceMove(board, piece, Move(0,2, 0)))
  }
}