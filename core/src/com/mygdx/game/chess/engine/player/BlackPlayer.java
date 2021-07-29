package com.mygdx.game.chess.engine.player;

import com.mygdx.game.chess.engine.League;
import com.mygdx.game.chess.engine.board.Board;
import com.mygdx.game.chess.engine.board.Move;
import com.mygdx.game.chess.engine.board.Tile;
import com.mygdx.game.chess.engine.pieces.Piece;
import com.mygdx.game.chess.engine.pieces.Rook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.mygdx.game.chess.engine.board.Move.*;

public final class BlackPlayer extends Player{
    public BlackPlayer(final Board board, final Collection<Move> whiteStandardLegalMoves, final Collection<Move> blackStandardLegalMoves, final int minute, final int second, final int millisecond) {
        super(board, blackStandardLegalMoves, whiteStandardLegalMoves, minute, second, millisecond);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return super.getBoard().getBlackPieces();
    }

    @Override
    public League getLeague() {
        return League.BLACK;
    }

    @Override
    public Player getOpponent() {
        return super.getBoard().whitePlayer();
    }

    @Override
    public String toString() {
        return "Black";
    }

    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> opponentLegals) {

        if (this.isCastled()) { return Collections.emptyList(); }

        final List<Move> kingCastles = new ArrayList<>();

        if (super.getPlayerKing().isFirstMove() && !this.isInCheck()) {
            //blacks king side castle
            if (!super.getBoard().getTile(5).isTileOccupied() && !super.getBoard().getTile(6).isTileOccupied()) {
                final Tile rookTile = super.getBoard().getTile(7);
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    if (calculateAttacksOnTile(5, opponentLegals).isEmpty() &&
                            calculateAttacksOnTile(6, opponentLegals).isEmpty() &&
                            rookTile.getPiece() instanceof Rook) {
                        kingCastles.add(new KingSideCastleMove(super.getBoard(), super.getPlayerKing(), 6, (Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 5));
                    }
                }
            }
            //blacks queens side castle
            if (!super.getBoard().getTile(1).isTileOccupied() &&
                    !super.getBoard().getTile(2).isTileOccupied() &&
                    !super.getBoard().getTile(3).isTileOccupied()) {
                final Tile rookTile = super.getBoard().getTile(0);
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove() &&
                        calculateAttacksOnTile(2, opponentLegals).isEmpty() &&
                        calculateAttacksOnTile(3, opponentLegals).isEmpty() &&
                        rookTile.getPiece() instanceof Rook) {
                    kingCastles.add(new QueenSideCastleMove(super.getBoard(), super.getPlayerKing(), 2, (Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 3));
                }
            }
        }
        return Collections.unmodifiableList(kingCastles);
    }
}