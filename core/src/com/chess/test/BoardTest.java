package com.chess.test;

import com.chess.engine.League;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.MoveTransition;
import com.chess.engine.pieces.*;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import static com.chess.engine.board.Board.Builder;
import static com.chess.engine.board.Move.MoveFactory;

public final class BoardTest {

    @Test
    public void testHashCode() {
        final Board board = Board.createStandardBoard(BoardUtils.UTILS.DEFAULT_TIMER_MINUTE, BoardUtils.UTILS.DEFAULT_TIMER_SECOND, BoardUtils.UTILS.DEFAULT_TIMER_MILLISECOND);
        final Set<Piece> pieceSet = Sets.newHashSet(board.getAllPieces());
        final Set<Piece> whitePieceSet = Sets.newHashSet(board.getWhitePieces());
        final Set<Piece> blackPieceSet = Sets.newHashSet(board.getBlackPieces());
        assertEquals(32, pieceSet.size());
        assertEquals(16, whitePieceSet.size());
        assertEquals(16, blackPieceSet.size());
    }

    @Test
    public void testInitialBoard() {
        final Board board = Board.createStandardBoard(BoardUtils.UTILS.DEFAULT_TIMER_MINUTE, BoardUtils.UTILS.DEFAULT_TIMER_SECOND, BoardUtils.UTILS.DEFAULT_TIMER_MILLISECOND);
        //each player should have 16 pieces and 20 moves at the beginning
        assertEquals(board.currentPlayer().getLegalMoves().size(), 20);
        assertEquals(board.currentPlayer().getOpponent().getLegalMoves().size(), 20);
        assertEquals(board.currentPlayer().getActivePieces().size(), 16);
        assertEquals(board.currentPlayer().getOpponent().getActivePieces().size(), 16);

        //there should be 20 pieces which is active and has legal moves at the beginning of the board
        final ArrayList<Piece> activeKnight = new ArrayList<>(), activePawns = new ArrayList<>();
        for (final Piece piece : board.getAllPieces()) {
            if (piece.calculateLegalMoves(board).size() != 0 && piece.getPieceType() == PieceType.PAWN) {
                activePawns.add(piece);
            } else if (piece.calculateLegalMoves(board).size() != 0 && piece.getPieceType() == PieceType.KNIGHT) {
                activeKnight.add(piece);
            }
        }
        assertEquals(activePawns.size(), 16);
        assertEquals(activeKnight.size(), 4);

        //initial board configuration should has all below false
        assertFalse(board.currentPlayer().isInCheck());
        assertFalse(board.currentPlayer().isInCheckmate());
        assertFalse(board.currentPlayer().isCastled());
        assertFalse(board.currentPlayer().getOpponent().isInCheck());
        assertFalse(board.currentPlayer().getOpponent().isInCheckmate());
        assertFalse(board.currentPlayer().getOpponent().isCastled());

        //both players should be capable of castling
        assertTrue(board.currentPlayer().isKingSideCastleCapable());
        assertTrue(board.currentPlayer().isQueenSideCastleCapable());
        assertTrue(board.currentPlayer().getOpponent().isKingSideCastleCapable());
        assertTrue(board.currentPlayer().getOpponent().isQueenSideCastleCapable());

        //current player = white, opponent = black
        assertEquals(board.currentPlayer(), board.whitePlayer());
        assertEquals(board.whitePlayer().toString(), "White");
        assertEquals(board.currentPlayer().getOpponent(), board.blackPlayer());
        assertEquals(board.blackPlayer().toString(), "Black");

        //is not end game
        assertFalse(BoardUtils.UTILS.isEndGameScenario(board));

        //no moves is attacking/castling move at all
        final Iterable<Move> allMoves = Iterables.concat(board.whitePlayer().getLegalMoves(), board.blackPlayer().getLegalMoves());
        for (final Move move : allMoves) {
            assertFalse(move.isAttack() || move.isCastlingMove());
        }

        final Move move = MoveFactory.createMove(board, getPieceAtPosition(board, "e2"), BoardUtils.UTILS.getCoordinateAtPosition("e4"));
        final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
        assertEquals(board, moveTransition.getPreviousBoard());
        assertEquals(moveTransition.getLatestBoard().currentPlayer(), moveTransition.getLatestBoard().blackPlayer());
        assertTrue(moveTransition.getMoveStatus().isDone());
    }

