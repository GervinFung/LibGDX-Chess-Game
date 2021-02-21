package com.mygdx.game.chess.test;

import com.mygdx.game.chess.engine.FEN.FenUtilities;
import com.mygdx.game.chess.engine.board.Board;
import com.mygdx.game.chess.engine.board.BoardUtils;
import com.mygdx.game.chess.engine.board.Move;
import com.mygdx.game.chess.engine.board.MoveTransition;
import com.mygdx.game.chess.engine.pieces.Bishop;
import com.mygdx.game.chess.engine.pieces.King;
import com.mygdx.game.chess.engine.pieces.Knight;
import com.mygdx.game.chess.engine.pieces.Pawn;
import com.mygdx.game.chess.engine.pieces.Queen;
import com.mygdx.game.chess.engine.pieces.Rook;
import com.mygdx.game.chess.engine.player.ArtificialIntelligence.MiniMax;
import org.junit.Test;
import com.mygdx.game.chess.engine.League;

import static com.mygdx.game.chess.engine.board.Board.Builder;
import static com.mygdx.game.chess.engine.board.Move.MoveFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class MiniMaxTest {

    @Test
    public void testOpeningDepth4BlackMovesFirst() {
        final Builder builder = new Builder(0, League.BLACK, null);
        // Black Layout
        builder.setPiece(new Rook(League.BLACK, 0));
        builder.setPiece(new Knight(League.BLACK, 1));
        builder.setPiece(new Bishop(League.BLACK, 2));
        builder.setPiece(new Queen(League.BLACK, 3));
        builder.setPiece(new King(League.BLACK, 4, false, false));
        builder.setPiece(new Bishop(League.BLACK, 5));
        builder.setPiece(new Knight(League.BLACK, 6));
        builder.setPiece(new Rook(League.BLACK, 7));
        builder.setPiece(new Pawn(League.BLACK, 8));
        builder.setPiece(new Pawn(League.BLACK, 9));
        builder.setPiece(new Pawn(League.BLACK, 10));
        builder.setPiece(new Pawn(League.BLACK, 11));
        builder.setPiece(new Pawn(League.BLACK, 12));
        builder.setPiece(new Pawn(League.BLACK, 13));
        builder.setPiece(new Pawn(League.BLACK, 14));
        builder.setPiece(new Pawn(League.BLACK, 15));
        // White Layout
        builder.setPiece(new Pawn(League.WHITE, 48));
        builder.setPiece(new Pawn(League.WHITE, 49));
        builder.setPiece(new Pawn(League.WHITE, 50));
        builder.setPiece(new Pawn(League.WHITE, 51));
        builder.setPiece(new Pawn(League.WHITE, 52));
        builder.setPiece(new Pawn(League.WHITE, 53));
        builder.setPiece(new Pawn(League.WHITE, 54));
        builder.setPiece(new Pawn(League.WHITE, 55));
        builder.setPiece(new Rook(League.WHITE, 56));
        builder.setPiece(new Knight(League.WHITE, 57));
        builder.setPiece(new Bishop(League.WHITE, 58));
        builder.setPiece(new Queen(League.WHITE, 59));
        builder.setPiece(new King(League.WHITE, 60, false, false));
        builder.setPiece(new Bishop(League.WHITE, 61));
        builder.setPiece(new Knight(League.WHITE, 62));
        builder.setPiece(new Rook(League.WHITE, 63));
        
        final Board board = builder.build();
        final MiniMax alphaBeta = new MiniMax(4);
        final Move bestMove = alphaBeta.execute(board);
        assertEquals(bestMove, MoveFactory.createMove(board, BoardTest.getPieceAtPosition(board, "e7"), BoardUtils.getCoordinateAtPosition("e5")));
    }

    @Test
    public void advancedLevelProblem2NakamuraShirov() {
        final Builder builder = new Builder(0, League.WHITE, null);
        // Black Layout
        builder.setPiece(new King(League.BLACK, 5, false, false));
        builder.setPiece(new Pawn(League.BLACK, 10));
        builder.setPiece(new Rook(League.BLACK, 25));
        builder.setPiece(new Bishop(League.BLACK, 29));
        // White Layout
        builder.setPiece(new Knight(League.WHITE, 27));
        builder.setPiece(new Rook(League.WHITE, 36));
        builder.setPiece(new Pawn(League.WHITE, 39));
        builder.setPiece(new King(League.WHITE, 42, false, false));
        builder.setPiece(new Pawn(League.WHITE, 46));

        final Board board = builder.build();
        final MiniMax alphaBeta = new MiniMax(6);
        final Move bestMove = alphaBeta.execute(board);
        assertEquals(bestMove, MoveFactory.createMove(board, BoardTest.getPieceAtPosition(board, "d5"), BoardUtils.getCoordinateAtPosition("c7")));
    }

    @Test
    public void testQualityTwoDepth6() {
        final Board board = FenUtilities.createGameFromFEN( "6k1/3b3r/1p1p4/p1n2p2/1PPNpP1q/P3Q1p1/1R1RB1P1/5K2 b - - 0 1");
        final MiniMax alphaBeta = new MiniMax(6);
        final Move bestMove = alphaBeta.execute(board);
        assertEquals(bestMove, MoveFactory.createMove(board, BoardTest.getPieceAtPosition(board, "h4"), BoardUtils.getCoordinateAtPosition("f4")));
    }

    @Test
    public void testQualityThreeDepth6() {
        final Board board = FenUtilities.createGameFromFEN("r2r1n2/pp2bk2/2p1p2p/3q4/3PN1QP/2P3R1/P4PP1/5RK1 w - - 0 1");
        final MiniMax alphaBeta = new MiniMax(7);
        final Move bestMove = alphaBeta.execute(board);
        assertEquals(bestMove, MoveFactory.createMove(board, BoardTest.getPieceAtPosition(board, "g4"), BoardUtils.getCoordinateAtPosition("g7")));
    }

    @Test
    public void testQualityFourDepth6() {
        final Board board = FenUtilities.createGameFromFEN("r1b1k2r/pp3pbp/1qn1p1p1/2pnP3/3p1PP1/1P1P1NBP/P1P5/RN1QKB1R b KQkq - 2 11");
        final MiniMax alphaBeta = new MiniMax(6);
        final Move bestMove = alphaBeta.execute(board);
        assertEquals(bestMove, MoveFactory.createMove(board, BoardTest.getPieceAtPosition(board, "e8"), BoardUtils.getCoordinateAtPosition("g8")));
    }

    @Test
    public void eloTest2() {
        final Builder builder = new Builder(0, League.WHITE, null);
        // Black Layout
        builder.setPiece(new Knight(League.BLACK, 2));
        builder.setPiece(new Queen(League.BLACK, 3));
        builder.setPiece(new Knight(League.BLACK, 5));
        builder.setPiece(new King(League.BLACK, 6, false, false));
        builder.setPiece(new Pawn(League.BLACK, 13));
        builder.setPiece(new Pawn(League.BLACK, 15));
        builder.setPiece(new Pawn(League.BLACK, 20));
        builder.setPiece(new Pawn(League.BLACK, 22));
        builder.setPiece(new Pawn(League.BLACK, 24));
        builder.setPiece(new Bishop(League.BLACK, 25));
        builder.setPiece(new Pawn(League.BLACK, 27));
        builder.setPiece(new Pawn(League.BLACK, 33));
        // White Layout
        builder.setPiece(new Queen(League.WHITE, 23));
        builder.setPiece(new Pawn(League.WHITE, 28));
        builder.setPiece(new Knight(League.WHITE, 30));
        builder.setPiece(new Pawn(League.WHITE, 31));
        builder.setPiece(new Pawn(League.WHITE, 35));
        builder.setPiece(new Pawn(League.WHITE, 38));
        builder.setPiece(new Pawn(League.WHITE, 41));
        builder.setPiece(new Knight(League.WHITE, 46));
        builder.setPiece(new Pawn(League.WHITE, 48));
        builder.setPiece(new Pawn(League.WHITE, 53));
        builder.setPiece(new Bishop(League.WHITE, 54));
        builder.setPiece(new King(League.WHITE, 62, false, false));
        
        final Board board = builder.build();
        final MiniMax alphaBeta = new MiniMax(8);
        final Move bestMove = alphaBeta.execute(board);
        assertEquals(bestMove, MoveFactory.createMove(board, BoardTest.getPieceAtPosition(board, "h5"), BoardUtils.getCoordinateAtPosition("g6")));
    }

    @Test
    public void eloTest3() {
        final Builder builder = new Builder(0, League.WHITE, null);
        // Black Layout
        builder.setPiece(new Rook(League.BLACK, 11));
        builder.setPiece(new Pawn(League.BLACK, 14));
        builder.setPiece(new Pawn(League.BLACK, 16));
        builder.setPiece(new Pawn(League.BLACK, 17));
        builder.setPiece(new Pawn(League.BLACK, 20));
        builder.setPiece(new Pawn(League.BLACK, 22));
        builder.setPiece(new King(League.BLACK, 25, false, false));
        builder.setPiece(new Knight(League.BLACK, 33));
        // White Layout
        builder.setPiece(new Bishop(League.WHITE, 19));
        builder.setPiece(new Pawn(League.WHITE, 26));
        builder.setPiece(new King(League.WHITE, 36, false, false));
        builder.setPiece(new Rook(League.WHITE, 46));
        builder.setPiece(new Pawn(League.WHITE, 49));
        builder.setPiece(new Pawn(League.WHITE, 53));
        
        final Board board = builder.build();
        final MiniMax alphaBeta = new MiniMax(6);
        final Move bestMove = alphaBeta.execute(board);
        assertEquals(bestMove, MoveFactory.createMove(board, BoardTest.getPieceAtPosition(board, "g3"),BoardUtils.getCoordinateAtPosition("g6")));
    }

    @Test
    public void blackWidowLoss1() {
        final Board board = FenUtilities.createGameFromFEN("r2qkb1r/3p1pp1/p1n1p2p/1p1bP3/P2p4/1PP5/5PPP/RNBQNRK1 w kq - 0 13");
        final MiniMax alphaBeta = new MiniMax(6);
        final Move bestMove = alphaBeta.execute(board);
        assertEquals(bestMove, MoveFactory.createMove(board, BoardTest.getPieceAtPosition(board, "c3"), BoardUtils.getCoordinateAtPosition("d4")));
    }

    @Test
    public void testCheckmateHorizon() {
        final Builder builder = new Builder(0, League.WHITE, null);
        // Black Layout
        builder.setPiece(new Rook(League.BLACK, 11));
        builder.setPiece(new Pawn(League.BLACK, 16));
        builder.setPiece(new Bishop(League.BLACK, 27));
        builder.setPiece(new King(League.BLACK, 29, false, false));
        // White Layout
        builder.setPiece(new Rook(League.WHITE, 17));
        builder.setPiece(new Rook(League.WHITE, 26));
        builder.setPiece(new Pawn(League.WHITE, 35));
        builder.setPiece(new Pawn(League.WHITE, 45));
        builder.setPiece(new Bishop(League.WHITE, 51));
        builder.setPiece(new Pawn(League.WHITE, 54));
        builder.setPiece(new Pawn(League.WHITE, 55));
        builder.setPiece(new King(League.WHITE, 63, false, false));
        
        final Board board = builder.build();
        final MiniMax alphaBeta = new MiniMax(4);
        final Move bestMove = alphaBeta.execute(board);
        assertEquals(bestMove, MoveFactory.createMove(board, BoardTest.getPieceAtPosition(board, "g2"), BoardUtils.getCoordinateAtPosition("g4")));
    }

    @Test
    public void testBlackInTrouble() {
        final Builder builder = new Builder(0, League.WHITE, null);
        // Black Layout
        builder.setPiece(new King(League.BLACK, 7, false, false));
        builder.setPiece(new Pawn(League.BLACK, 8));
        builder.setPiece(new Pawn(League.BLACK, 9));
        builder.setPiece(new Pawn(League.BLACK, 10));
        builder.setPiece(new Queen(League.BLACK, 11));
        builder.setPiece(new Rook(League.BLACK, 14));
        builder.setPiece(new Pawn(League.BLACK, 15));
        builder.setPiece(new Bishop(League.BLACK, 17));
        builder.setPiece(new Knight(League.BLACK, 18));
        builder.setPiece(new Pawn(League.BLACK, 19));
        builder.setPiece(new Pawn(League.BLACK, 21));
        // White Layout
        builder.setPiece(new Knight(League.WHITE, 31));
        builder.setPiece(new Pawn(League.WHITE, 35));
        builder.setPiece(new Rook(League.WHITE, 36));
        builder.setPiece(new Queen(League.WHITE, 46));
        builder.setPiece(new Pawn(League.WHITE, 48));
        builder.setPiece(new Pawn(League.WHITE, 53));
        builder.setPiece(new Pawn(League.WHITE, 54));
        builder.setPiece(new Pawn(League.WHITE, 55));
        builder.setPiece(new King(League.WHITE, 62, false, false));
        
        final Board board = builder.build();
        final MiniMax alphaBeta = new MiniMax(4);
        final Move bestMove = alphaBeta.execute(board);
        assertEquals(bestMove, MoveFactory.createMove(board, BoardTest.getPieceAtPosition(board, "e4"), BoardUtils.getCoordinateAtPosition("e8")));
    }

    @Test
    public void findMate3() {
        final Board board = FenUtilities.createGameFromFEN("5rk1/5Npp/8/3Q4/8/8/8/7K w - - 0");
        final MiniMax alphaBeta = new MiniMax(6);
        final Move bestMove = alphaBeta.execute(board);
        assertEquals(bestMove, MoveFactory.createMove(board, BoardTest.getPieceAtPosition(board, "f7"), BoardUtils.getCoordinateAtPosition("h6")));
        final MoveTransition t1 = board.currentPlayer().makeMove(bestMove);
        assertTrue(t1.getMoveStatus().isDone());
    }

    @Test
    public void runawayPawnMakesIt() {
        final Board board = FenUtilities.createGameFromFEN("2k5/8/8/8/p7/8/8/4K3 b - - 0 1");
        final MiniMax alphaBeta = new MiniMax(5);
        final Move bestMove = alphaBeta.execute(board);
        assertEquals(bestMove, MoveFactory.createMove(board, BoardTest.getPieceAtPosition(board, "a4"), BoardUtils.getCoordinateAtPosition("a3")));
        final MoveTransition t1 = board.currentPlayer().makeMove(bestMove);
        assertTrue(t1.getMoveStatus().isDone());
    }

    @Test
    public void testMackHackScenario() {
        final Board board = FenUtilities.createGameFromFEN("1r1k1r2/p5Q1/2p3p1/8/1q1p2n1/3P2P1/P3RPP1/4RK2 b - - 0 1");
        final MiniMax alphaBeta = new MiniMax(8);
        final Move bestMove = alphaBeta.execute(board);
        assertEquals(bestMove, MoveFactory.createMove(board, BoardTest.getPieceAtPosition(board, "f8"), BoardUtils.getCoordinateAtPosition("f2")));
        final MoveTransition t1 = board.currentPlayer().makeMove(bestMove);
        assertTrue(t1.getMoveStatus().isDone());
    }

    @Test
    public void testAutoResponseVsPrinChess() {
        final Board board = FenUtilities.createGameFromFEN("r2q1rk1/p1p2pp1/3p1b2/2p2QNb/4PB1P/6R1/PPPR4/2K5 b - - 0 1");
        final MiniMax alphaBeta = new MiniMax(6);
        final Move bestMove = alphaBeta.execute(board);
        assertEquals(bestMove, MoveFactory.createMove(board, BoardTest.getPieceAtPosition(board, "h5"), BoardUtils.getCoordinateAtPosition("g6")));
        final MoveTransition t1 = board.currentPlayer().makeMove(bestMove);
        assertTrue(t1.getMoveStatus().isDone());
    }

    @Test
    public void testBratcoKopec1() {
        final Board board = FenUtilities.createGameFromFEN( "1k1r4/pp1b1R2/3q2pp/4p3/2B5/4Q3/PPP2B2/2K5 b - - 0 1");
        final MiniMax alphaBeta = new MiniMax(6);
        final Move bestMove = alphaBeta.execute(board);
        assertEquals(bestMove, MoveFactory.createMove(board, BoardTest.getPieceAtPosition(board, "d6"), BoardUtils.getCoordinateAtPosition("d1")));
        final MoveTransition t1 = board.currentPlayer().makeMove(bestMove);
        assertTrue(t1.getMoveStatus().isDone());
    }

    @Test
    public void testTimeOut() {
        final Board board = Board.createStandardBoard(0, 10, 99);
        final Move whiteMove = MoveFactory.createMove(board, BoardTest.getPieceAtPosition(board, "e2"), BoardUtils.getCoordinateAtPosition("e4"));
        final MiniMax miniMax = new MiniMax(5);
        final Board currentBoard = board.currentPlayer().makeMove(whiteMove).getLatestBoard();
        new Thread(new Runnable() {
            @Override
            public void run() {
                long start = System.nanoTime();
                while (!currentBoard.currentPlayer().isTimeOut()) {
                    if (((System.nanoTime() - start) / 1000000000) == 1) {
                        currentBoard.currentPlayer().countDown();
                        start = System.nanoTime();
                    }
                }
                miniMax.setTerminateProcess(true);
            }
        }).start();
        final Move bestMove = miniMax.execute(currentBoard);
        assertEquals(MoveFactory.getNullMove(), bestMove);
    }
}
