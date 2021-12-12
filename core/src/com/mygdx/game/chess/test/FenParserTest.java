package com.mygdx.game.chess.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.mygdx.game.chess.engine.FEN.FenUtilities;
import com.mygdx.game.chess.engine.board.Board;
import com.mygdx.game.chess.engine.board.BoardUtils;
import com.mygdx.game.chess.engine.board.Move;
import com.mygdx.game.chess.engine.board.MoveTransition;

import org.junit.Test;

public final class FenParserTest {

    @Test
    public void testWriteFEN1() {
        final Board board = Board.createStandardBoardWithDefaultTimer();
        final String fenString = FenUtilities.createFENFromGame(board);
        assertEquals(fenString, "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 0");
    }

    @Test
    public void testWriteFEN2() {
        final Board board = Board.createStandardBoardWithDefaultTimer();
        final MoveTransition t1 = board.currentPlayer().makeMove(Move.MoveFactory.createMove(board, BoardUtils.getPieceAtPosition(board, "e2"), BoardUtils.getCoordinateAtPosition("e4")));
        assertTrue(t1.getMoveStatus().isDone());
        final String fenString = FenUtilities.createFENFromGame(t1.getLatestBoard());
        assertEquals(fenString, "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1");
        final MoveTransition t2 = t1.getLatestBoard().currentPlayer().makeMove(Move.MoveFactory.createMove(t1.getLatestBoard(), BoardUtils.getPieceAtPosition(board, "c7"), BoardUtils.getCoordinateAtPosition("c5")));
        assertTrue(t2.getMoveStatus().isDone());
        final String fenString2 = FenUtilities.createFENFromGame(t2.getLatestBoard());
        assertEquals(fenString2, "rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2");

    }
}
