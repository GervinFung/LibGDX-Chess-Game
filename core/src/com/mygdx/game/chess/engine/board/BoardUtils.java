package com.mygdx.game.chess.engine.board;

import com.mygdx.game.chess.engine.pieces.Piece;
import com.mygdx.game.chess.engine.pieces.PieceType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class BoardUtils {

    private BoardUtils() { throw new RuntimeException("Cannot instantiate BoardUtils"); }

    public final static List<Boolean> FIRST_COLUMN = initColumn(0);
    public final static List<Boolean> SECOND_COLUMN = initColumn(1);
    public final static List<Boolean> SEVENTH_COLUMN = initColumn(6);
    public final static List<Boolean> EIGHTH_COLUMN = initColumn(7);

    public final static List<Boolean> FIRST_ROW = initRow(0);
    public final static List<Boolean> SECOND_ROW = initRow(8);
    public final static List<Boolean> THIRD_ROW = initRow(16);
    public final static List<Boolean> FIFTH_ROW = initRow(32);
    public final static List<Boolean> SEVENTH_ROW = initRow(48);
    public final static List<Boolean> EIGHTH_ROW = initRow(56);

    public final static List<String> ALGEBRAIC_NOTATION = initializeAlgebraicNotation();
    public final static Map<String, Integer> POSITION_TO_COORDINATE = initializePositionToCoordinateMap();

    public final static int NUM_TILES = 64, NUM_TILES_PER_ROW = 8;
    public final static int DEFAULT_TIMER_MINUTE = 5, DEFAULT_TIMER_SECOND = 0, DEFAULT_TIMER_MILLISECOND = 0;

    //Method
    public static boolean isValidTileCoordinate(final int coordinate) { return coordinate >= 0 && coordinate < NUM_TILES; }
    public static String getPositionAtCoordinate(final int destinationCoordinate) { return ALGEBRAIC_NOTATION.get(destinationCoordinate); }
    public static int getCoordinateAtPosition(final String destinationPosition) { return POSITION_TO_COORDINATE.get(destinationPosition); }
    public static boolean kingThreat(final Move move) { return move.getBoard().currentPlayer().makeMove(move).getLatestBoard().currentPlayer().isInCheck(); }
    public static boolean isEndGameScenario(final Board board) { return board.currentPlayer().isInCheckmate() || board.currentPlayer().isInStalemate(); }

    private static List<String> initializeAlgebraicNotation() {
        return Collections.unmodifiableList(Arrays.asList(
                "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"));
    }

    private static List<Boolean> initColumn(int columnNumber) {
        final boolean[] columns = new boolean[NUM_TILES];
        do {
            columns[columnNumber] = true;
            columnNumber += NUM_TILES_PER_ROW;
        } while (columnNumber < NUM_TILES);
        final List<Boolean> columnList = new ArrayList<>(NUM_TILES);
        for (final boolean column : columns) {
            columnList.add(column);
        }
        return Collections.unmodifiableList(columnList);
    }

    private static List<Boolean> initRow(int rowNumber) {
        final boolean[] rows = new boolean[NUM_TILES];
        do {
            rows[rowNumber] = true;
            rowNumber ++;
        } while (rowNumber % NUM_TILES_PER_ROW != 0);
        final List<Boolean> rowList = new ArrayList<>(NUM_TILES);
        for (final boolean row : rows) {
            rowList.add(row);
        }
        return Collections.unmodifiableList(rowList);
    }

    public static int mostValuableVictimLeastValuableAggressor(final Move move) {
        final Piece movingPiece = move.getMovedPiece();
        if(move.isAttack()) {
            final Piece attackedPiece = move.getAttackedPiece();
            return (attackedPiece.getPieceValue() - movingPiece.getPieceValue() +  PieceType.KING.getPieceValue()) * 100;
        }
        return PieceType.KING.getPieceValue() - movingPiece.getPieceValue();
    }

    public static List<Move> lastNMoves(final Board board, int N) {
        final List<Move> moveHistory = new ArrayList<>();
        Move currentMove = board.getTransitionMove();
        int i = 0;
        while(currentMove != Move.MoveFactory.getNullMove() && i < N) {
            moveHistory.add(currentMove);
            currentMove = currentMove.getBoard().getTransitionMove();
            i++;
        }
        return Collections.unmodifiableList(moveHistory);
    }

    private static Map<String, Integer> initializePositionToCoordinateMap() {
        final Map<String, Integer> positionToCoordinate = new HashMap<>(64);
        for (int i = 0; i < NUM_TILES; i++) {
            assert ALGEBRAIC_NOTATION.get(i) != null;
            positionToCoordinate.put(ALGEBRAIC_NOTATION.get(i), i);
        }
        return Collections.unmodifiableMap(positionToCoordinate);
    }
}