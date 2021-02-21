package com.mygdx.game.chess.test;

import org.junit.Test;

import com.mygdx.game.chess.engine.board.Board;
import com.mygdx.game.chess.engine.board.BoardUtils;
import com.mygdx.game.chess.engine.board.MoveTransition;
import com.mygdx.game.chess.engine.League;
import com.mygdx.game.chess.engine.pieces.Bishop;
import com.mygdx.game.chess.engine.pieces.King;
import com.mygdx.game.chess.engine.pieces.Pawn;

import static com.mygdx.game.chess.engine.board.Board.Builder;
import static com.mygdx.game.chess.engine.board.Move.MoveFactory;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class StaleMateTest {
    @Test
    public void testAnandKramnikStaleMate() {

        final Builder builder = new Builder(0, League.BLACK, null);
        // Black Layout
        builder.setPiece(new Pawn(League.BLACK, 14));
        builder.setPiece(new Pawn(League.BLACK, 21));
        builder.setPiece(new King(League.BLACK, 36, false, false));
        // White Layout
        builder.setPiece(new Pawn(League.WHITE, 29));
        builder.setPiece(new King(League.WHITE, 31, false, false));
        builder.setPiece(new Pawn(League.WHITE, 39));

        final Board board = builder.build();
        assertFalse(board.currentPlayer().isInStalemate());
        final MoveTransition t1 = board.currentPlayer().makeMove(MoveFactory.createMove(board, BoardTest.getPieceAtPosition(board, "e4"), BoardUtils.getCoordinateAtPosition("f5")));
        assertTrue(t1.getMoveStatus().isDone());
        assertTrue(t1.getLatestBoard().currentPlayer().isInStalemate());
        assertFalse(t1.getLatestBoard().currentPlayer().isInCheck());
        assertFalse(t1.getLatestBoard().currentPlayer().isInCheckmate());
    }

    @Test
    public void testAnonymousStaleMate() {
        final Builder builder = new Builder(0, League.WHITE, null);
        // Black Layout
        builder.setPiece(new King(League.BLACK, 2, false, false));
        // White Layout
        builder.setPiece(new Pawn(League.WHITE, 10));
        builder.setPiece(new King(League.WHITE, 26, false, false));

        final Board board = builder.build();
        assertFalse(board.currentPlayer().isInStalemate());
        final MoveTransition t1 = board.currentPlayer().makeMove(MoveFactory.createMove(board, BoardTest.getPieceAtPosition(board, "c5"), BoardUtils.getCoordinateAtPosition("c6")));
        assertTrue(t1.getMoveStatus().isDone());
        assertTrue(t1.getLatestBoard().currentPlayer().isInStalemate());
        assertFalse(t1.getLatestBoard().currentPlayer().isInCheck());
        assertFalse(t1.getLatestBoard().currentPlayer().isInCheckmate());
    }

    @Test
    public void testAnonymousStaleMate2() {
        final Builder builder = new Builder(0, League.WHITE, null);
        // Black Layout
        builder.setPiece(new King(League.BLACK, 0, false, false));
        // White Layout
        builder.setPiece(new Pawn(League.WHITE, 16));
        builder.setPiece(new King(League.WHITE, 17, false, false));
        builder.setPiece(new Bishop(League.WHITE, 19));

        final Board board = builder.build();
        assertFalse(board.currentPlayer().isInStalemate());
        final MoveTransition t1 = board.currentPlayer().makeMove(MoveFactory.createMove(board, BoardTest.getPieceAtPosition(board, "a6"), BoardUtils.getCoordinateAtPosition("a7")));
        assertTrue(t1.getMoveStatus().isDone());
        assertTrue(t1.getLatestBoard().currentPlayer().isInStalemate());
        assertFalse(t1.getLatestBoard().currentPlayer().isInCheck());
        assertFalse(t1.getLatestBoard().currentPlayer().isInCheckmate());
    }
}
