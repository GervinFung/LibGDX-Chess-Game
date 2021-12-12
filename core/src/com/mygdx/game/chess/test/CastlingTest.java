package com.mygdx.game.chess.test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import com.mygdx.game.chess.engine.FEN.FenUtilities;
import com.mygdx.game.chess.engine.board.Board;
import com.mygdx.game.chess.engine.board.BoardUtils;
import com.mygdx.game.chess.engine.board.Move;
import com.mygdx.game.chess.engine.board.MoveTransition;

import org.junit.Test;

public final class CastlingTest {

    @Test
    public void testWhiteKingSideCastle() {
        final Board board = Board.createStandardBoardWithDefaultTimer();
        final MoveTransition t1 = board.currentPlayer().makeMove(Move.MoveFactory.createMove(board, BoardUtils.getPieceAtPosition(board, "e2"), BoardUtils.getCoordinateAtPosition("e4")));
        assertTrue(t1.getMoveStatus().isDone());
        final MoveTransition t2 = t1.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t1.getLatestBoard(), BoardUtils.getPieceAtPosition(t1.getLatestBoard(), "e7"), BoardUtils.getCoordinateAtPosition("e5")));
        assertTrue(t2.getMoveStatus().isDone());
        final MoveTransition t3 = t2.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t2.getLatestBoard(), BoardUtils.getPieceAtPosition(t2.getLatestBoard(), "g1"), BoardUtils.getCoordinateAtPosition("f3")));
        assertTrue(t3.getMoveStatus().isDone());
        final MoveTransition t4 = t3.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t3.getLatestBoard(), BoardUtils.getPieceAtPosition(t3.getLatestBoard(), "d7"), BoardUtils.getCoordinateAtPosition("d6")));
        assertTrue(t4.getMoveStatus().isDone());
        final MoveTransition t5 = t4.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t4.getLatestBoard(), BoardUtils.getPieceAtPosition(t4.getLatestBoard(), "f1"), BoardUtils.getCoordinateAtPosition("e2")));
        assertTrue(t5.getMoveStatus().isDone());
        final MoveTransition t6 = t5.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t5.getLatestBoard(), BoardUtils.getPieceAtPosition(t5.getLatestBoard(), "d6"), BoardUtils.getCoordinateAtPosition("d5")));
        assertTrue(t6.getMoveStatus().isDone());
        final Move move = Move.MoveFactory.createMove(t6.getLatestBoard(), BoardUtils.getPieceAtPosition(t6.getLatestBoard(), "e1"), BoardUtils.getCoordinateAtPosition("g1"));
        assertTrue(t6.getLatestBoard().currentPlayer().getLegalMoves().contains(move));
        final MoveTransition t7 = t6.getLatestBoard().currentPlayer().makeMove(move);
        assertTrue(t7.getMoveStatus().isDone());
        assertTrue(t7.getLatestBoard().whitePlayer().isCastled());
        assertFalse(t7.getLatestBoard().whitePlayer().isKingSideCastleCapable());
        assertFalse(t7.getLatestBoard().whitePlayer().isQueenSideCastleCapable());
    }

    @Test
    public void testWhiteQueenSideCastle() {
        final Board board = Board.createStandardBoardWithDefaultTimer();
        final MoveTransition t1 = board.currentPlayer().makeMove(Move.MoveFactory.createMove(board, BoardUtils.getPieceAtPosition(board, "e2"), BoardUtils.getCoordinateAtPosition("e4")));
        assertTrue(t1.getMoveStatus().isDone());
        final MoveTransition t2 = t1.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t1.getLatestBoard(), BoardUtils.getPieceAtPosition(t1.getLatestBoard(), "e7"), BoardUtils.getCoordinateAtPosition("e5")));
        assertTrue(t2.getMoveStatus().isDone());
        final MoveTransition t3 = t2.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t2.getLatestBoard(), BoardUtils.getPieceAtPosition(t2.getLatestBoard(), "d2"), BoardUtils.getCoordinateAtPosition("d3")));
        assertTrue(t3.getMoveStatus().isDone());
        final MoveTransition t4 = t3.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t3.getLatestBoard(), BoardUtils.getPieceAtPosition(t3.getLatestBoard(), "d7"), BoardUtils.getCoordinateAtPosition("d6")));
        assertTrue(t4.getMoveStatus().isDone());
        final MoveTransition t5 = t4.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t4.getLatestBoard(), BoardUtils.getPieceAtPosition(t4.getLatestBoard(), "c1"), BoardUtils.getCoordinateAtPosition("d2")));
        assertTrue(t5.getMoveStatus().isDone());
        final MoveTransition t6 = t5.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t5.getLatestBoard(), BoardUtils.getPieceAtPosition(t5.getLatestBoard(), "d6"), BoardUtils.getCoordinateAtPosition("d5")));
        assertTrue(t6.getMoveStatus().isDone());
        final MoveTransition t7 = t6.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t6.getLatestBoard(), BoardUtils.getPieceAtPosition(t6.getLatestBoard(), "d1"), BoardUtils.getCoordinateAtPosition("e2")));
        assertTrue(t7.getMoveStatus().isDone());
        final MoveTransition t8 = t7.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t7.getLatestBoard(), BoardUtils.getPieceAtPosition(t7.getLatestBoard(), "h7"), BoardUtils.getCoordinateAtPosition("h6")));
        assertTrue(t8.getMoveStatus().isDone());
        final MoveTransition t9 = t8.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t8.getLatestBoard(), BoardUtils.getPieceAtPosition(t8.getLatestBoard(), "b1"), BoardUtils.getCoordinateAtPosition("c3")));
        assertTrue(t9.getMoveStatus().isDone());
        final MoveTransition t10 = t9.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t9.getLatestBoard(), BoardUtils.getPieceAtPosition(t9.getLatestBoard(), "h6"), BoardUtils.getCoordinateAtPosition("h5")));
        assertTrue(t10.getMoveStatus().isDone());
        final Move move = Move.MoveFactory.createMove(t10.getLatestBoard(), BoardUtils.getPieceAtPosition(t10.getLatestBoard(), "e1"), BoardUtils.getCoordinateAtPosition("c1"));
        assertTrue(t10.getLatestBoard().currentPlayer().getLegalMoves().contains(move));
        final MoveTransition t11 = t10.getLatestBoard().currentPlayer().makeMove(move);
        assertTrue(t11.getMoveStatus().isDone());
        assertTrue(t11.getLatestBoard().whitePlayer().isCastled());
        assertFalse(t11.getLatestBoard().whitePlayer().isKingSideCastleCapable());
        assertFalse(t11.getLatestBoard().whitePlayer().isQueenSideCastleCapable());
    }

    @Test
    public void testBlackKingSideCastle() {
        final Board board = Board.createStandardBoardWithDefaultTimer();
        final MoveTransition t1 = board.currentPlayer().makeMove(Move.MoveFactory.createMove(board, BoardUtils.getPieceAtPosition(board, "e2"), BoardUtils.getCoordinateAtPosition("e4")));
        assertTrue(t1.getMoveStatus().isDone());
        final MoveTransition t2 = t1.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t1.getLatestBoard(), BoardUtils.getPieceAtPosition(t1.getLatestBoard(), "e7"), BoardUtils.getCoordinateAtPosition("e5")));
        assertTrue(t2.getMoveStatus().isDone());
        final MoveTransition t3 = t2.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t2.getLatestBoard(), BoardUtils.getPieceAtPosition(t2.getLatestBoard(), "d2"), BoardUtils.getCoordinateAtPosition("d3")));
        assertTrue(t3.getMoveStatus().isDone());
        final MoveTransition t4 = t3.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t3.getLatestBoard(), BoardUtils.getPieceAtPosition(t3.getLatestBoard(), "g8"), BoardUtils.getCoordinateAtPosition("f6")));
        assertTrue(t4.getMoveStatus().isDone());
        final MoveTransition t5 = t4.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t4.getLatestBoard(), BoardUtils.getPieceAtPosition(t4.getLatestBoard(), "d3"), BoardUtils.getCoordinateAtPosition("d4")));
        assertTrue(t5.getMoveStatus().isDone());
        final MoveTransition t6 = t5.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t5.getLatestBoard(), BoardUtils.getPieceAtPosition(t5.getLatestBoard(), "f8"), BoardUtils.getCoordinateAtPosition("e7")));
        assertTrue(t6.getMoveStatus().isDone());
        final MoveTransition t7 = t6.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t6.getLatestBoard(), BoardUtils.getPieceAtPosition(t6.getLatestBoard(), "d4"), BoardUtils.getCoordinateAtPosition("d5")));
        assertTrue(t7.getMoveStatus().isDone());
        final Move move = Move.MoveFactory.createMove(t7.getLatestBoard(), BoardUtils.getPieceAtPosition(t7.getLatestBoard(), "e8"), BoardUtils.getCoordinateAtPosition("g8"));
        assertTrue(t7.getLatestBoard().currentPlayer().getLegalMoves().contains(move));
        final MoveTransition t8 = t7.getLatestBoard().currentPlayer().makeMove(move);
        assertTrue(t8.getMoveStatus().isDone());
        assertTrue(t8.getLatestBoard().blackPlayer().isCastled());
        assertFalse(t8.getLatestBoard().blackPlayer().isKingSideCastleCapable());
        assertFalse(t8.getLatestBoard().blackPlayer().isQueenSideCastleCapable());
    }

    @Test
    public void testBlackQueenSideCastle() {
        final Board board = Board.createStandardBoardWithDefaultTimer();
        final MoveTransition t1 = board.currentPlayer().makeMove(Move.MoveFactory.createMove(board, BoardUtils.getPieceAtPosition(board, "e2"), BoardUtils.getCoordinateAtPosition("e4")));
        assertTrue(t1.getMoveStatus().isDone());
        final MoveTransition t2 = t1.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t1.getLatestBoard(), BoardUtils.getPieceAtPosition(t1.getLatestBoard(), "e7"), BoardUtils.getCoordinateAtPosition("e5")));
        assertTrue(t2.getMoveStatus().isDone());
        final MoveTransition t3 = t2.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t2.getLatestBoard(), BoardUtils.getPieceAtPosition(t2.getLatestBoard(), "d2"), BoardUtils.getCoordinateAtPosition("d3")));
        assertTrue(t3.getMoveStatus().isDone());
        final MoveTransition t4 = t3.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t3.getLatestBoard(), BoardUtils.getPieceAtPosition(t3.getLatestBoard(), "d8"), BoardUtils.getCoordinateAtPosition("e7")));
        assertTrue(t4.getMoveStatus().isDone());
        final MoveTransition t5 = t4.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t4.getLatestBoard(), BoardUtils.getPieceAtPosition(t4.getLatestBoard(), "b1"), BoardUtils.getCoordinateAtPosition("c3")));
        assertTrue(t5.getMoveStatus().isDone());
        final MoveTransition t6 = t5.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t5.getLatestBoard(), BoardUtils.getPieceAtPosition(t5.getLatestBoard(), "b8"), BoardUtils.getCoordinateAtPosition("c6")));
        assertTrue(t6.getMoveStatus().isDone());
        final MoveTransition t7 = t6.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t6.getLatestBoard(), BoardUtils.getPieceAtPosition(t6.getLatestBoard(), "c1"), BoardUtils.getCoordinateAtPosition("d2")));
        assertTrue(t7.getMoveStatus().isDone());
        final MoveTransition t8 = t7.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t7.getLatestBoard(), BoardUtils.getPieceAtPosition(t7.getLatestBoard(), "d7"), BoardUtils.getCoordinateAtPosition("d6")));
        assertTrue(t8.getMoveStatus().isDone());
        final MoveTransition t9 = t8.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t8.getLatestBoard(), BoardUtils.getPieceAtPosition(t8.getLatestBoard(), "f1"), BoardUtils.getCoordinateAtPosition("e2")));
        assertTrue(t9.getMoveStatus().isDone());
        final MoveTransition t10 = t9.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t9.getLatestBoard(), BoardUtils.getPieceAtPosition(t9.getLatestBoard(), "c8"), BoardUtils.getCoordinateAtPosition("d7")));
        assertTrue(t10.getMoveStatus().isDone());
        final MoveTransition t11 = t10.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t10.getLatestBoard(), BoardUtils.getPieceAtPosition(t10.getLatestBoard(), "g1"), BoardUtils.getCoordinateAtPosition("f3")));
        assertTrue(t11.getMoveStatus().isDone());
        final Move move = Move.MoveFactory.createMove(t11.getLatestBoard(), BoardUtils.getPieceAtPosition(t11.getLatestBoard(), "e8"), BoardUtils.getCoordinateAtPosition("c8"));
        assertTrue(t11.getLatestBoard().currentPlayer().getLegalMoves().contains(move));
        final MoveTransition t12 = t11.getLatestBoard().currentPlayer().makeMove(move);
        assertTrue(t12.getMoveStatus().isDone());
        assertTrue(t12.getLatestBoard().blackPlayer().isCastled());
        assertFalse(t12.getLatestBoard().blackPlayer().isKingSideCastleCapable());
        assertFalse(t12.getLatestBoard().blackPlayer().isQueenSideCastleCapable());
    }

    @Test
    public void testCastleBugOne() {
        final Board board = Board.createStandardBoardWithDefaultTimer();
        final MoveTransition t1 = board.currentPlayer().makeMove(Move.MoveFactory.createMove(board, BoardUtils.getPieceAtPosition(board, "e2"), BoardUtils.getCoordinateAtPosition("e4")));
        assertTrue(t1.getMoveStatus().isDone());
        final MoveTransition t2 = t1.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t1.getLatestBoard(), BoardUtils.getPieceAtPosition(t1.getLatestBoard(), "d7"), BoardUtils.getCoordinateAtPosition("d5")));
        assertTrue(t2.getMoveStatus().isDone());
        final MoveTransition t3 = t2.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t2.getLatestBoard(), BoardUtils.getPieceAtPosition(t2.getLatestBoard(), "e4"), BoardUtils.getCoordinateAtPosition("e5")));
        assertTrue(t3.getMoveStatus().isDone());
        final MoveTransition t4 = t3.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t3.getLatestBoard(), BoardUtils.getPieceAtPosition(t3.getLatestBoard(), "c8"), BoardUtils.getCoordinateAtPosition("f5")));
        assertTrue(t4.getMoveStatus().isDone());
        final MoveTransition t5 = t4.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t4.getLatestBoard(), BoardUtils.getPieceAtPosition(t4.getLatestBoard(), "f1"), BoardUtils.getCoordinateAtPosition("d3")));
        assertTrue(t5.getMoveStatus().isDone());
        final MoveTransition t6 = t5.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t5.getLatestBoard(), BoardUtils.getPieceAtPosition(t5.getLatestBoard(), "f5"), BoardUtils.getCoordinateAtPosition("d3")));
        assertTrue(t6.getMoveStatus().isDone());
        final MoveTransition t7 = t6.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t6.getLatestBoard(), BoardUtils.getPieceAtPosition(t6.getLatestBoard(), "c2"), BoardUtils.getCoordinateAtPosition("d3")));
        assertTrue(t7.getMoveStatus().isDone());
        final MoveTransition t8 = t7.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t7.getLatestBoard(), BoardUtils.getPieceAtPosition(t7.getLatestBoard(), "e7"), BoardUtils.getCoordinateAtPosition("e6")));
        assertTrue(t8.getMoveStatus().isDone());
        final MoveTransition t9 = t8.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t8.getLatestBoard(), BoardUtils.getPieceAtPosition(t8.getLatestBoard(), "d1"), BoardUtils.getCoordinateAtPosition("a4")));
        assertTrue(t9.getMoveStatus().isDone());
        final MoveTransition t10 = t9.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t9.getLatestBoard(), BoardUtils.getPieceAtPosition(t9.getLatestBoard(), "d8"), BoardUtils.getCoordinateAtPosition("d7")));
        assertTrue(t10.getMoveStatus().isDone());
        final MoveTransition t11 = t10.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t10.getLatestBoard(), BoardUtils.getPieceAtPosition(t10.getLatestBoard(), "b1"), BoardUtils.getCoordinateAtPosition("c3")));
        assertTrue(t11.getMoveStatus().isDone());
    }

    @Test
    public void testNoCastlingOutOfCheck() {
        final Board board = FenUtilities.createGameFromFEN("r3k2r/1pN1nppp/p3p3/3p4/8/8/PPPK1PPP/R6R b kq - 1 18");
        final Move illegalCastleMove = Move.MoveFactory.createMove(board, BoardUtils.getPieceAtPosition(board, "e8"), BoardUtils.getCoordinateAtPosition("c8"));
        final MoveTransition t1 = board.currentPlayer().makeMove(illegalCastleMove);
        assertFalse(t1.getMoveStatus().isDone());
    }
}