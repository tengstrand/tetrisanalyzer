package nu.tengstrand.tetrisanalyzer.game

import nu.tengstrand.tetrisanalyzer.BaseTest
import org.junit.Test
import nu.tengstrand.tetrisanalyzer.settings.DefaultGameSettings
import nu.tengstrand.tetrisanalyzer.move.Move
import nu.tengstrand.tetrisanalyzer.piece.{PieceO, PieceI, PieceT, PieceS}

class PositionTest extends BaseTest {

  @Test def testToString() {
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

  @Test def testSetStartPieceIfFree() {
    val position = Position(10,5);
    position.setStartPieceIfFree(new PieceS, new DefaultGameSettings)

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

  @Test def testSetPiece() {
    val position = Position(10,5);
    position.setPiece(new PieceT, Move(1, 5, 2))

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

  @Test def clearRows() {
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
    position.setPiece(new PieceT, Move(0, 3, 0))
    position.setPiece(new PieceI, Move(0, 0, 5))
    position.setPiece(new PieceO, Move(0, 4, 4))
    position.setPiece(new PieceI, Move(0, 6, 5))
    position.setPiece(new PieceI, Move(0, 0, 3))
    position.setPiece(new PieceI, Move(0, 4, 3))
    position.setPiece(new PieceO, Move(0, 8, 2))
    position.setPiece(new PieceI, Move(0, 0, 2))
    position.setPiece(new PieceI, Move(0, 4, 2))

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

  @Test def copyConstructor {
    val position = Position(10,5);
    position.setPiece(new PieceT, Move(1, 5, 2))

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