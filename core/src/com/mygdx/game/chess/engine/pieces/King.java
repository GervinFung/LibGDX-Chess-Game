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

public final class King extends Piece {

    private static final int[] MOVE_VECTOR_COORDINATE = {-9, -8, -7, -1, 1, 7, 8, 9};
    private final boolean isCastled;
    private final boolean kingSideCastleCapable;
    private final boolean queenSideCastleCapable;

    public King(final League league, final int piecePosition, final boolean isFirstMove,
                final boolean isCastled, final boolean kingSideCastleCapable, final boolean queenSideCastleCapable) {
        super(PieceType.KING, piecePosition, league, isFirstMove);
        this.isCastled = isCastled;
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;
    }

    public King(final League league, final int piecePosition, final boolean kingSideCastleCapable, final boolean queenSideCastleCapable) {
        super(PieceType.KING, piecePosition, league, true);
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;
        this.isCastled = false;
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOFFSET) {
        return BoardUtils.FIRST_COLUMN.get(currentPosition) && (candidateOFFSET == -9 || candidateOFFSET == 7 || candidateOFFSET == -1);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOFFSET) {
        return BoardUtils.EIGHTH_COLUMN.get(currentPosition) && (candidateOFFSET == 9 || candidateOFFSET == -7 || candidateOFFSET == 1);
    }

    public boolean isCastled() {
        return this.isCastled;
    }

    @Override
    public ImmutableList<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final int currentCandidateOFFSET : MOVE_VECTOR_COORDINATE) {

            final int candidateDestinationCoordinate = super.getPiecePosition() + currentCandidateOFFSET;

            if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {

                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

                if (isFirstColumnExclusion(super.getPiecePosition(), currentCandidateOFFSET) ||
                        isEighthColumnExclusion(super.getPiecePosition(), currentCandidateOFFSET)) {
                    continue;
                }

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
        if (!this.isCastled && board.currentPlayer() != null) {
            legalMoves.addAll(board.currentPlayer().calculateKingCastles(board.currentPlayer().getOpponent().getLegalMoves()));
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public King movedPiece(final Move move) {
        return new King(move.getMovedPiece().getLeague(), move.getDestinationCoordinate(), false, move.isCastlingMove(), false, false);
    }

    @Override
    public String toString() {
        return PieceType.KING.toString();
    }
}