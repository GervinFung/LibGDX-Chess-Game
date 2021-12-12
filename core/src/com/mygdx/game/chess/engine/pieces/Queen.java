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

public final class Queen extends Piece {

    private static final int[] MOVE_VECTOR_COORDINATE = {-9, -8, -7, -1, 1, 7, 8, 9};

    public Queen(final League league, final int piecePosition) {
        super(PieceType.QUEEN, piecePosition, league, true);
    }

    public Queen(final League league, final int piecePosition, final boolean isFirstMove) {
        super(PieceType.QUEEN, piecePosition, league, isFirstMove);
    }

    @Override
    public ImmutableList<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int CoordinateOFFSET : MOVE_VECTOR_COORDINATE) {

            int candidateDestinationCoordinate = super.getPiecePosition();

            while (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {

                if (isEighthColumnExclusion(candidateDestinationCoordinate, CoordinateOFFSET) ||
                        isFirstColumnExclusion(candidateDestinationCoordinate, CoordinateOFFSET)) {
                    break;
                }

                candidateDestinationCoordinate += CoordinateOFFSET;

                if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

                    if (!candidateDestinationTile.isTileOccupied() && this.isLegalMove(board, candidateDestinationCoordinate)) {
                        legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));

                    } else if (candidateDestinationTile.isTileOccupied()) {
                        final Piece pieceDestination = candidateDestinationTile.getPiece();
                        final League league = pieceDestination.getLeague();

                        if (this.getLeague() != league && this.isLegalMove(board, candidateDestinationCoordinate)) {
                            legalMoves.add(new MajorAttackMove(board, this, candidateDestinationCoordinate, pieceDestination));
                        }
                        break;
                    }
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Queen movedPiece(Move move) {
        return new Queen(move.getMovedPiece().getLeague(), move.getDestinationCoordinate(), false);
    }

    @Override
    public String toString() {
        return PieceType.QUEEN.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOFFSET) {
        return BoardUtils.FIRST_COLUMN.get(currentPosition) && (candidateOFFSET == -9 || candidateOFFSET == 7 || candidateOFFSET == -1);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOFFSET) {
        return BoardUtils.EIGHTH_COLUMN.get(currentPosition) && (candidateOFFSET == 9 || candidateOFFSET == -7 || candidateOFFSET == 1);
    }
}