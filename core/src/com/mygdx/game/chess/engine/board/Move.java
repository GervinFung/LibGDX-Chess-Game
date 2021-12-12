package com.mygdx.game.chess.engine.board;

import com.mygdx.game.chess.engine.pieces.Pawn;
import com.mygdx.game.chess.engine.pieces.Piece;
import com.mygdx.game.chess.engine.pieces.Rook;

public abstract class Move {

    private final Board board;
    private final Piece movePiece;
    private final int destinationCoordinate;
    private final boolean isFirstMove;

    private Move(final Board board, final Piece movePiece, final int destinationCoordinate) {
        this.board = board;
        this.movePiece = movePiece;
        this.destinationCoordinate = destinationCoordinate;
        this.isFirstMove = movePiece != null && movePiece.isFirstMove();
    }

    private Move(final Board board, final int destinationCoordinate) {
        this(board, null, destinationCoordinate);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.destinationCoordinate;
        result = 31 * result + this.movePiece.hashCode();
        result = 31 * result + this.movePiece.getPiecePosition();
        result = result + (isFirstMove ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(final Object object) {

        if (this == object) {
            return true;
        }

        if (!(object instanceof Move)) {
            return false;
        }

        final Move otherMove = (Move) object;
        return this.getCurrentCoordinate() == otherMove.getCurrentCoordinate() &&
                this.getDestinationCoordinate() == otherMove.getDestinationCoordinate() &&
                this.getMovedPiece().equals(otherMove.getMovedPiece());
    }


    public final Board getBoard() {
        return this.board;
    }

    public final int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    public final Piece getMovedPiece() {
        return this.movePiece;
    }

    public final boolean isFirstMove() {
        return this.isFirstMove;
    }

    public int getCurrentCoordinate() {
        return this.getMovedPiece().getPiecePosition();
    }

    public boolean isAttack() {
        return false;
    }

    public boolean isPromotionMove() {
        return false;
    }

    public boolean isCastlingMove() {
        return false;
    }

    public Piece getAttackedPiece() {
        return null;
    }

    public Board execute() {

        final Board.Builder builder = new Board.Builder(this.board.getMoveCount() + 1, this.board.currentPlayer().getOpponent().getLeague(), null)
                .updateWhiteTimer(this.board.whitePlayer().getMinute(), this.board.whitePlayer().getSecond(), this.board.whitePlayer().getMillisecond())
                .updateBlackTimer(this.board.blackPlayer().getMinute(), this.board.blackPlayer().getSecond(), this.board.blackPlayer().getMillisecond());

        this.board.currentPlayer().getActivePieces().forEach(piece -> {
            if (!this.movePiece.equals(piece)) {
                builder.setPiece(piece);
            }
        });
        this.board.currentPlayer().getOpponent().getActivePieces().forEach(builder::setPiece);

        builder.setPiece(this.movePiece.movedPiece(this));
        builder.setTransitionMove(this);

        return builder.build();
    }

    public static final class MajorMove extends Move {

        public MajorMove(final Board board, final Piece movePiece, final int destinationCoordinate) {
            super(board, movePiece, destinationCoordinate);
        }

        @Override
        public boolean equals(final Object object) {
            return this == object || object instanceof MajorMove && super.equals(object);
        }

        @Override
        public String toString() {
            return getMovedPiece().getPieceType().toString() + BoardUtils.getPositionAtCoordinate(super.getDestinationCoordinate());
        }
    }

    public static class AttackMove extends Move {

        private final Piece attackedPiece;

        public AttackMove(final Board board, final Piece movePiece, final int destinationCoordinate, final Piece attackedPiece) {
            super(board, movePiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public int hashCode() {
            return this.attackedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(final Object object) {
            if (this == object) {
                return true;
            }

            if (!(object instanceof AttackMove)) {
                return false;
            }

            final AttackMove otherAttackMove = (AttackMove) object;
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }

        @Override
        public boolean isAttack() {
            return true;
        }

        @Override
        public Piece getAttackedPiece() {
            return this.attackedPiece;
        }
    }

    public static final class MajorAttackMove extends AttackMove {
        public MajorAttackMove(final Board board, final Piece piece, final int destinationCoordinate, final Piece pieceAttacked) {
            super(board, piece, destinationCoordinate, pieceAttacked);
        }

        @Override
        public boolean equals(final Object object) {
            return this == object || object instanceof MajorAttackMove && super.equals(object);
        }

        @Override
        public String toString() {
            return getMovedPiece().getPieceType() + BoardUtils.getPositionAtCoordinate(super.getDestinationCoordinate()) + "x" + this.getAttackedPiece();
        }
    }

    public static final class PawnMove extends Move {

        public PawnMove(final Board board, final Piece movePiece, final int destinationCoordinate) {
            super(board, movePiece, destinationCoordinate);
        }

        @Override
        public boolean equals(final Object object) {
            return this == object || object instanceof PawnMove && super.equals(object);
        }

        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(super.getDestinationCoordinate());
        }
    }

    public static class PawnAttackMove extends AttackMove {

        public PawnAttackMove(final Board board, final Piece movePiece, final int destinationCoordinate, final Piece attackedPiece) {
            super(board, movePiece, destinationCoordinate, attackedPiece);
        }

        @Override
        public boolean equals(final Object object) {
            return this == object || object instanceof PawnAttackMove && super.equals(object);
        }

        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(super.getMovedPiece().getPiecePosition()).charAt(0) + "x" + BoardUtils.getPositionAtCoordinate(super.getDestinationCoordinate());
        }
    }

    public static final class PawnEnPassantAttackMove extends PawnAttackMove {

        public PawnEnPassantAttackMove(final Board board, final Piece movePiece, final int destinationCoordinate, final Piece attackedPiece) {
            super(board, movePiece, destinationCoordinate, attackedPiece);
        }

        @Override
        public boolean equals(final Object object) {
            return this == object || object instanceof PawnEnPassantAttackMove && super.equals(object);
        }

        @Override
        public Board execute() {
            final Board.Builder builder = new Board.Builder(super.getBoard().getMoveCount() + 1, super.getBoard().currentPlayer().getOpponent().getLeague(), null)
                    .updateWhiteTimer(super.getBoard().whitePlayer().getMinute(), super.getBoard().whitePlayer().getSecond(), super.getBoard().whitePlayer().getMillisecond())
                    .updateBlackTimer(super.getBoard().blackPlayer().getMinute(), super.getBoard().blackPlayer().getSecond(), super.getBoard().blackPlayer().getMillisecond());

            super.getBoard().currentPlayer().getActivePieces().forEach(piece -> {
                if (!super.getMovedPiece().equals(piece)) {
                    builder.setPiece(piece);
                }
            });
            super.getBoard().currentPlayer().getOpponent().getActivePieces().forEach(piece -> {
                if (!piece.equals(this.getAttackedPiece())) {
                    builder.setPiece(piece);
                }
            });

            builder.setPiece(super.getMovedPiece().movedPiece(this));
            builder.setTransitionMove(this);

            return builder.build();
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

    public static final class PawnPromotion extends Move {

        private final Move decoratedMove;
        private final Pawn promotedPawn;
        private Piece promotedPiece;
        private final Piece minimaxPromotionPiece;

        public PawnPromotion(final Move decoratedMove, final Piece MinimaxPromotionPiece) {
            super(decoratedMove.getBoard(), decoratedMove.getMovedPiece(), decoratedMove.getDestinationCoordinate());
            this.decoratedMove = decoratedMove;
            this.promotedPawn = (Pawn) decoratedMove.getMovedPiece();
            this.minimaxPromotionPiece = MinimaxPromotionPiece;
        }

        public void setPromotedPiece(final Piece piece) {
            this.promotedPiece = piece;
        }

        public Move getDecoratedMove() {
            return this.decoratedMove;
        }

        public Piece getPromotedPiece() {
            return this.promotedPiece;
        }

        public Pawn getPromotedPawn() {
            return this.promotedPawn;
        }

        @Override
        public boolean isPromotionMove() {
            return true;
        }

        @Override
        public Board execute() {


            final Board pawnMoveBoard = this.decoratedMove.execute();
            final Board.Builder builder = new Board.Builder(super.getBoard().getMoveCount() + 1, pawnMoveBoard.currentPlayer().getLeague(), null)
                    .updateWhiteTimer(super.getBoard().whitePlayer().getMinute(), super.getBoard().whitePlayer().getSecond(), super.getBoard().whitePlayer().getMillisecond())
                    .updateBlackTimer(super.getBoard().blackPlayer().getMinute(), super.getBoard().blackPlayer().getSecond(), super.getBoard().blackPlayer().getMillisecond());

            super.getBoard().currentPlayer().getActivePieces().forEach(piece -> {
                if (!this.promotedPawn.equals(piece)) {
                    builder.setPiece(piece);
                }
            });
            super.getBoard().currentPlayer().getOpponent().getActivePieces().forEach(builder::setPiece);

            this.setPromotedPiece(this.minimaxPromotionPiece);
            builder.setPiece(this.minimaxPromotionPiece.movedPiece(this));
            return builder.build();
        }

        @Override
        public boolean isAttack() {
            return this.decoratedMove.isAttack();
        }

        @Override
        public Piece getAttackedPiece() {
            return this.decoratedMove.getAttackedPiece();
        }

        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(super.getDestinationCoordinate()) + "=" + this.promotedPiece.toString().charAt(0);
        }

        @Override
        public int hashCode() {
            return this.decoratedMove.hashCode() + (31 * this.promotedPawn.hashCode());
        }

        @Override
        public boolean equals(final Object object) {
            return this == object || object instanceof PawnPromotion && (super.equals(object));
        }
    }

    public static final class PawnJump extends Move {

        public PawnJump(final Board board, final Piece movePiece, final int destinationCoordinate) {
            super(board, movePiece, destinationCoordinate);
        }

        @Override
        public Board execute() {
            final Pawn movedPawn = (Pawn) super.getMovedPiece().movedPiece(this);

            final Board.Builder builder = new Board.Builder(super.getBoard().getMoveCount() + 1, super.getBoard().currentPlayer().getOpponent().getLeague(), movedPawn)
                    .updateWhiteTimer(super.getBoard().whitePlayer().getMinute(), super.getBoard().whitePlayer().getSecond(), super.getBoard().whitePlayer().getMillisecond())
                    .updateBlackTimer(super.getBoard().blackPlayer().getMinute(), super.getBoard().blackPlayer().getSecond(), super.getBoard().blackPlayer().getMillisecond());

            super.getBoard().currentPlayer().getActivePieces().forEach(piece -> {
                if (!super.getMovedPiece().equals(piece)) {
                    builder.setPiece(piece);
                }
            });
            super.getBoard().currentPlayer().getOpponent().getActivePieces().forEach(builder::setPiece);

            builder.setPiece(movedPawn);
            builder.setTransitionMove(this);
            return builder.build();
        }

        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(super.getDestinationCoordinate());
        }
    }

    private static abstract class CastleMove extends Move {

        protected final Rook castleRook;

        protected final int castleRookStart, castleRookDestination;

        public CastleMove(final Board board, final Piece movePiece, final int destinationCoordinate, final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
            super(board, movePiece, destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }

        public Rook getCastleRook() {
            return this.castleRook;
        }

        @Override
        public boolean isCastlingMove() {
            return true;
        }

        @Override
        public Board execute() {

            final Board.Builder builder = new Board.Builder(super.getBoard().getMoveCount() + 1, super.getBoard().currentPlayer().getOpponent().getLeague(), null)
                    .updateWhiteTimer(super.getBoard().whitePlayer().getMinute(), super.getBoard().whitePlayer().getSecond(), super.getBoard().whitePlayer().getMillisecond())
                    .updateBlackTimer(super.getBoard().blackPlayer().getMinute(), super.getBoard().blackPlayer().getSecond(), super.getBoard().blackPlayer().getMillisecond());

            super.getBoard().getAllPieces().forEach(piece -> {
                if (!super.getMovedPiece().equals(piece) && !this.castleRook.equals(piece)) {
                    builder.setPiece(piece);
                }
            });

            builder.setPiece(super.getMovedPiece().movedPiece(this));
            builder.setPiece(new Rook(this.castleRook.getLeague(), this.castleRookDestination, false));
            builder.setTransitionMove(this);
            return builder.build();
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + this.castleRook.hashCode();
            result = prime * result + this.castleRookDestination;
            return result;
        }

        @Override
        public boolean equals(final Object object) {
            if (this == object) {
                return true;
            }
            if (!(object instanceof CastleMove)) {
                return false;
            }
            final CastleMove castleMove = (CastleMove) object;

            return super.equals(castleMove) && this.castleRook.equals(castleMove.getCastleRook());
        }
    }

    public static final class KingSideCastleMove extends CastleMove {

        public KingSideCastleMove(final Board board, final Piece movePiece, final int destinationCoordinate, final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
            super(board, movePiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }

        @Override
        public boolean equals(final Object object) {
            return this == object || object instanceof KingSideCastleMove && super.equals(object);
        }

        @Override
        public String toString() {
            return "O-O";
        }
    }

    public static final class QueenSideCastleMove extends CastleMove {

        public QueenSideCastleMove(final Board board, final Piece movePiece, final int destinationCoordinate, final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
            super(board, movePiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }

        @Override
        public boolean equals(final Object object) {
            return this == object || object instanceof QueenSideCastleMove && super.equals(object);
        }

        @Override
        public String toString() {
            return "O-O-O";
        }
    }

    private static final class NullMove extends Move {
        private NullMove() {
            super(null, 65);
        }

        @Override
        public Board execute() {
            return null;
        }

        @Override
        public int getCurrentCoordinate() {
            return -1;
        }
    }

    public static final class MoveFactory {

        private static final Move NULL_MOVE = new NullMove();

        private MoveFactory() {
            throw new RuntimeException("Not instantiatable");
        }

        public static Move getNullMove() {
            return NULL_MOVE;
        }

        public static Move createMove(final Board board, final Piece piece, final int destinationCoordinate) {
            return piece.calculateLegalMoves(board).parallelStream().filter(move -> move.getCurrentCoordinate() == piece.getPiecePosition() && move.getDestinationCoordinate() == destinationCoordinate).findFirst().orElseGet(MoveFactory::getNullMove);
        }

        public static Move createMoveFromMoveHistory(final Board board, final int currentCoordinate, final int destinationCoordinate) {
            final Piece pieceToMove = board.getAllPieces().parallelStream().filter(piece -> piece.getPiecePosition() == currentCoordinate).findAny().orElseThrow(() -> new IllegalStateException("no such piece"));
            return pieceToMove.calculateLegalMoves(board).parallelStream().filter(move -> move.getCurrentCoordinate() == currentCoordinate && move.getDestinationCoordinate() == destinationCoordinate).findFirst().orElseGet(MoveFactory::getNullMove);
        }
    }
}