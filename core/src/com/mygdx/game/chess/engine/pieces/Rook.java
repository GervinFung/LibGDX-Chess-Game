package com.mygdx.game.chess.engine.pieces;

import com.mygdx.game.chess.engine.League;
import com.mygdx.game.chess.engine.board.Board;
import com.mygdx.game.chess.engine.board.BoardUtils;
import com.mygdx.game.chess.engine.board.Move;
import com.mygdx.game.chess.engine.board.Tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.mygdx.game.chess.engine.board.Move.*;

public final class Rook extends Piece{

    private static final int[] MOVE_VECTOR_COORDINATE = {-8, -1, 1, 8};

    public Rook(final League league, final int piecePosition) {
        super(PieceType.ROOK, piecePosition, league, true);
    }

    public Rook(final League league, final int piecePosition, final boolean isFirstMove) { super(PieceType.ROOK, piecePosition, league, isFirstMove); }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int CoordinateOFFSET : MOVE_VECTOR_COORDINATE) {

            int candidateDestinationCoordinate = this.piecePosition;

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

                    } else if (candidateDestinationTile.isTileOccupied()){
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

        return Collections.unmodifiableList(legalMoves);
    }
    @Override
    public Rook movedPiece(Move move) { return new Rook(move.getMovedPiece().getLeague(), move.getDestinationCoordinate(), false); }

    @Override
    public String toString() {
        return PieceType.ROOK.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOFFSET) {
        return BoardUtils.FIRST_COLUMN.get(currentPosition) && (candidateOFFSET == -1);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOFFSET) {
        return BoardUtils.EIGHTH_COLUMN.get(currentPosition) && (candidateOFFSET == 1);
    }
}