package nu.tengstrand.tetrisanalyzer.game.startpiece

import nu.tengstrand.tetrisanalyzer.BaseTest
import org.junit.Test
import nu.tengstrand.tetrisanalyzer.piece.{PieceL, PieceT}

class StartPieceTest extends BaseTest {

  @Test def hasNext_onePiece(): Unit = {
    val startPiece = new StartPiece(PieceT())
    startPiece.nextPiece()
    startPiece.hasNext should be (false)
  }

  @Test def hasNext_twoPieces(): Unit = {
    val startPiece = new StartPiece(PieceT(), Some(PieceT()))
    startPiece.hasNext should be (true)
  }

  @Test def nextOnePiece(): Unit = {
    val startPiece = new StartPiece(PieceT())
    startPiece.nextPiece() should be (PieceT())
  }

  @Test def nextTwoPieces(): Unit = {
    val startPiece = new StartPiece(PieceT(), Some(PieceL()))
    startPiece.nextPiece() should be (PieceT())
    startPiece.nextPiece() should be (PieceL())
  }
}