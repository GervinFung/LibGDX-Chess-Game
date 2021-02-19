package com.mygdx.game.chess.engine.pieces;

import com.mygdx.game.chess.engine.League;
import com.mygdx.game.chess.engine.board.Board;
import com.mygdx.game.chess.engine.board.BoardUtils;
import com.mygdx.game.chess.engine.board.Move;
import com.mygdx.game.chess.engine.board.Tile;
import com.mygdx.game.chess.engine.player.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.mygdx.game.chess.engine.board.Move.*;

public final class King extends Piece{

    private final boolean isCastled;
    private final boolean kingSideCastleCapable;
    private final boolean queenSideCastleCapable;

    private static final int[] MOVE_VECTOR_COORDINATE = {-9, -8, -7, -1, 1, 7, 8, 9};
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

    public boolean isCastled() {
        return this.isCastled;
    }

    private Collection<Move> calculateKingCastle(final Board board, final Collection<Move> opponentLegals) {

        final List<Move> kingCastle = new ArrayList<>();
        if (this.isFirstMove() && !board.currentPlayer().isInCheck()) {
            if (this.kingSideCastleCapable) {
                //king side
                if (!board.getTile(this.getLeague().isWhite() ? 61 : 5).isTileOccupied() && !board.getTile(this.getLeague().isWhite() ? 62 : 6).isTileOccupied()) {

                    final Tile rookTile = board.getTile(this.getLeague().isWhite() ? 63 : 7);

                    if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                        if (Player.calculateAttacksOnTile(this.getLeague().isWhite() ? 61 : 5, opponentLegals).isEmpty() &&
                                Player.calculateAttacksOnTile(this.getLeague().isWhite() ? 62 : 6, opponentLegals).isEmpty() &&
                                rookTile.getPiece().getPieceType().isRook()) {

                            kingCastle.add(new KingSideCastleMove(board, this, this.getLeague().isWhite() ? 62 : 6, (Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), this.getLeague().isWhite() ? 61 : 5));
                        }
                    }
                }
            }
            if (this.queenSideCastleCapable) {
                //QUEEN side
                if (!board.getTile(this.getLeague().isWhite() ? 57 : 1).isTileOccupied() && !board.getTile(this.getLeague().isWhite() ? 58 : 2).isTileOccupied() && !board.getTile(this.getLeague().isWhite() ? 59 : 3).isTileOccupied()) {

                    final Tile rookTile = board.getTile(this.getLeague().isWhite() ? 56 : 0);

                    if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                        if (Player.calculateAttacksOnTile(this.getLeague().isWhite() ? 58 : 2, opponentLegals).isEmpty() &&
                                Player.calculateAttacksOnTile(this.getLeague().isWhite() ? 59 : 3, opponentLegals).isEmpty() &&
                                rookTile.getPiece().getPieceType().isRook()) {

                            kingCastle.add(new QueenSideCastleMove(board, this, this.getLeague().isWhite() ? 58 : 2, (Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), this.getLeague().isWhite() ? 59 : 3));
                        }
                    }
                }
            }
        }

        return Collections.unmodifiableList(kingCastle);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final int currentCandidateOFFSET : MOVE_VECTOR_COORDINATE) {

            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOFFSET;

            if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {

                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

                if (isFirstColumnExclusion(this.piecePosition, currentCandidateOFFSET) ||
                        isEighthColumnExclusion(this.piecePosition, currentCandidateOFFSET)) {
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
        if (!this.isCastled) {
            try {
                legalMoves.addAll(calculateKingCastle(board, board.currentPlayer().getOpponent().getLegalMoves()));
            } catch (final NullPointerException ignored) {}
        }
        return Collections.unmodifiableList(legalMoves);
    }

    @Override
    public King movedPiece(final Move move) {
        return new King(move.getMovedPiece().getLeague(), move.getDestinationCoordinate(), false, move.isCastlingMove(), false, false);
    }

    @Override
    public String toString() { return PieceType.KING.toString(); }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOFFSET) {
        return BoardUtils.FIRST_COLUMN.get(currentPosition) && (candidateOFFSET == -9 || candidateOFFSET == 7 || candidateOFFSET == -1);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOFFSET) {
        return BoardUtils.EIGHTH_COLUMN.get(currentPosition) && (candidateOFFSET == 9 || candidateOFFSET == -7 || candidateOFFSET == 1);
    }
}