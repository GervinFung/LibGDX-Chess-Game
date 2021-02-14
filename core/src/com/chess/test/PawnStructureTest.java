package com.chess.test;

import com.chess.engine.FEN.FenUtilities;
import com.chess.engine.League;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.player.ArtificialIntelligence.PawnStructureAnalyse;
import com.chess.engine.player.ArtificialIntelligence.StandardBoardEvaluation;
import org.junit.Test;
import com.chess.engine.pieces.*;

import static com.chess.engine.board.Board.Builder;
import static org.junit.Assert.assertEquals;

public final class PawnStructureTest {
    private final PawnStructureAnalyse pawnStructureAnalyzer = new PawnStructureAnalyse();

    @Test
    public void testIsolatedPawnsOnStandardBoard() {
        final Board board = Board.createStandardBoard(BoardUtils.UTILS.DEFAULT_TIMER_MINUTE, BoardUtils.UTILS.DEFAULT_TIMER_SECOND, BoardUtils.UTILS.DEFAULT_TIMER_MILLISECOND);
        assertEquals(pawnStructureAnalyzer.isolatedPawnPenalty(board.whitePlayer()), 0);
        assertEquals(pawnStructureAnalyzer.isolatedPawnPenalty(board.blackPlayer()), 0);
    }

    @Test
    public void testIsolatedPawnByExample1() {
        final Board board = FenUtilities.createGameFromFEN( "r1bq1rk1/pp2bppp/1np2n2/6B1/3P4/1BNQ4/PP2NPPP/R3R1K1 b - - 0 13\n1:0:0\n1:0:0");
        assertEquals(pawnStructureAnalyzer.isolatedPawnPenalty(board.whitePlayer()), PawnStructureAnalyse.ISOLATED_PAWN_PENALTY);
        assertEquals(pawnStructureAnalyzer.isolatedPawnPenalty(board.blackPlayer()), 0);
    }

    @Test
    public void testIsolatedPawnByExample2() {
        final Board board = FenUtilities.createGameFromFEN("r1bq1rk1/p3bppp/1np2n2/6B1/3P4/1BNQ4/PP2NPPP/R3R1K1 b - - 0 1\n1:0:0\n1:0:0");
        assertEquals(pawnStructureAnalyzer.isolatedPawnPenalty(board.whitePlayer()), PawnStructureAnalyse.ISOLATED_PAWN_PENALTY);
        assertEquals(pawnStructureAnalyzer.isolatedPawnPenalty(board.blackPlayer()), PawnStructureAnalyse.ISOLATED_PAWN_PENALTY * 2);
    }

    @Test
    public void testIsolatedPawnByExample3() {
        final Builder builder = new Builder(0, League.WHITE, null);
        // Black Layout
        builder.setPiece(new King(League.BLACK, 4, false, false));
        builder.setPiece(new Pawn(League.BLACK, 12));
        builder.setPiece(new Pawn(League.BLACK, 20));
        builder.setPiece(new Pawn(League.BLACK, 28));
        builder.setPiece(new Pawn(League.BLACK, 8));
        builder.setPiece(new Pawn(League.BLACK, 16));
        // White Layout
        builder.setPiece(new Pawn(League.WHITE, 52));
        builder.setPiece(new King(League.WHITE, 60, false, false));
        
        final Board board = builder.build();

        assertEquals(pawnStructureAnalyzer.isolatedPawnPenalty(board.whitePlayer()), PawnStructureAnalyse.ISOLATED_PAWN_PENALTY);
        assertEquals(pawnStructureAnalyzer.isolatedPawnPenalty(board.blackPlayer()), PawnStructureAnalyse.ISOLATED_PAWN_PENALTY * 5);
    }

    @Test
    public void testIsolatedPawnByExample4() {
        final Board board = FenUtilities.createGameFromFEN("4k3/2p1p1p1/8/8/8/8/2P1P1P1/4K3 w KQkq - - 0 1\n1:0:0\n1:0:0");
        assertEquals(pawnStructureAnalyzer.isolatedPawnPenalty(board.whitePlayer()), PawnStructureAnalyse.ISOLATED_PAWN_PENALTY * 3);
        assertEquals(pawnStructureAnalyzer.isolatedPawnPenalty(board.blackPlayer()), PawnStructureAnalyse.ISOLATED_PAWN_PENALTY * 3);
        final StandardBoardEvaluation boardEvaluator = new StandardBoardEvaluation();
        assertEquals(boardEvaluator.evaluate(board, 1), 0);
    }

