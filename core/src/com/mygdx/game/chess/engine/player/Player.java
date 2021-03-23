package com.mygdx.game.chess.engine.player;

import com.mygdx.game.chess.engine.League;
import com.mygdx.game.chess.engine.board.Board;
import com.mygdx.game.chess.engine.board.MoveTransition;
import com.mygdx.game.chess.engine.board.MoveStatus;
import com.mygdx.game.chess.engine.board.Move;
import com.mygdx.game.chess.engine.board.Tile;
import com.mygdx.game.chess.engine.pieces.King;
import com.mygdx.game.chess.engine.pieces.Piece;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public abstract class Player {

    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;
    private final boolean noTimer;
    private int minute, second, millisecond;

    public Player(final Board board, final Collection<Move> legalMoves, final Collection<Move> opponentLegalMoves, final int minute, final int second, final int millisecond) {
        this.board = board;
        this.playerKing = this.establishKing();
        final List<Move> legal = new ArrayList<>(legalMoves);
        this.isInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentLegalMoves).isEmpty();
        //for ai
        legal.addAll(calculateKingCastles(opponentLegalMoves));
        this.legalMoves = legal;
        this.minute = minute;
        this.second = second;
        this.millisecond = millisecond;
        this.noTimer = minute == -1;
    }

    public final boolean isNoTimer() { return this.noTimer; }

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

    public final int getMinute() { return this.minute; }

    public final int getSecond() { return this.second; }

    public final int getMillisecond() { return this.millisecond; }

    public final boolean isTimeOut() { return this.minute == 0 && this.second == 0 && this.millisecond == 0; }

    public final King getPlayerKing() {
        return this.playerKing;
    }

    public final Collection<Move> getLegalMoves() { return Collections.unmodifiableCollection(this.legalMoves); }

    public static List<Move> calculateAttacksOnTile(final int piecePosition, final Collection<Move> moves) {
        final List<Move> attackMove = new ArrayList<>();

        for (final Move move : moves) {
            if(piecePosition == move.getDestinationCoordinate()) {
                attackMove.add(move);
            }
        }
        return Collections.unmodifiableList(attackMove);
    }

    private King establishKing() {
        for (final Piece piece : getActivePieces()) {
            if (piece.getPieceType().isKing()) {
                return (King) piece;
            }
        }
        throw new RuntimeException("Invalid board");
    }

    public abstract Collection<Piece> getActivePieces();

    public abstract League getLeague();

    public abstract Player getOpponent();

    public final boolean isInCheck() {
        return this.isInCheck;
    }

    public final boolean isInCheckmate() {
        return this.isInCheck && noEscapeMoves();
    }

    public final boolean isInStalemate() { return !this.isInCheck && noEscapeMoves(); }

    protected abstract Collection<Move> calculateKingCastles(final Collection<Move> opponentLegals);

    public final boolean isCastled() {
        return this.playerKing.isCastled();
    }

    public final boolean isKingSideCastleCapable() {
        final Tile rookTile = board.getTile(this.getLeague().isWhite() ? 63 : 7);
        if (!rookTile.isTileOccupied() || this.playerKing.isCastled()) {
            return false;
        }
        return rookTile.getPiece().isFirstMove();
    }

    public final boolean isQueenSideCastleCapable() {
        final Tile rookTile = board.getTile(this.getLeague().isWhite() ? 56 : 0);
        if (!rookTile.isTileOccupied() || this.playerKing.isCastled()) {
            return false;
        }
        return rookTile.getPiece().isFirstMove();
    }

    protected final boolean noEscapeMoves() {
        for (final Move move : this.legalMoves) {
            final MoveTransition transition = makeMove(move);

            if (transition.getMoveStatus().isDone()) {
                return false;
            }
        }
        return true;
    }

    public final MoveTransition makeMove(final Move move) {

        final Board transitionBoard = move.execute();
        if (transitionBoard != null) {
            final Collection<Move> currentPlayerLegals = transitionBoard.currentPlayer().getLegalMoves();
            final List<Move> kingAttacks = Player.calculateAttacksOnTile(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(), currentPlayerLegals);

            if (!kingAttacks.isEmpty()) { return new MoveTransition(board, board, MoveStatus.LEAVES_PLAYER_IN_CHECK); }

            return new MoveTransition(transitionBoard, board, MoveStatus.DONE);
        }
        return new MoveTransition(null, null, MoveStatus.Illegal_Move);
    }

    public final MoveTransition undoMove(final Move move) { return new MoveTransition(board, move.getBoard(), MoveStatus.DONE); }
}