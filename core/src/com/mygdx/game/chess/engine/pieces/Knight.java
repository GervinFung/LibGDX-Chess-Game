package com.mygdx.game.chess.engine.pieces;

import static com.mygdx.game.chess.engine.board.Move.MajorAttackMove;
import static com.mygdx.game.chess.engine.board.Move.MajorMove;

import com.google.common.collect.ImmutableList;
import com.mygdx.game.chess.engine.League;
import com.mygdx.game.chess.engine.board.Board;
import com.mygdx.game.chess.engine.board.BoardUtils;
import com.mygdx.game.chess.engine.board.Move;
import com.mygdx.game.chess.engine.board.Tile;

import java.util.ArrayList;
import java.util.List;

public final class Knight extends Piece {

    private static final int[] MOVE_VECTOR_COORDINATE = {-17, -15, -10, -6, 6, 10, 15, 17};

    public Knight(final League league, final int piecePosition) {
        super(PieceType.KNIGHT, piecePosition, league, true);
    }

    public Knight(final League league, final int piecePosition, final boolean isFirstMove) {
        super(PieceType.KNIGHT, piecePosition, league, isFirstMove);
    }

    @Override
    public ImmutableList<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final int currentCandidateOFFSET : MOVE_VECTOR_COORDINATE) {

            final int candidateDestinationCoordinate = super.getPiecePosition() + currentCandidateOFFSET;

            //not out of bound
            if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {

                if (isFirstColumnExclusion(super.getPiecePosition(), currentCandidateOFFSET) ||
                        isSecondColumnExclusion(super.getPiecePosition(), currentCandidateOFFSET) ||
                        isSeventhColumnExclusion(super.getPiecePosition(), currentCandidateOFFSET) ||
                        isEighthColumnExclusion(super.getPiecePosition(), currentCandidateOFFSET)) {
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

        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Knight movedPiece(Move move) {
        return new Knight(move.getMovedPiece().getLeague(), move.getDestinationCoordinate(), false);
    }

    @Override
    public String toString() {
        return PieceType.KNIGHT.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOFFSET) {
        return BoardUtils.FIRST_COLUMN.get(currentPosition) && (candidateOFFSET == -17 || candidateOFFSET == -10 || candidateOFFSET == 6 || candidateOFFSET == 15);
    }

    private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOFFSET) {
        return BoardUtils.SECOND_COLUMN.get(currentPosition) && (candidateOFFSET == -10 || candidateOFFSET == 6);
    }

    private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOFFSET) {
        return BoardUtils.SEVENTH_COLUMN.get(currentPosition) && (candidateOFFSET == -6 || candidateOFFSET == 10);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOFFSET) {
        return BoardUtils.EIGHTH_COLUMN.get(currentPosition) && (candidateOFFSET == -15 || candidateOFFSET == -6 || candidateOFFSET == 10 || candidateOFFSET == 17);
    }
}