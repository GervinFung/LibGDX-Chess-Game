package com.mygdx.game.chess.engine.player.ArtificialIntelligence;

import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;
import com.mygdx.game.chess.engine.board.Board;
import com.mygdx.game.chess.engine.board.Move;
import com.mygdx.game.chess.engine.pieces.Piece;
import com.mygdx.game.chess.engine.pieces.PieceType;
import com.mygdx.game.chess.engine.player.Player;

public final class StandardBoardEvaluation {

    private static final int CHECK_KING = 45;
    private static final int CHECK_MATE = 10000;
    private static final int DEPTH_BONUS = 100;
    private static final int CASTLE_BONUS = 25;

    private static final int MOBILITY_MULTIPLIER = 5;
    private static final int ATTACK_MULTIPLIER = 1;
    private static final int TWO_BISHOPS_BONUS = 25;
    private static final PawnStructureAnalyse pawnStructureScore = new PawnStructureAnalyse();

    private static final int[] kingEvaluation = {
            -30, -40, -40, -50, -50, -40, -40, -30,
            -30, -40, -40, -50, -50, -40, -40, -30,
            -30, -40, -40, -50, -50, -40, -40, -30,
            -30, -40, -40, -50, -50, -40, -40, -30,
            -20, -30, -30, -40, -40, -30, -30, -20,
            -10, -20, -20, -20, -20, -20, -20, -10,
            20, 20, 0, 0, 0, 0, 20, 20,
            20, 30, 10, 0, 0, 10, 30, 20
    };

    private static final int[] queenEvaluation = {
            -20, -10, -10, -5, -5, -10, -10, -20,
            -10, 0, 0, 0, 0, 0, 0, -10,
            -10, 0, 5, 5, 5, 5, 0, -10,
            -5, 0, 5, 5, 5, 5, 0, -5,
            0, 0, 5, 5, 5, 5, 0, -5,
            -10, 5, 5, 5, 5, 5, 0, -10,
            -10, 0, 5, 0, 0, 0, 0, -10,
            -20, -10, -10, -5, -5, -10, -10, -20
    };

    private static final int[] rookEvaluation = {
            0, 0, 0, 0, 0, 0, 0, 0,
            5, 20, 20, 20, 20, 20, 20, 5,
            -5, 0, 0, 0, 0, 0, 0, -5,
            -5, 0, 0, 0, 0, 0, 0, -5,
            -5, 0, 0, 0, 0, 0, 0, -5,
            -5, 0, 0, 0, 0, 0, 0, -5,
            -5, 0, 0, 0, 0, 0, 0, -5,
            0, 0, 0, 5, 5, 0, 0, 0
    };

    private static final int[] bishopEvaluation = {
            -20, -10, -10, -10, -10, -10, -10, -20,
            -10, 0, 0, 0, 0, 0, 0, -10,
            -10, 0, 5, 10, 10, 5, 0, -10,
            -10, 5, 5, 10, 10, 5, 5, -10,
            -10, 0, 10, 10, 10, 10, 0, -10,
            -10, 10, 10, 10, 10, 10, 10, -10,
            -10, 5, 0, 0, 0, 0, 5, -10,
            -20, -10, -10, -10, -10, -10, -10, -20
    };

    private static final int[] knightEvaluation = {
            -50, -40, -30, -30, -30, -30, -40, -50,
            -40, -20, 0, 0, 0, 0, -20, -40,
            -30, 0, 10, 15, 15, 10, 0, -30,
            -30, 5, 15, 20, 20, 15, 5, -30,
            -30, 0, 15, 20, 20, 15, 0, -30,
            -30, 5, 10, 15, 15, 10, 5, -30,
            -40, -20, 0, 5, 5, 0, -20, -40,
            -50, -40, -30, -30, -30, -30, -40, -50
    };

