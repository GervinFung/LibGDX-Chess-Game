package com.mygdx.game.chess.test;

import com.mygdx.game.chess.engine.League;
import com.mygdx.game.chess.engine.board.Board;
import com.mygdx.game.chess.engine.board.BoardUtils;
import com.mygdx.game.chess.engine.board.Move;
import com.mygdx.game.chess.engine.board.MoveTransition;
import com.mygdx.game.chess.engine.pieces.Bishop;
import com.mygdx.game.chess.engine.pieces.King;
import com.mygdx.game.chess.engine.pieces.Rook;
import com.mygdx.game.chess.engine.player.ArtificialIntelligence.StandardBoardEvaluation;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import static com.mygdx.game.chess.engine.board.Board.Builder;
import static com.mygdx.game.chess.engine.board.Move.MoveFactory;

public final class PlayerTest {
    final StandardBoardEvaluation standardBoardEvaluation = new StandardBoardEvaluation();
    @Test
    public void testSimpleEvaluation() {
        final Board board = Board.createStandardBoard(BoardUtils.DEFAULT_TIMER_MINUTE, BoardUtils.DEFAULT_TIMER_SECOND, BoardUtils.DEFAULT_TIMER_MILLISECOND);
        final MoveTransition t1 = board.currentPlayer().makeMove(MoveFactory.createMove(board, BoardTest.getPieceAtPosition(board, "e2"), BoardUtils.getCoordinateAtPosition("e4")));
        assertTrue(t1.getMoveStatus().isDone());

        final MoveTransition t2 = t1.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t1.getLatestBoard(), BoardTest.getPieceAtPosition(t1.getLatestBoard(), "e7"),BoardUtils.getCoordinateAtPosition("e5")));
        assertTrue(t2.getMoveStatus().isDone());

        assertEquals(this.standardBoardEvaluation.evaluate(t2.getLatestBoard(), 0), 0);
    }

    @Test
    public void testBug() {
        final Board board = Board.createStandardBoard(BoardUtils.DEFAULT_TIMER_MINUTE, BoardUtils.DEFAULT_TIMER_SECOND, BoardUtils.DEFAULT_TIMER_MILLISECOND);
        final MoveTransition t1 = board.currentPlayer().makeMove(MoveFactory.createMove(board, BoardTest.getPieceAtPosition(board, "c2"), BoardUtils.getCoordinateAtPosition("c3")));
        assertTrue(t1.getMoveStatus().isDone());
        
        final MoveTransition t2 = t1.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t1.getLatestBoard(), BoardTest.getPieceAtPosition(t1.getLatestBoard(), "b8"), BoardUtils.getCoordinateAtPosition("a6")));
        assertTrue(t2.getMoveStatus().isDone());
        
        final MoveTransition t3 = t2.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t2.getLatestBoard(), BoardTest.getPieceAtPosition(t2.getLatestBoard(), "d1"), BoardUtils.getCoordinateAtPosition("a4")));
        assertTrue(t3.getMoveStatus().isDone());
        
        final MoveTransition t4 = t3.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t3.getLatestBoard(), BoardTest.getPieceAtPosition(t3.getLatestBoard(), "d7"), BoardUtils.getCoordinateAtPosition("d6")));
        assertFalse(t4.getMoveStatus().isDone());
    }

    @Test
    public void testDiscoveredCheck() {
        final Builder builder = new Builder(0, League.WHITE, null);
        // Black Layout
        builder.setPiece(new King(League.BLACK, 4, false, false));
        builder.setPiece(new Rook(League.BLACK, 24));
        // White Layout
        builder.setPiece(new Bishop(League.WHITE, 44));
        builder.setPiece(new Rook(League.WHITE, 52));
        builder.setPiece(new King(League.WHITE, 58, false, false));
        final Board board = builder.build();
        final MoveTransition t1 = board.currentPlayer().makeMove(MoveFactory.createMove(board, BoardTest.getPieceAtPosition(board, "e3"), BoardUtils.getCoordinateAtPosition("b6")));
        assertTrue(t1.getMoveStatus().isDone());
        assertTrue(t1.getLatestBoard().currentPlayer().isInCheck());

        final MoveTransition t2 = t1.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t1.getLatestBoard(), BoardTest.getPieceAtPosition(t1.getLatestBoard(), "a5"), BoardUtils.getCoordinateAtPosition("b5")));
        assertFalse(t2.getMoveStatus().isDone());

        final MoveTransition t3 = t1.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t1.getLatestBoard(), BoardTest.getPieceAtPosition(t1.getLatestBoard(), "a5"), BoardUtils.getCoordinateAtPosition("e5")));
        assertTrue(t3.getMoveStatus().isDone());
    }

    @Test
    public void testUnmakeMove() {
        final Board board = Board.createStandardBoard(BoardUtils.DEFAULT_TIMER_MINUTE, BoardUtils.DEFAULT_TIMER_SECOND, BoardUtils.DEFAULT_TIMER_MILLISECOND);
        final Move m1 = MoveFactory.createMove(board, BoardTest.getPieceAtPosition(board, "e2"), BoardUtils.getCoordinateAtPosition("e4"));
        final MoveTransition t1 = board.currentPlayer().makeMove(m1);
        assertTrue(t1.getMoveStatus().isDone());
        t1.getLatestBoard().currentPlayer().getOpponent().undoMove(m1);
    }

    @Test
    public void testIllegalMove() {
        final Board board = Board.createStandardBoard(BoardUtils.DEFAULT_TIMER_MINUTE, BoardUtils.DEFAULT_TIMER_SECOND, BoardUtils.DEFAULT_TIMER_MILLISECOND);
        final Move m1 = MoveFactory.createMove(board, BoardTest.getPieceAtPosition(board, "e2"), BoardUtils.getCoordinateAtPosition("e6"));
        final MoveTransition t1 = board.currentPlayer().makeMove(m1);
        assertFalse(t1.getMoveStatus().isDone());
    }
}