    @Test
    public void testKingMove() {
        final Builder builder = new Builder(0, League.WHITE, null);

        builder.setPiece(new Pawn(League.BLACK, 12));
        builder.setPiece(new Pawn(League.WHITE, 52));

        builder.setPiece(new King(League.BLACK, 4, false, false));
        builder.setPiece(new King(League.WHITE, 60, false, false));

        //Only allow 2 pawns and 2 kings on board
        final Board board = builder.build();

        assertEquals(board.currentPlayer(), board.whitePlayer());
        assertEquals(board.currentPlayer().getOpponent(), board.blackPlayer());

        assertEquals(board.currentPlayer().getLegalMoves().size(), 6);
        assertEquals(board.currentPlayer().getOpponent().getLegalMoves().size(), 6);

        assertEquals(board.currentPlayer().getPlayerKing().calculateLegalMoves(board).size(), 4);
        assertEquals(board.currentPlayer().getOpponent().getPlayerKing().calculateLegalMoves(board).size(), 4);

        assertFalse(board.currentPlayer().isInCheck());
        assertFalse(board.currentPlayer().isInCheckmate());
        assertFalse(board.currentPlayer().isCastled());
        assertFalse(board.currentPlayer().getOpponent().isInCheck());
        assertFalse(board.currentPlayer().getOpponent().isInCheckmate());
        assertFalse(board.currentPlayer().getOpponent().isCastled());

        //both players should not be capable of castling as rook does not exists
        assertFalse(board.currentPlayer().isKingSideCastleCapable());
        assertFalse(board.currentPlayer().isQueenSideCastleCapable());
        assertFalse(board.currentPlayer().getOpponent().isKingSideCastleCapable());
        assertFalse(board.currentPlayer().getOpponent().isQueenSideCastleCapable());
    }

    protected static Piece getPieceAtPosition(final Board board, final String position) {
        for (final Piece piece : board.currentPlayer().getActivePieces()) {
            if (piece.getPiecePosition() == BoardUtils.UTILS.getCoordinateAtPosition(position)) {
                return piece;
            }
        }
        throw new RuntimeException("Invalid Piece");
    }

    private static int calculatedActivesFor(final Board board, final League league) {
        int count = 0;
        for (final Piece piece : board.getAllPieces()) {
            if (piece.getLeague().equals(league)) {
                count++;
            }
        }
        return count;
    }

    @Test
    public void testAlgebreicNotation() {
        assertEquals(BoardUtils.UTILS.getPositionAtCoordinate(0), "a8");
        assertEquals(BoardUtils.UTILS.getPositionAtCoordinate(1), "b8");
        assertEquals(BoardUtils.UTILS.getPositionAtCoordinate(2), "c8");
        assertEquals(BoardUtils.UTILS.getPositionAtCoordinate(3), "d8");
        assertEquals(BoardUtils.UTILS.getPositionAtCoordinate(4), "e8");
        assertEquals(BoardUtils.UTILS.getPositionAtCoordinate(5), "f8");
        assertEquals(BoardUtils.UTILS.getPositionAtCoordinate(6), "g8");
        assertEquals(BoardUtils.UTILS.getPositionAtCoordinate(7), "h8");

        assertEquals(0, BoardUtils.UTILS.getCoordinateAtPosition("a8"));
        assertEquals(1, BoardUtils.UTILS.getCoordinateAtPosition("b8"));
        assertEquals(2, BoardUtils.UTILS.getCoordinateAtPosition("c8"));
        assertEquals(3, BoardUtils.UTILS.getCoordinateAtPosition("d8"));
        assertEquals(4, BoardUtils.UTILS.getCoordinateAtPosition("e8"));
        assertEquals(5, BoardUtils.UTILS.getCoordinateAtPosition("f8"));
        assertEquals(6, BoardUtils.UTILS.getCoordinateAtPosition("g8"));
        assertEquals(7, BoardUtils.UTILS.getCoordinateAtPosition("h8"));

    }

    //RunTimeException is thrown when there is no king
    @Test(expected=RuntimeException.class)
    public void testInvalidBoard() {

        final Builder builder = new Builder(0, League.WHITE, null);
        // Black Layout
        builder.setPiece(new Rook(League.BLACK, 0))
        .setPiece(new Knight(League.BLACK, 1))
        .setPiece(new Bishop(League.BLACK, 2))
        .setPiece(new Queen(League.BLACK, 3))
        //No King
        .setPiece(new Bishop(League.BLACK, 5))
        .setPiece(new Knight(League.BLACK, 6))
        .setPiece(new Rook(League.BLACK, 7));
        for (int i = 8; i < 16; i++) {
            builder.setPiece(new Pawn(League.BLACK, i));
        }
        // White Layout
        for (int i = 48; i < 56; i++) {
            builder.setPiece(new Pawn(League.WHITE, i));
        }
        builder.setPiece(new Rook(League.WHITE, 56))
        .setPiece(new Knight(League.WHITE, 57))
        .setPiece(new Bishop(League.WHITE, 58))
        .setPiece(new Queen(League.WHITE, 59))
        //No King
        .setPiece(new Bishop(League.WHITE, 61))
        .setPiece(new Knight(League.WHITE, 62))
        .setPiece(new Rook(League.WHITE, 63))
        //build the board
        .build();
    }

