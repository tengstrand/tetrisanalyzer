package nu.tengstrand.tetrisanalyzer.game

import nu.tengstrand.tetrisanalyzer.BaseTest
import org.junit.Test
import nu.tengstrand.tetrisanalyzer.settings.DefaultGameSettings
import nu.tengstrand.tetrisanalyzer.move.Move
import nu.tengstrand.tetrisanalyzer.piece.{PieceO, PieceI, PieceT, PieceS}
import startpiece.StartPiece

class PositionTest extends BaseTest {

  @Test def testToString(): Unit = {
    val position = Position();

    position.toString should be (
      "######----------##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "##################\n" +
      "##################"
    )
  }

  @Test def setStartPieceIfFree(): Unit = {
    val position = Position(10,5);
    val startPiece = new StartPiece(PieceS())
    position.setStartPieceIfFree(startPiece, new DefaultGameSettings)

    position.toString should be (
      "######-----SS---##\n" +
      "######----SS----##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "##################\n" +
      "##################"
    )
  }

  @Test def setOrRestoreNextPiece(): Unit = {
    val position = Position(10,5);
    val startPiece = new StartPiece(PieceS(), Some(PieceT()))
    position.setOrRemoveNextPiece(startPiece)

    position.toString should be (
      "-TTT-#----------##\n" +
      "--T--#----------##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "##################\n" +
      "##################"
    )
  }

  @Test def setPiece(): Unit = {
    val position = Position(10,5);
    position.setPiece(PieceT(), Move(1, 5, 2))

    position.toString should be (
      "######----------##\n" +
      "######----------##\n" +
      "######-----T----##\n" +
      "######-----TT---##\n" +
      "######-----T----##\n" +
      "##################\n" +
      "##################"
    )
  }

  @Test def clearRows(): Unit = {
    // Before:
    // ######---TTT----##
    // ######----T-----##
    // ######IIIIIIIIOO##
    // ######IIIIIIIIOO##
    // ######----OO----##
    // ######IIIIOOIIII##
    // ##################
    // ##################

    val position = Position(10,6);
    position.setPiece(PieceT(), Move(0, 3, 0))
    position.setPiece(PieceI(), Move(0, 0, 5))
    position.setPiece(PieceO(), Move(0, 4, 4))
    position.setPiece(PieceI(), Move(0, 6, 5))
    position.setPiece(PieceI(), Move(0, 0, 3))
    position.setPiece(PieceI(), Move(0, 4, 3))
    position.setPiece(PieceO(), Move(0, 8, 2))
    position.setPiece(PieceI(), Move(0, 0, 2))
    position.setPiece(PieceI(), Move(0, 4, 2))

    position.clearRows(2, 4) should be (3)

    position.toString should be (
      "######----------##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "######---TTT----##\n" +
      "######----T-----##\n" +
      "######----OO----##\n" +
      "##################\n" +
      "##################"
    )
  }

  @Test def copyConstructor(): Unit = {
    val position = Position(10,5);
    position.setPiece(PieceT(), Move(1, 5, 2))

    val copyPosition = Position(position)

    copyPosition.toString should be (
      "######----------##\n" +
      "######----------##\n" +
      "######-----T----##\n" +
      "######-----TT---##\n" +
      "######-----T----##\n" +
      "##################\n" +
      "##################"
    )
  }
}