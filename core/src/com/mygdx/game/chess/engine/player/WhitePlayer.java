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

public final class WhitePlayer extends Player{
    public WhitePlayer(final Board board, final Collection<Move> whiteStandardLegalMoves, final Collection<Move> blackStandardLegalMoves, final int minute, final int second, final int millisecond) {
        super(board, whiteStandardLegalMoves, blackStandardLegalMoves, minute, second, millisecond);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return board.getWhitePieces();
    }

    @Override
    public League getLeague() {
        return League.WHITE;
    }

    @Override
    public Player getOpponent() {
        return board.blackPlayer();
    }

    @Override
    public String toString() {
        return "White";
    }

    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> opponentLegals) {

        if (this.isCastled()) { return Collections.emptyList(); }

        final List<Move> kingCastles = new ArrayList<>();

        if (this.playerKing.isFirstMove() && !this.isInCheck()) {
            //white's king side castle
            if (!board.getTile(61).isTileOccupied() && !board.getTile(62).isTileOccupied()) {
                final Tile rookTile = board.getTile(63);
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    if (calculateAttacksOnTile(61, opponentLegals).isEmpty() &&
                            calculateAttacksOnTile(62, opponentLegals).isEmpty() &&
                            rookTile.getPiece() instanceof Rook) {
                        kingCastles.add(new KingSideCastleMove(board, this.playerKing, 62, (Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 61));
                    }

                }
            }
            if (!board.getTile(59).isTileOccupied() &&
                    !board.getTile(58).isTileOccupied() &&
                    !board.getTile(57).isTileOccupied()) {
                final Tile rookTile = board.getTile(59);
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove() &&
                        calculateAttacksOnTile(58, opponentLegals).isEmpty() &&
                        calculateAttacksOnTile(59, opponentLegals).isEmpty() &&
                        rookTile.getPiece() instanceof Rook) {
                    kingCastles.add(new QueenSideCastleMove(board, this.playerKing, 58, (Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 59));
                }
            }

        }

        return Collections.unmodifiableList(kingCastles);
    }
}