    @Test
    public void testBoardConsistency() {
        final Board board = Board.createStandardBoard(BoardUtils.UTILS.DEFAULT_TIMER_MINUTE, BoardUtils.UTILS.DEFAULT_TIMER_SECOND, BoardUtils.UTILS.DEFAULT_TIMER_MILLISECOND);
        assertEquals(board.currentPlayer(), board.whitePlayer());

        final MoveTransition t1 = board.currentPlayer()
                .makeMove(MoveFactory.createMove(board, getPieceAtPosition(board, "e2"), BoardUtils.UTILS.getCoordinateAtPosition("e4")));
        final MoveTransition t2 = t1.getLatestBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t1.getLatestBoard(), getPieceAtPosition(t1.getLatestBoard(), "e7"), BoardUtils.UTILS.getCoordinateAtPosition("e5")));

        final MoveTransition t3 = t2.getLatestBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t2.getLatestBoard(), getPieceAtPosition(t2.getLatestBoard(), "g1"), BoardUtils.UTILS.getCoordinateAtPosition("f3")));
        final MoveTransition t4 = t3.getLatestBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t3.getLatestBoard(), getPieceAtPosition(t3.getLatestBoard(), "d7"), BoardUtils.UTILS.getCoordinateAtPosition("d5")));

        final MoveTransition t5 = t4.getLatestBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t4.getLatestBoard(), getPieceAtPosition(t4.getLatestBoard(), "e4"), BoardUtils.UTILS.getCoordinateAtPosition("d5")));
        final MoveTransition t6 = t5.getLatestBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t5.getLatestBoard(), getPieceAtPosition(t5.getLatestBoard(), "d8"), BoardUtils.UTILS.getCoordinateAtPosition("d5")));

        final MoveTransition t7 = t6.getLatestBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t6.getLatestBoard(), getPieceAtPosition(t6.getLatestBoard(), "f3"), BoardUtils.UTILS.getCoordinateAtPosition("g5")));
        final MoveTransition t8 = t7.getLatestBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t7.getLatestBoard(), getPieceAtPosition(t7.getLatestBoard(), "f7"), BoardUtils.UTILS.getCoordinateAtPosition("f6")));

        final MoveTransition t9 = t8.getLatestBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t8.getLatestBoard(), getPieceAtPosition(t8.getLatestBoard(), "d1"), BoardUtils.UTILS.getCoordinateAtPosition("h5")));
        final MoveTransition t10 = t9.getLatestBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t9.getLatestBoard(), getPieceAtPosition(t9.getLatestBoard(), "g7"), BoardUtils.UTILS.getCoordinateAtPosition("g6")));

        final MoveTransition t11 = t10.getLatestBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t10.getLatestBoard(), getPieceAtPosition(t10.getLatestBoard(), "h5"), BoardUtils.UTILS.getCoordinateAtPosition("h4")));
        final MoveTransition t12 = t11.getLatestBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t11.getLatestBoard(), getPieceAtPosition(t11.getLatestBoard(), "f6"), BoardUtils.UTILS.getCoordinateAtPosition("g5")));

        final MoveTransition t13 = t12.getLatestBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t12.getLatestBoard(), getPieceAtPosition(t12.getLatestBoard(), "h4"), BoardUtils.UTILS.getCoordinateAtPosition("g5")));
        final MoveTransition t14 = t13.getLatestBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t13.getLatestBoard(), getPieceAtPosition(t13.getLatestBoard(), "d5"), BoardUtils.UTILS.getCoordinateAtPosition("e4")));

        assertEquals(t14.getLatestBoard().whitePlayer().getActivePieces().size(), calculatedActivesFor(t14.getLatestBoard(), League.WHITE));
        assertEquals(t14.getLatestBoard().blackPlayer().getActivePieces().size(), calculatedActivesFor(t14.getLatestBoard(), League.BLACK));
    }
}