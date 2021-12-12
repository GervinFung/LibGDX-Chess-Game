package com.mygdx.game.chess.engine.pieces;

import static com.mygdx.game.chess.engine.board.Move.PawnAttackMove;
import static com.mygdx.game.chess.engine.board.Move.PawnEnPassantAttackMove;
import static com.mygdx.game.chess.engine.board.Move.PawnJump;
import static com.mygdx.game.chess.engine.board.Move.PawnMove;
import static com.mygdx.game.chess.engine.board.Move.PawnPromotion;

import com.google.common.collect.ImmutableList;
import com.mygdx.game.chess.engine.League;
import com.mygdx.game.chess.engine.board.Board;
import com.mygdx.game.chess.engine.board.BoardUtils;
import com.mygdx.game.chess.engine.board.Move;

import java.util.ArrayList;
import java.util.List;

public final class Pawn extends Piece {

    private static final int[] MOVE_VECTOR_COORDINATE = {8, 16, 7, 9};

    public Pawn(final League league, final int piecePosition) {
        super(PieceType.PAWN, piecePosition, league, true);
    }

    @Override
    public ImmutableList<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidateOFFSET : MOVE_VECTOR_COORDINATE) {

            int candidateDestinationCoordinate = super.getPiecePosition() + (currentCandidateOFFSET * this.getLeague().getDirection());

            if (!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                continue;
            }
            if (currentCandidateOFFSET == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {

                if (super.getLeague().isPawnPromotionSquare(candidateDestinationCoordinate) && this.isLegalMove(board, candidateDestinationCoordinate)) {

                    final PawnMove pawnMove = new PawnMove(board, this, candidateDestinationCoordinate);

                    legalMoves.add(new PawnPromotion(pawnMove, new Queen(super.getLeague(), candidateDestinationCoordinate, false)));
                    legalMoves.add(new PawnPromotion(pawnMove, new Rook(super.getLeague(), candidateDestinationCoordinate, false)));
                    legalMoves.add(new PawnPromotion(pawnMove, new Bishop(super.getLeague(), candidateDestinationCoordinate, false)));
                    legalMoves.add(new PawnPromotion(pawnMove, new Knight(super.getLeague(), candidateDestinationCoordinate, false)));

                } else if (!super.getLeague().isPawnPromotionSquare(candidateDestinationCoordinate) && this.isLegalMove(board, candidateDestinationCoordinate)) {
                    legalMoves.add(new PawnMove(board, this, candidateDestinationCoordinate));
                }


            } else if (currentCandidateOFFSET == 16 && this.isFirstMove() &&
                    ((BoardUtils.SECOND_ROW.get(super.getPiecePosition()) && this.getLeague().isBlack()) ||
                            (BoardUtils.SEVENTH_ROW.get(super.getPiecePosition()) && this.getLeague().isWhite()))) {

                final int behindCandidateDestinationCoordinate = super.getPiecePosition() + (this.getLeague().getDirection() * 8);
                if (!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() &&
                        !board.getTile(candidateDestinationCoordinate).isTileOccupied() &&
                        this.isLegalMove(board, candidateDestinationCoordinate)) {
                    legalMoves.add(new PawnJump(board, this, candidateDestinationCoordinate));
                }
            } else if (currentCandidateOFFSET == 7 &&
                    !((BoardUtils.EIGHTH_COLUMN.get(super.getPiecePosition()) && super.getLeague().isWhite()) ||
                            (BoardUtils.FIRST_COLUMN.get(super.getPiecePosition()) && super.getLeague().isBlack()))) {
                if (board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    final Piece pieceDestination = board.getTile(candidateDestinationCoordinate).getPiece();

                    if (super.getLeague() != pieceDestination.getLeague() && this.isLegalMove(board, candidateDestinationCoordinate)) {

                        if (super.getLeague().isPawnPromotionSquare(candidateDestinationCoordinate)) {

                            final PawnAttackMove pawnAttackMove = new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceDestination);

                            legalMoves.add(new PawnPromotion(pawnAttackMove, new Queen(super.getLeague(), candidateDestinationCoordinate, false)));
                            legalMoves.add(new PawnPromotion(pawnAttackMove, new Rook(super.getLeague(), candidateDestinationCoordinate, false)));
                            legalMoves.add(new PawnPromotion(pawnAttackMove, new Bishop(super.getLeague(), candidateDestinationCoordinate, false)));
                            legalMoves.add(new PawnPromotion(pawnAttackMove, new Knight(super.getLeague(), candidateDestinationCoordinate, false)));

                        } else {
                            legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceDestination));
                        }

                    }
                } else if (board.getEnPassantPawn() != null) {

                    if (board.getEnPassantPawn().getPiecePosition() == (super.getPiecePosition() + (super.getLeague().getOppositeDirection()))) {
                        final Piece pieceDestination = board.getEnPassantPawn();

                        if (super.getLeague() != pieceDestination.getLeague() && this.isLegalMove(board, candidateDestinationCoordinate)) {
                            legalMoves.add(new PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceDestination));
                        }
                    }
                }

            } else if (currentCandidateOFFSET == 9 &&
                    !((BoardUtils.FIRST_COLUMN.get(super.getPiecePosition()) && super.getLeague().isWhite()) ||
                            (BoardUtils.EIGHTH_COLUMN.get(super.getPiecePosition()) && super.getLeague().isBlack()))) {
                if (board.getTile(candidateDestinationCoordinate).isTileOccupied()) {

                    final Piece pieceDestination = board.getTile(candidateDestinationCoordinate).getPiece();

                    if (super.getLeague() != pieceDestination.getLeague() && this.isLegalMove(board, candidateDestinationCoordinate)) {

                        if (super.getLeague().isPawnPromotionSquare(candidateDestinationCoordinate)) {

                            final PawnAttackMove pawnAttackMove = new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceDestination);

                            legalMoves.add(new PawnPromotion(pawnAttackMove, new Queen(super.getLeague(), candidateDestinationCoordinate, false)));
                            legalMoves.add(new PawnPromotion(pawnAttackMove, new Rook(super.getLeague(), candidateDestinationCoordinate, false)));
                            legalMoves.add(new PawnPromotion(pawnAttackMove, new Bishop(super.getLeague(), candidateDestinationCoordinate, false)));
                            legalMoves.add(new PawnPromotion(pawnAttackMove, new Knight(super.getLeague(), candidateDestinationCoordinate, false)));

                        } else {
                            legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceDestination));
                        }
                    }
                } else if (board.getEnPassantPawn() != null) {

                    if (board.getEnPassantPawn().getPiecePosition() == (super.getPiecePosition() - (super.getLeague().getOppositeDirection()))) {
                        final Piece pieceDestination = board.getEnPassantPawn();

                        if (super.getLeague() != pieceDestination.getLeague() && this.isLegalMove(board, candidateDestinationCoordinate)) {
                            legalMoves.add(new PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceDestination));
                        }
                    }
                }

            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Pawn movedPiece(final Move move) {
        return new Pawn(move.getMovedPiece().getLeague(), move.getDestinationCoordinate());
    }

    @Override
    public String toString() {
        return PieceType.PAWN.toString();
    }

    public ImmutableList<Piece> getPromotionPieces(final int destinationCoordinate) {
        return ImmutableList.of(new Queen(super.getLeague(), destinationCoordinate, false), new Rook(super.getLeague(), destinationCoordinate, false), new Bishop(super.getLeague(), destinationCoordinate, false), new Knight(super.getLeague(), destinationCoordinate, false));
    }
}