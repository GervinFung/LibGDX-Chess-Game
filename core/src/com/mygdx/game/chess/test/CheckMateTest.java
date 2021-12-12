package com.mygdx.game.chess.test;

import static com.mygdx.game.chess.engine.board.Move.MoveFactory;
import static org.testng.Assert.assertTrue;

import com.mygdx.game.chess.engine.board.Board;
import com.mygdx.game.chess.engine.board.BoardUtils;
import com.mygdx.game.chess.engine.board.MoveTransition;

import org.junit.Test;

public final class CheckMateTest {

    @Test
    public void testFoolsMate() {

        final Board board = Board.createStandardBoard(BoardUtils.DEFAULT_TIMER_MINUTE, BoardUtils.DEFAULT_TIMER_SECOND, BoardUtils.DEFAULT_TIMER_MILLISECOND);
        final MoveTransition t1 = board.currentPlayer().makeMove(MoveFactory.createMove(board, BoardUtils.getPieceAtPosition(board, "f2"), BoardUtils.getCoordinateAtPosition("f3")));

        assertTrue(t1.getMoveStatus().isDone());

        final MoveTransition t2 = t1.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t1.getLatestBoard(), BoardUtils.getPieceAtPosition(t1.getLatestBoard(), "e7"), BoardUtils.getCoordinateAtPosition("e5")));

        assertTrue(t2.getMoveStatus().isDone());

        final MoveTransition t3 = t2.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t2.getLatestBoard(), BoardUtils.getPieceAtPosition(t2.getLatestBoard(), "g2"), BoardUtils.getCoordinateAtPosition("g4")));

        assertTrue(t3.getMoveStatus().isDone());

        final MoveTransition t4 = t3.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t3.getLatestBoard(), BoardUtils.getPieceAtPosition(t3.getLatestBoard(), "d8"), BoardUtils.getCoordinateAtPosition("h4")));

        assertTrue(t4.getMoveStatus().isDone());

        assertTrue(t4.getLatestBoard().currentPlayer().isInCheckmate());

    }

    @Test
    public void testScholarsMate() {

        final Board board = Board.createStandardBoard(BoardUtils.DEFAULT_TIMER_MINUTE, BoardUtils.DEFAULT_TIMER_SECOND, BoardUtils.DEFAULT_TIMER_MILLISECOND);
        final MoveTransition t1 = board.currentPlayer().makeMove(MoveFactory.createMove(board, BoardUtils.getPieceAtPosition(board, "e2"), BoardUtils.getCoordinateAtPosition("e4")));

        assertTrue(t1.getMoveStatus().isDone());

        final MoveTransition t2 = t1.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t1.getLatestBoard(), BoardUtils.getPieceAtPosition(t1.getLatestBoard(), "a7"), BoardUtils.getCoordinateAtPosition("a6")));

        assertTrue(t2.getMoveStatus().isDone());

        final MoveTransition t3 = t2.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t2.getLatestBoard(), BoardUtils.getPieceAtPosition(t2.getLatestBoard(), "d1"), BoardUtils.getCoordinateAtPosition("f3")));

        assertTrue(t3.getMoveStatus().isDone());
        final MoveTransition t4 = t3.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t3.getLatestBoard(), BoardUtils.getPieceAtPosition(t3.getLatestBoard(), "a6"), BoardUtils.getCoordinateAtPosition("a5")));

        assertTrue(t4.getMoveStatus().isDone());

        final MoveTransition t5 = t4.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t4.getLatestBoard(), BoardUtils.getPieceAtPosition(t4.getLatestBoard(), "f1"), BoardUtils.getCoordinateAtPosition("c4")));

        assertTrue(t5.getMoveStatus().isDone());

        final MoveTransition t6 = t5.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t5.getLatestBoard(), BoardUtils.getPieceAtPosition(t5.getLatestBoard(), "a5"), BoardUtils.getCoordinateAtPosition("a4")));

        assertTrue(t6.getMoveStatus().isDone());

        final MoveTransition t7 = t6.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t6.getLatestBoard(), BoardUtils.getPieceAtPosition(t6.getLatestBoard(), "f3"), BoardUtils.getCoordinateAtPosition("f7")));

        assertTrue(t7.getMoveStatus().isDone());
        assertTrue(t7.getLatestBoard().currentPlayer().isInCheckmate());

    }

    @Test
    public void testLegalsMate() {

        final Board board = Board.createStandardBoard(BoardUtils.DEFAULT_TIMER_MINUTE, BoardUtils.DEFAULT_TIMER_SECOND, BoardUtils.DEFAULT_TIMER_MILLISECOND);
        final MoveTransition t1 = board.currentPlayer().makeMove(MoveFactory.createMove(board, BoardUtils.getPieceAtPosition(board, "e2"), BoardUtils.getCoordinateAtPosition("e4")));

        assertTrue(t1.getMoveStatus().isDone());

        final MoveTransition t2 = t1.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t1.getLatestBoard(), BoardUtils.getPieceAtPosition(t1.getLatestBoard(), "e7"), BoardUtils.getCoordinateAtPosition("e5")));

        assertTrue(t2.getMoveStatus().isDone());

        final MoveTransition t3 = t2.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t2.getLatestBoard(), BoardUtils.getPieceAtPosition(t2.getLatestBoard(), "f1"), BoardUtils.getCoordinateAtPosition("c4")));

        assertTrue(t3.getMoveStatus().isDone());

        final MoveTransition t4 = t3.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t3.getLatestBoard(), BoardUtils.getPieceAtPosition(t3.getLatestBoard(), "d7"), BoardUtils.getCoordinateAtPosition("d6")));

        assertTrue(t4.getMoveStatus().isDone());

        final MoveTransition t5 = t4.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t4.getLatestBoard(), BoardUtils.getPieceAtPosition(t4.getLatestBoard(), "g1"), BoardUtils.getCoordinateAtPosition("f3")));

        assertTrue(t5.getMoveStatus().isDone());

        final MoveTransition t6 = t5.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t5.getLatestBoard(), BoardUtils.getPieceAtPosition(t5.getLatestBoard(), "c8"), BoardUtils.getCoordinateAtPosition("g4")));

        assertTrue(t6.getMoveStatus().isDone());

        final MoveTransition t7 = t6.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t6.getLatestBoard(), BoardUtils.getPieceAtPosition(t6.getLatestBoard(), "b1"), BoardUtils.getCoordinateAtPosition("c3")));

        assertTrue(t7.getMoveStatus().isDone());

        final MoveTransition t8 = t7.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t7.getLatestBoard(), BoardUtils.getPieceAtPosition(t7.getLatestBoard(), "g7"), BoardUtils.getCoordinateAtPosition("g6")));

        assertTrue(t8.getMoveStatus().isDone());

        final MoveTransition t9 = t8.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t8.getLatestBoard(), BoardUtils.getPieceAtPosition(t8.getLatestBoard(), "f3"), BoardUtils.getCoordinateAtPosition("e5")));

        assertTrue(t9.getMoveStatus().isDone());

        final MoveTransition t10 = t9.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t9.getLatestBoard(), BoardUtils.getPieceAtPosition(t9.getLatestBoard(), "g4"), BoardUtils.getCoordinateAtPosition("d1")));

        assertTrue(t10.getMoveStatus().isDone());

        final MoveTransition t11 = t10.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t10.getLatestBoard(), BoardUtils.getPieceAtPosition(t10.getLatestBoard(), "c4"), BoardUtils.getCoordinateAtPosition("f7")));

        assertTrue(t11.getMoveStatus().isDone());

        final MoveTransition t12 = t11.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t11.getLatestBoard(), BoardUtils.getPieceAtPosition(t11.getLatestBoard(), "e8"), BoardUtils.getCoordinateAtPosition("e7")));

        assertTrue(t12.getMoveStatus().isDone());

        final MoveTransition t13 = t12.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t12.getLatestBoard(), BoardUtils.getPieceAtPosition(t12.getLatestBoard(), "c3"), BoardUtils.getCoordinateAtPosition("d5")));

        assertTrue(t13.getMoveStatus().isDone());
        assertTrue(t13.getLatestBoard().currentPlayer().isInCheckmate());
    }

    @Test
    public void testSevenMoveMate() {

        final Board board = Board.createStandardBoard(BoardUtils.DEFAULT_TIMER_MINUTE, BoardUtils.DEFAULT_TIMER_SECOND, BoardUtils.DEFAULT_TIMER_MILLISECOND);
        final MoveTransition t1 = board.currentPlayer().makeMove(MoveFactory.createMove(board, BoardUtils.getPieceAtPosition(board, "e2"), BoardUtils.getCoordinateAtPosition("e4")));

        assertTrue(t1.getMoveStatus().isDone());

        final MoveTransition t2 = t1.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t1.getLatestBoard(), BoardUtils.getPieceAtPosition(t1.getLatestBoard(), "e7"), BoardUtils.getCoordinateAtPosition("e5")));

        assertTrue(t2.getMoveStatus().isDone());

        final MoveTransition t3 = t2.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t2.getLatestBoard(), BoardUtils.getPieceAtPosition(t2.getLatestBoard(), "d2"), BoardUtils.getCoordinateAtPosition("d4")));

        assertTrue(t3.getMoveStatus().isDone());

        final MoveTransition t4 = t3.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t3.getLatestBoard(), BoardUtils.getPieceAtPosition(t3.getLatestBoard(), "d7"), BoardUtils.getCoordinateAtPosition("d6")));

        assertTrue(t4.getMoveStatus().isDone());

        final MoveTransition t5 = t4.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t4.getLatestBoard(), BoardUtils.getPieceAtPosition(t4.getLatestBoard(), "d4"), BoardUtils.getCoordinateAtPosition("e5")));

        assertTrue(t5.getMoveStatus().isDone());

        final MoveTransition t6 = t5.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t5.getLatestBoard(), BoardUtils.getPieceAtPosition(t5.getLatestBoard(), "d8"), BoardUtils.getCoordinateAtPosition("e7")));

        assertTrue(t6.getMoveStatus().isDone());

        final MoveTransition t7 = t6.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t6.getLatestBoard(), BoardUtils.getPieceAtPosition(t6.getLatestBoard(), "e5"), BoardUtils.getCoordinateAtPosition("d6")));

        assertTrue(t7.getMoveStatus().isDone());

        final MoveTransition t8 = t7.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t7.getLatestBoard(), BoardUtils.getPieceAtPosition(t7.getLatestBoard(), "e7"), BoardUtils.getCoordinateAtPosition("e4")));

        assertTrue(t8.getMoveStatus().isDone());

        final MoveTransition t9 = t8.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t8.getLatestBoard(), BoardUtils.getPieceAtPosition(t8.getLatestBoard(), "f1"), BoardUtils.getCoordinateAtPosition("e2")));

        assertTrue(t9.getMoveStatus().isDone());

        final MoveTransition t10 = t9.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t9.getLatestBoard(), BoardUtils.getPieceAtPosition(t9.getLatestBoard(), "e4"), BoardUtils.getCoordinateAtPosition("g2")));

        assertTrue(t10.getMoveStatus().isDone());

        final MoveTransition t11 = t10.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t10.getLatestBoard(), BoardUtils.getPieceAtPosition(t10.getLatestBoard(), "d6"), BoardUtils.getCoordinateAtPosition("c7")));

        assertTrue(t11.getMoveStatus().isDone());

        final MoveTransition t12 = t11.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t11.getLatestBoard(), BoardUtils.getPieceAtPosition(t11.getLatestBoard(), "g2"), BoardUtils.getCoordinateAtPosition("h1")));

        assertTrue(t12.getMoveStatus().isDone());

        final MoveTransition t13 = t12.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t12.getLatestBoard(), BoardUtils.getPieceAtPosition(t12.getLatestBoard(), "d1"), BoardUtils.getCoordinateAtPosition("d8")));

        assertTrue(t13.getMoveStatus().isDone());
        assertTrue(t13.getLatestBoard().currentPlayer().isInCheckmate());

    }

    @Test
    public void testGrecoGame() {

        final Board board = Board.createStandardBoard(BoardUtils.DEFAULT_TIMER_MINUTE, BoardUtils.DEFAULT_TIMER_SECOND, BoardUtils.DEFAULT_TIMER_MILLISECOND);
        final MoveTransition t1 = board.currentPlayer().makeMove(MoveFactory.createMove(board, BoardUtils.getPieceAtPosition(board, "d2"), BoardUtils.getCoordinateAtPosition("d4")));

        assertTrue(t1.getMoveStatus().isDone());

        final MoveTransition t2 = t1.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t1.getLatestBoard(), BoardUtils.getPieceAtPosition(t1.getLatestBoard(), "g8"), BoardUtils.getCoordinateAtPosition("f6")));

        assertTrue(t2.getMoveStatus().isDone());

        final MoveTransition t3 = t2.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t2.getLatestBoard(), BoardUtils.getPieceAtPosition(t2.getLatestBoard(), "b1"), BoardUtils.getCoordinateAtPosition("d2")));

        assertTrue(t3.getMoveStatus().isDone());

        final MoveTransition t4 = t3.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t3.getLatestBoard(), BoardUtils.getPieceAtPosition(t3.getLatestBoard(), "e7"), BoardUtils.getCoordinateAtPosition("e5")));

        assertTrue(t4.getMoveStatus().isDone());

        final MoveTransition t5 = t4.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t4.getLatestBoard(), BoardUtils.getPieceAtPosition(t4.getLatestBoard(), "d4"), BoardUtils.getCoordinateAtPosition("e5")));

        assertTrue(t5.getMoveStatus().isDone());

        final MoveTransition t6 = t5.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t5.getLatestBoard(), BoardUtils.getPieceAtPosition(t5.getLatestBoard(), "f6"), BoardUtils.getCoordinateAtPosition("g4")));

        assertTrue(t6.getMoveStatus().isDone());

        final MoveTransition t7 = t6.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t6.getLatestBoard(), BoardUtils.getPieceAtPosition(t6.getLatestBoard(), "h2"), BoardUtils.getCoordinateAtPosition("h3")));

        assertTrue(t7.getMoveStatus().isDone());

        final MoveTransition t8 = t7.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t7.getLatestBoard(), BoardUtils.getPieceAtPosition(t7.getLatestBoard(), "g4"), BoardUtils.getCoordinateAtPosition("e3")));

        assertTrue(t8.getMoveStatus().isDone());

        final MoveTransition t9 = t8.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t8.getLatestBoard(), BoardUtils.getPieceAtPosition(t8.getLatestBoard(), "f2"), BoardUtils.getCoordinateAtPosition("e3")));

        assertTrue(t9.getMoveStatus().isDone());

        final MoveTransition t10 = t9.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t9.getLatestBoard(), BoardUtils.getPieceAtPosition(t9.getLatestBoard(), "d8"), BoardUtils.getCoordinateAtPosition("h4")));

        assertTrue(t10.getMoveStatus().isDone());

        final MoveTransition t11 = t10.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t10.getLatestBoard(), BoardUtils.getPieceAtPosition(t10.getLatestBoard(), "g2"), BoardUtils.getCoordinateAtPosition("g3")));

        assertTrue(t11.getMoveStatus().isDone());

        final MoveTransition t12 = t11.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t11.getLatestBoard(), BoardUtils.getPieceAtPosition(t11.getLatestBoard(), "h4"), BoardUtils.getCoordinateAtPosition("g3")));

        assertTrue(t12.getMoveStatus().isDone());
        assertTrue(t12.getLatestBoard().currentPlayer().isInCheckmate());

    }

    @Test
    public void testOlympicGame() {

        final Board board = Board.createStandardBoard(BoardUtils.DEFAULT_TIMER_MINUTE, BoardUtils.DEFAULT_TIMER_SECOND, BoardUtils.DEFAULT_TIMER_MILLISECOND);
        final MoveTransition t1 = board.currentPlayer().makeMove(MoveFactory.createMove(board, BoardUtils.getPieceAtPosition(board, "e2"), BoardUtils.getCoordinateAtPosition("e4")));

        assertTrue(t1.getMoveStatus().isDone());

        final MoveTransition t2 = t1.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t1.getLatestBoard(), BoardUtils.getPieceAtPosition(t1.getLatestBoard(), "c7"), BoardUtils.getCoordinateAtPosition("c6")));

        assertTrue(t2.getMoveStatus().isDone());

        final MoveTransition t3 = t2.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t2.getLatestBoard(), BoardUtils.getPieceAtPosition(t2.getLatestBoard(), "g1"), BoardUtils.getCoordinateAtPosition("f3")));

        assertTrue(t3.getMoveStatus().isDone());

        final MoveTransition t4 = t3.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t3.getLatestBoard(), BoardUtils.getPieceAtPosition(t3.getLatestBoard(), "d7"), BoardUtils.getCoordinateAtPosition("d5")));

        assertTrue(t4.getMoveStatus().isDone());

        final MoveTransition t5 = t4.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t4.getLatestBoard(), BoardUtils.getPieceAtPosition(t4.getLatestBoard(), "b1"), BoardUtils.getCoordinateAtPosition("c3")));

        assertTrue(t5.getMoveStatus().isDone());

        final MoveTransition t6 = t5.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t5.getLatestBoard(), BoardUtils.getPieceAtPosition(t5.getLatestBoard(), "d5"), BoardUtils.getCoordinateAtPosition("e4")));

        assertTrue(t6.getMoveStatus().isDone());

        final MoveTransition t7 = t6.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t6.getLatestBoard(), BoardUtils.getPieceAtPosition(t6.getLatestBoard(), "c3"), BoardUtils.getCoordinateAtPosition("e4")));

        assertTrue(t7.getMoveStatus().isDone());

        final MoveTransition t8 = t7.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t7.getLatestBoard(), BoardUtils.getPieceAtPosition(t7.getLatestBoard(), "b8"), BoardUtils.getCoordinateAtPosition("d7")));

        assertTrue(t8.getMoveStatus().isDone());

        final MoveTransition t9 = t8.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t8.getLatestBoard(), BoardUtils.getPieceAtPosition(t8.getLatestBoard(), "d1"), BoardUtils.getCoordinateAtPosition("e2")));

        assertTrue(t9.getMoveStatus().isDone());

        final MoveTransition t10 = t9.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t9.getLatestBoard(), BoardUtils.getPieceAtPosition(t9.getLatestBoard(), "g8"), BoardUtils.getCoordinateAtPosition("f6")));

        assertTrue(t10.getMoveStatus().isDone());

        final MoveTransition t11 = t10.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t10.getLatestBoard(), BoardUtils.getPieceAtPosition(t10.getLatestBoard(), "e4"), BoardUtils.getCoordinateAtPosition("d6")));

        assertTrue(t11.getMoveStatus().isDone());
        assertTrue(t11.getLatestBoard().currentPlayer().isInCheckmate());

    }

    @Test
    public void testAnotherGame() {

        final Board board = Board.createStandardBoard(BoardUtils.DEFAULT_TIMER_MINUTE, BoardUtils.DEFAULT_TIMER_SECOND, BoardUtils.DEFAULT_TIMER_MILLISECOND);
        final MoveTransition t1 = board.currentPlayer().makeMove(MoveFactory.createMove(board, BoardUtils.getPieceAtPosition(board, "e2"), BoardUtils.getCoordinateAtPosition("e4")));

        assertTrue(t1.getMoveStatus().isDone());

        final MoveTransition t2 = t1.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t1.getLatestBoard(), BoardUtils.getPieceAtPosition(t1.getLatestBoard(), "e7"), BoardUtils.getCoordinateAtPosition("e5")));

        assertTrue(t2.getMoveStatus().isDone());

        final MoveTransition t3 = t2.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t2.getLatestBoard(), BoardUtils.getPieceAtPosition(t2.getLatestBoard(), "g1"), BoardUtils.getCoordinateAtPosition("f3")));

        assertTrue(t3.getMoveStatus().isDone());

        final MoveTransition t4 = t3.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t3.getLatestBoard(), BoardUtils.getPieceAtPosition(t3.getLatestBoard(), "b8"), BoardUtils.getCoordinateAtPosition("c6")));

        assertTrue(t4.getMoveStatus().isDone());

        final MoveTransition t5 = t4.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t4.getLatestBoard(), BoardUtils.getPieceAtPosition(t4.getLatestBoard(), "f1"), BoardUtils.getCoordinateAtPosition("c4")));

        assertTrue(t5.getMoveStatus().isDone());

        final MoveTransition t6 = t5.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t5.getLatestBoard(), BoardUtils.getPieceAtPosition(t5.getLatestBoard(), "c6"), BoardUtils.getCoordinateAtPosition("d4")));

        assertTrue(t6.getMoveStatus().isDone());

        final MoveTransition t7 = t6.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t6.getLatestBoard(), BoardUtils.getPieceAtPosition(t6.getLatestBoard(), "f3"), BoardUtils.getCoordinateAtPosition("e5")));

        assertTrue(t7.getMoveStatus().isDone());

        final MoveTransition t8 = t7.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t7.getLatestBoard(), BoardUtils.getPieceAtPosition(t7.getLatestBoard(), "d8"), BoardUtils.getCoordinateAtPosition("g5")));

        assertTrue(t8.getMoveStatus().isDone());

        final MoveTransition t9 = t8.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t8.getLatestBoard(), BoardUtils.getPieceAtPosition(t8.getLatestBoard(), "e5"), BoardUtils.getCoordinateAtPosition("f7")));

        assertTrue(t9.getMoveStatus().isDone());

        final MoveTransition t10 = t9.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t9.getLatestBoard(), BoardUtils.getPieceAtPosition(t9.getLatestBoard(), "g5"), BoardUtils.getCoordinateAtPosition("g2")));

        assertTrue(t10.getMoveStatus().isDone());

        final MoveTransition t11 = t10.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t10.getLatestBoard(), BoardUtils.getPieceAtPosition(t10.getLatestBoard(), "h1"), BoardUtils.getCoordinateAtPosition("f1")));

        assertTrue(t11.getMoveStatus().isDone());

        final MoveTransition t12 = t11.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t11.getLatestBoard(), BoardUtils.getPieceAtPosition(t11.getLatestBoard(), "g2"), BoardUtils.getCoordinateAtPosition("e4")));

        assertTrue(t12.getMoveStatus().isDone());

        final MoveTransition t13 = t12.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t12.getLatestBoard(), BoardUtils.getPieceAtPosition(t12.getLatestBoard(), "c4"), BoardUtils.getCoordinateAtPosition("e2")));

        assertTrue(t13.getMoveStatus().isDone());

        final MoveTransition t14 = t13.getLatestBoard().currentPlayer().makeMove(MoveFactory.createMove(t13.getLatestBoard(), BoardUtils.getPieceAtPosition(t13.getLatestBoard(), "d4"), BoardUtils.getCoordinateAtPosition("f3")));

        assertTrue(t14.getMoveStatus().isDone());
        assertTrue(t14.getLatestBoard().currentPlayer().isInCheckmate());

    }
}