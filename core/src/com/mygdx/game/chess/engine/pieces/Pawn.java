package com.mygdx.game.chess.engine.pieces;

import com.mygdx.game.chess.engine.League;
import com.mygdx.game.chess.engine.board.Board;
import com.mygdx.game.chess.engine.board.BoardUtils;
import com.mygdx.game.chess.engine.board.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.mygdx.game.chess.engine.board.Move.*;

public final class Pawn extends Piece {

    private static final int[] MOVE_VECTOR_COORDINATE = {8, 16, 7, 9};

    public Pawn(final League league, final int piecePosition) {
        super(PieceType.PAWN, piecePosition, league, true);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidateOFFSET : MOVE_VECTOR_COORDINATE) {

            int candidateDestinationCoordinate = this.piecePosition + (currentCandidateOFFSET * this.getLeague().getDirection());

            if (!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                continue;
            }
            if (currentCandidateOFFSET == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {

                if (this.league.isPawnPromotionSquare(candidateDestinationCoordinate) && this.isLegalMove(board, candidateDestinationCoordinate)) {

                    final PawnMove pawnMove = new PawnMove(board, this, candidateDestinationCoordinate);

                    legalMoves.add(new PawnPromotion(pawnMove, new Queen(this.league, candidateDestinationCoordinate, false)));
                    legalMoves.add(new PawnPromotion(pawnMove, new Rook(this.league, candidateDestinationCoordinate, false)));
                    legalMoves.add(new PawnPromotion(pawnMove, new Bishop(this.league, candidateDestinationCoordinate, false)));
                    legalMoves.add(new PawnPromotion(pawnMove, new Knight(this.league, candidateDestinationCoordinate, false)));

                } else if (!this.league.isPawnPromotionSquare(candidateDestinationCoordinate) && this.isLegalMove(board, candidateDestinationCoordinate)){
                    legalMoves.add(new PawnMove(board, this, candidateDestinationCoordinate));
                }


            } else if (currentCandidateOFFSET == 16 && this.isFirstMove() &&
                    ((BoardUtils.SECOND_ROW.get(this.piecePosition) && this.getLeague().isBlack()) ||
                            (BoardUtils.SEVENTH_ROW.get(this.piecePosition) && this.getLeague().isWhite()))) {

                final int behindCandidateDestinationCoordinate = this.piecePosition + (this.getLeague().getDirection() * 8);
                if (!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() &&
                        !board.getTile(candidateDestinationCoordinate).isTileOccupied() &&
                        this.isLegalMove(board, candidateDestinationCoordinate)) {
                    legalMoves.add(new PawnJump(board, this, candidateDestinationCoordinate));
                }
            } else if (currentCandidateOFFSET == 7 &&
                    !((BoardUtils.EIGHTH_COLUMN.get(this.piecePosition) && this.league.isWhite()) ||
                            (BoardUtils.FIRST_COLUMN.get(this.piecePosition) && this.league.isBlack()))) {
                if (board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    final Piece pieceDestination = board.getTile(candidateDestinationCoordinate).getPiece();

                    if (this.league != pieceDestination.getLeague() && this.isLegalMove(board, candidateDestinationCoordinate)) {

                        if (this.league.isPawnPromotionSquare(candidateDestinationCoordinate)) {

                            final PawnAttackMove pawnAttackMove = new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceDestination);

                            legalMoves.add(new PawnPromotion(pawnAttackMove, new Queen(this.league, candidateDestinationCoordinate, false)));
                            legalMoves.add(new PawnPromotion(pawnAttackMove, new Rook(this.league, candidateDestinationCoordinate, false)));
                            legalMoves.add(new PawnPromotion(pawnAttackMove, new Bishop(this.league, candidateDestinationCoordinate, false)));
                            legalMoves.add(new PawnPromotion(pawnAttackMove, new Knight(this.league, candidateDestinationCoordinate, false)));

                        } else {
                            legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceDestination));
                        }

                    }
                } else if (board.getEnPassantPawn() != null) {

                    if (board.getEnPassantPawn().getPiecePosition() == (this.piecePosition + (this.league.getOppositeDirection()))) {
                        final Piece pieceDestination = board.getEnPassantPawn();

                        if (this.league != pieceDestination.getLeague() && this.isLegalMove(board, candidateDestinationCoordinate)) {
                            legalMoves.add(new PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceDestination));
                        }
                    }
                }

            } else if (currentCandidateOFFSET == 9 &&
                    !((BoardUtils.FIRST_COLUMN.get(this.piecePosition) && this.league.isWhite()) ||
                            (BoardUtils.EIGHTH_COLUMN.get(this.piecePosition) && this.league.isBlack()))) {
                if (board.getTile(candidateDestinationCoordinate).isTileOccupied()) {

                    final Piece pieceDestination = board.getTile(candidateDestinationCoordinate).getPiece();

                    if (this.league != pieceDestination.getLeague() && this.isLegalMove(board, candidateDestinationCoordinate)) {

                        if (this.league.isPawnPromotionSquare(candidateDestinationCoordinate)) {

                            final PawnAttackMove pawnAttackMove = new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceDestination);

                            legalMoves.add(new PawnPromotion(pawnAttackMove, new Queen(this.league, candidateDestinationCoordinate, false)));
                            legalMoves.add(new PawnPromotion(pawnAttackMove, new Rook(this.league, candidateDestinationCoordinate, false)));
                            legalMoves.add(new PawnPromotion(pawnAttackMove, new Bishop(this.league, candidateDestinationCoordinate, false)));
                            legalMoves.add(new PawnPromotion(pawnAttackMove, new Knight(this.league, candidateDestinationCoordinate, false)));

                        } else {
                            legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceDestination));
                        }
                    }
                } else if (board.getEnPassantPawn() != null) {

                    if (board.getEnPassantPawn().getPiecePosition() == (this.piecePosition - (this.league.getOppositeDirection()))) {
                        final Piece pieceDestination = board.getEnPassantPawn();

                        if (this.league != pieceDestination.getLeague() && this.isLegalMove(board, candidateDestinationCoordinate)) {
                            legalMoves.add(new PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceDestination));
                        }
                    }
                }

            }
        }
        return Collections.unmodifiableList(legalMoves);
    }

    @Override
    public Pawn movedPiece(final Move move) { return new Pawn(move.getMovedPiece().getLeague(), move.getDestinationCoordinate()); }

    @Override
    public String toString() {
        return PieceType.PAWN.toString();
    }

    public List<Piece> getPromotionPieces(final int destinationCoordinate) {
        return Collections.unmodifiableList(Arrays.asList(new Queen(this.league, destinationCoordinate, false), new Rook(this.league, destinationCoordinate, false), new Bishop(this.league, destinationCoordinate, false), new Knight(this.league, destinationCoordinate, false)));
    }
}