    @Test
    public void testIsolatedPawnByExample5() {
        final Board board = FenUtilities.createGameFromFEN( "6k1/p6p/8/8/8/8/P6P/6K1 b - - 0 1\n1:0:0\n1:0:0");
        assertEquals(pawnStructureAnalyzer.isolatedPawnPenalty(board.whitePlayer()), PawnStructureAnalyse.ISOLATED_PAWN_PENALTY * 2);
        assertEquals(pawnStructureAnalyzer.isolatedPawnPenalty(board.blackPlayer()), PawnStructureAnalyse.ISOLATED_PAWN_PENALTY * 2);
        final StandardBoardEvaluation boardEvaluator = new StandardBoardEvaluation();
        assertEquals(boardEvaluator.evaluate(board, 1), 0);
    }

    @Test
    public void testIsolatedPawnByExample6() {
        final Board board = FenUtilities.createGameFromFEN("6k1/4p3/4p3/8/8/4P3/4P3/6K1 b - - 0 1\n1:0:0\n1:0:0");
        assertEquals(pawnStructureAnalyzer.isolatedPawnPenalty(board.whitePlayer()), PawnStructureAnalyse.ISOLATED_PAWN_PENALTY * 2);
        assertEquals(pawnStructureAnalyzer.isolatedPawnPenalty(board.blackPlayer()), PawnStructureAnalyse.ISOLATED_PAWN_PENALTY * 2);
        final StandardBoardEvaluation boardEvaluator = new StandardBoardEvaluation();
        assertEquals(boardEvaluator.evaluate(board, 1), 0);
    }

    @Test
    public void testDoubledPawnByExample1() {
        final Board board = Board.createStandardBoard(BoardUtils.UTILS.DEFAULT_TIMER_MINUTE, BoardUtils.UTILS.DEFAULT_TIMER_SECOND, BoardUtils.UTILS.DEFAULT_TIMER_MILLISECOND);
        assertEquals(pawnStructureAnalyzer.doubledPawnPenalty(board.whitePlayer()), 0);
        assertEquals(pawnStructureAnalyzer.doubledPawnPenalty(board.blackPlayer()), 0);
        final StandardBoardEvaluation boardEvaluator = new StandardBoardEvaluation();
        assertEquals(boardEvaluator.evaluate(board, 1), 0);
    }

    @Test
    public void testDoubledPawnByExample2() {
        final Board board = FenUtilities.createGameFromFEN("6k1/4p3/4p3/8/8/4P3/4P3/6K1 b - - 0 1\n1:0:0\n1:0:0");
        assertEquals(pawnStructureAnalyzer.doubledPawnPenalty(board.whitePlayer()), PawnStructureAnalyse.DOUBLED_PAWN_PENALTY * 2);
        assertEquals(pawnStructureAnalyzer.doubledPawnPenalty(board.blackPlayer()), PawnStructureAnalyse.DOUBLED_PAWN_PENALTY * 2);
        final StandardBoardEvaluation boardEvaluator = new StandardBoardEvaluation();
        assertEquals(boardEvaluator.evaluate(board, 1), 0);
    }

    @Test
    public void testDoubledPawnByExample3() {
        final Board board = FenUtilities.createGameFromFEN("6k1/8/8/P7/P7/P7/8/6K1 b - - 0 1\n1:0:0\n1:0:0");
        assertEquals(pawnStructureAnalyzer.doubledPawnPenalty(board.whitePlayer()), PawnStructureAnalyse.DOUBLED_PAWN_PENALTY * 3);
        assertEquals(pawnStructureAnalyzer.doubledPawnPenalty(board.blackPlayer()), 0);
    }

    @Test
    public void testDoubledPawnByExample4() {
        final Board board = FenUtilities.createGameFromFEN( "6k1/8/8/P6p/P6p/P6p/8/6K1 b - - 0 1\n1:0:0\n1:0:0");
        assertEquals(pawnStructureAnalyzer.doubledPawnPenalty(board.whitePlayer()), PawnStructureAnalyse.DOUBLED_PAWN_PENALTY * 3);
        assertEquals(pawnStructureAnalyzer.doubledPawnPenalty(board.blackPlayer()), PawnStructureAnalyse.DOUBLED_PAWN_PENALTY * 3);
        assertEquals(pawnStructureAnalyzer.isolatedPawnPenalty(board.whitePlayer()), PawnStructureAnalyse.ISOLATED_PAWN_PENALTY * 3);
        assertEquals(pawnStructureAnalyzer.isolatedPawnPenalty(board.blackPlayer()), PawnStructureAnalyse.ISOLATED_PAWN_PENALTY * 3);
    }
}
