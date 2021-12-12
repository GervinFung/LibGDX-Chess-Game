package com.mygdx.game.chess.engine.player;

import com.google.common.collect.ImmutableList;
import com.mygdx.game.chess.engine.League;
import com.mygdx.game.chess.engine.board.Board;
import com.mygdx.game.chess.engine.board.Move;
import com.mygdx.game.chess.engine.board.MoveStatus;
import com.mygdx.game.chess.engine.board.MoveTransition;
import com.mygdx.game.chess.engine.board.Tile;
import com.mygdx.game.chess.engine.pieces.King;
import com.mygdx.game.chess.engine.pieces.Piece;

import java.util.stream.Collectors;

public abstract class Player {

    private final Board board;
    private final King playerKing;
    private final ImmutableList<Move> legalMoves;
    private final boolean isInCheck;
    private int minute, second, millisecond;

    public Player(final Board board, final ImmutableList<Move> legalMoves, final ImmutableList<Move> opponentLegalMoves, final int minute, final int second, final int millisecond) {
        this.board = board;
        this.playerKing = this.establishKing();
        this.isInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentLegalMoves).isEmpty();
        this.legalMoves = ImmutableList.<Move>builder().addAll(legalMoves).addAll(calculateKingCastles(opponentLegalMoves)).build();
        this.minute = minute;
        this.second = second;
        this.millisecond = millisecond;
    }

    public final boolean isNoTimer() {
        return this.minute == -1;
    }

    public final void countDown() {
        if (this.millisecond == 0) {
            if (this.second == 0) {
                if (this.minute == 0) {
                    return;
                }
                this.second = 60;
                this.minute -= 1;
            }
            this.millisecond = 99;
            this.second -= 1;
        }
        this.millisecond -= 1;
    }

    public final Board getBoard() {
        return this.board;
    }

    public final int getMinute() {
        return this.minute;
    }

    public final int getSecond() {
        return this.second;
    }

    public final int getMillisecond() {
        return this.millisecond;
    }

    public final boolean isTimeOut() {
        return this.minute == 0 && this.second == 0 && this.millisecond == 0;
    }

    public final King getPlayerKing() {
        return this.playerKing;
    }

    public final ImmutableList<Move> getLegalMoves() {
        return this.legalMoves;
    }

    public static ImmutableList<Move> calculateAttacksOnTile(final int piecePosition, final ImmutableList<Move> moves) {
        return ImmutableList.copyOf(moves.parallelStream().filter(move -> piecePosition == move.getDestinationCoordinate()).collect(Collectors.toList()));
    }

    private King establishKing() {
        return ((King)this.getActivePieces().parallelStream().filter(piece -> piece.getPieceType().isKing()).findFirst().orElseThrow(() -> new IllegalStateException("Invalid board")));
    }

    public abstract ImmutableList<Piece> getActivePieces();

    public abstract League getLeague();

    public abstract Player getOpponent();

    public final boolean isInCheck() {
        return this.isInCheck;
    }

    public final boolean isInCheckmate() {
        return this.isInCheck && this.noEscapeMoves();
    }

    public final boolean isInStalemate() {
        final ImmutableList<Piece> activePieces = this.getActivePieces();
        final ImmutableList<Piece> opponentActivePieces = this.getOpponent().getActivePieces();
        if (activePieces.size() == 1 && opponentActivePieces.size() == 1) {
            if (activePieces.get(0) instanceof King && opponentActivePieces.get(0) instanceof King) {
                return true;
            }
            throw new IllegalStateException("If there is only 1 active piece left, it must be king, however it is " + activePieces + " and " + opponentActivePieces);
        }
        this.getOpponent().getActivePieces();
        return !this.isInCheck && this.noEscapeMoves();
    }

    protected abstract Move.KingSideCastleMove getKingSideCastleMove(final ImmutableList<Move> opponentLegals);
    protected abstract Move.QueenSideCastleMove getQueenSideCastleMove(final ImmutableList<Move> opponentLegals);

    public abstract ImmutableList<Move> calculateKingCastles(final ImmutableList<Move> opponentLegals);

    public final boolean isCastled() {
        return this.playerKing.isCastled();
    }

    public final boolean isKingSideCastleCapable() {
        final Tile rookTile = this.board.getTile(this.getLeague().isWhite() ? 63 : 7);
        return !(!rookTile.isTileOccupied() || this.playerKing.isCastled()) && rookTile.getPiece().isFirstMove();
    }

    public final boolean isQueenSideCastleCapable() {
        final Tile rookTile = this.board.getTile(this.getLeague().isWhite() ? 56 : 0);
        return !(!rookTile.isTileOccupied() || this.playerKing.isCastled()) && rookTile.getPiece().isFirstMove();
    }

    protected final boolean noEscapeMoves() {
        return this.legalMoves.parallelStream().noneMatch(move -> this.makeMove(move).getMoveStatus().isDone());
    }

    public final MoveTransition makeMove(final Move move) {

        final Board transitionBoard = move.execute();
        if (transitionBoard != null) {
            final ImmutableList<Move> currentPlayerLegals = transitionBoard.currentPlayer().getLegalMoves();
            final ImmutableList<Move> kingAttacks = Player.calculateAttacksOnTile(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(), currentPlayerLegals);

            if (!kingAttacks.isEmpty()) {
                return new MoveTransition(board, board, MoveStatus.LEAVES_PLAYER_IN_CHECK);
            }

            return new MoveTransition(transitionBoard, board, MoveStatus.DONE);
        }
        return new MoveTransition(null, null, MoveStatus.ILLEGAL_MOVE);
    }

    public final MoveTransition undoMove(final Move move) {
        return new MoveTransition(board, move.getBoard(), MoveStatus.DONE);
    }
}