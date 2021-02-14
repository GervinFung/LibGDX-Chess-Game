package com.chess.engine.pieces;

import com.chess.engine.League;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.chess.engine.board.Move.*;

public final class Knight extends Piece{

    private final static int[] MOVE_VECTOR_COORDINATE = {-17, -15, -10, -6, 6, 10, 15, 17};

    public Knight(final League league, final int piecePosition) { super(PieceType.KNIGHT, piecePosition, league, true); }

    public Knight(final League league, final int piecePosition, final boolean isFirstMove) { super(PieceType.KNIGHT, piecePosition, league, isFirstMove); }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final int currentCandidateOFFSET : MOVE_VECTOR_COORDINATE) {

            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOFFSET;

            //not out of bound
            if (BoardUtils.UTILS.isValidTileCoordinate(candidateDestinationCoordinate)) {

                if (isFirstColumnExclusion(this.piecePosition, currentCandidateOFFSET) ||
                    isSecondColumnExclusion(this.piecePosition, currentCandidateOFFSET) ||
                    isSeventhColumnExclusion(this.piecePosition, currentCandidateOFFSET) ||
                    isEighthColumnExclusion(this.piecePosition, currentCandidateOFFSET)) {
                    continue;
                }

                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if (!candidateDestinationTile.isTileOccupied() && this.isLegalMove(board, candidateDestinationCoordinate)) {
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));

                } else if (candidateDestinationTile.isTileOccupied()) {
                    final Piece pieceDestination = candidateDestinationTile.getPiece();
                    final League league = pieceDestination.getLeague();

                    if (this.getLeague() != league && this.isLegalMove(board, candidateDestinationCoordinate)) {
                        legalMoves.add(new MajorAttackMove(board, this, candidateDestinationCoordinate, pieceDestination));
                    }
                }
            }
        }

        return Collections.unmodifiableList(legalMoves);
    }

    @Override
    public Knight movedPiece(Move move) { return new Knight(move.getMovedPiece().getLeague(), move.getDestinationCoordinate(), false); }

    @Override
    public String toString() {
        return PieceType.KNIGHT.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOFFSET) {
        return BoardUtils.UTILS.FIRST_COLUMN.get(currentPosition) && (candidateOFFSET == -17 || candidateOFFSET == -10 || candidateOFFSET == 6 || candidateOFFSET == 15);
    }
    private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOFFSET) {
        return BoardUtils.UTILS.SECOND_COLUMN.get(currentPosition) && (candidateOFFSET == -10 || candidateOFFSET == 6);
    }
    private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOFFSET) {
        return BoardUtils.UTILS.SEVENTH_COLUMN.get(currentPosition) && (candidateOFFSET == -6 || candidateOFFSET == 10);
    }
    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOFFSET) {
        return BoardUtils.UTILS.EIGHTH_COLUMN.get(currentPosition) && (candidateOFFSET == -15 || candidateOFFSET == -6 || candidateOFFSET == 10 || candidateOFFSET == 17);
    }
}