    private static final int[] pawnEvaluation = {
            0, 0, 0, 0, 0, 0, 0, 0,
            75, 75, 75, 75, 75, 75, 75, 75,
            25, 25, 29, 29, 29, 29, 25, 25,
            5, 5, 10, 55, 55, 10, 5, 5,
            0, 0, 0, 20, 20, 0, 0, 0,
            5, -5, -10, 0, 0, -10, -5, 5,
            5, 10, 10, -20, -20, 10, 10, 5,
            0, 0, 0, 0, 0, 0, 0, 0
    };

    private static int scorePlayer(final Player player, final int depth) {
        return mobility(player) +
                checkMate(player, depth) +
                attacks(player) +
                castled(player) +
                pieceEvaluations(player) +
                pawnStructure(player);
    }

    private static int attacks(final Player player) {
        int attackScore = 0;
        for (final Move move : player.getLegalMoves()) {
            if (move.isAttack()) {
                final Piece movedPiece = move.getMovedPiece();
                final Piece attackedPiece = move.getAttackedPiece();
                if (movedPiece.getPieceValue() <= attackedPiece.getPieceValue()) {
                    attackScore++;
                }
            }
        }
        return attackScore * ATTACK_MULTIPLIER;
    }

    private static int pieceEvaluations(final Player player) {
        int pieceValuationScore = 0;
        int numBishops = 0;
        for (final Piece piece : player.getActivePieces()) {
            pieceValuationScore += piece.getPieceValue() + positionValue(piece).get(piece.getPiecePosition());
            if (piece.getPieceType() == PieceType.BISHOP) {
                numBishops++;
            }
        }
        return pieceValuationScore + (numBishops == 2 ? TWO_BISHOPS_BONUS : 0);
    }

    private static int mobility(final Player player) {
        return MOBILITY_MULTIPLIER * mobilityRatio(player);
    }

    private static int mobilityRatio(final Player player) {
        return (int) ((player.getLegalMoves().size() * 10.0f) / player.getOpponent().getLegalMoves().size());
    }

    private static int castled(final Player player) {
        return player.isCastled() ? CASTLE_BONUS : 0;
    }

    private static int checkMate(final Player player, final int depth) {
        return player.getOpponent().isInCheckmate() ? CHECK_MATE * depthBonus(depth) : check(player);
    }

    private static int depthBonus(final int depth) {
        return depth == 0 ? 1 : DEPTH_BONUS * depth;
    }

    private static int check(final Player player) {
        return player.getOpponent().isInCheck() ? CHECK_KING : 0;
    }

    private static int pawnStructure(final Player player) {
        return pawnStructureScore.pawnStructureScore(player);
    }

    private static ImmutableList<Integer> positionValue(final Piece piece) {
        final boolean isWhite = piece.getLeague().isWhite();

        if ("K".equals(piece.toString())) {
            return getPiecePositionValue(isWhite, kingEvaluation);
        } else if ("Q".equals(piece.toString())) {
            return getPiecePositionValue(isWhite, queenEvaluation);
        } else if ("R".equals(piece.toString())) {
            return getPiecePositionValue(isWhite, rookEvaluation);
        } else if ("B".equals(piece.toString())) {
            return getPiecePositionValue(isWhite, bishopEvaluation);
        } else if ("N".equals(piece.toString())) {
            return getPiecePositionValue(isWhite, knightEvaluation);
        } else {
            return getPiecePositionValue(isWhite, pawnEvaluation);
        }
    }

    private static ImmutableList<Integer> getPiecePositionValue(final boolean isWhite, final int[] positionValue) {
        return isWhite ? ImmutableList.copyOf(Ints.asList(positionValue)) : reversePositionEvaluation(positionValue);
    }

    private static ImmutableList<Integer> reversePositionEvaluation(final int[] positionValue) {
        return ImmutableList.copyOf(Ints.asList(positionValue)).reverse();
    }

    public int evaluate(final Board board, final int depth) {
        return -scorePlayer(board.blackPlayer(), depth) + scorePlayer(board.whitePlayer(), depth);
    }
}