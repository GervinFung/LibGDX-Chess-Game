package com.mygdx.game.chess.engine.player.ArtificialIntelligence;

import com.mygdx.game.chess.engine.pieces.Piece;
import com.mygdx.game.chess.engine.pieces.PieceType;
import com.mygdx.game.chess.engine.player.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public final class PawnStructureAnalyse {
    public static final int ISOLATED_PAWN_PENALTY = -10;
    public static final int DOUBLED_PAWN_PENALTY = -10;

    public int pawnStructureScore(final Player player) {
        final int[] pawnsOnColumnTable = createPawnColumnTable(calculatePlayerPawns(player));
        return calculatePawnColumnStack(pawnsOnColumnTable) + calculateIsolatedPawnPenalty(pawnsOnColumnTable);
    }
    
    public int isolatedPawnPenalty(final Player player) { return calculateIsolatedPawnPenalty(createPawnColumnTable(calculatePlayerPawns(player))); }

    public int doubledPawnPenalty(final Player player) { return calculatePawnColumnStack(createPawnColumnTable(calculatePlayerPawns(player))); }

    private static Collection<Piece> calculatePlayerPawns(final Player player) {
        final ArrayList<Piece> activePawn = new ArrayList<>(8);
        for (final Piece piece :player.getActivePieces()) {
            if (piece.getPieceType() == PieceType.PAWN) {
                activePawn.add(piece);
            }
        }
        return Collections.unmodifiableList(activePawn);
    }

    private static int calculatePawnColumnStack(final int[] pawnsOnColumnTable) {
        int pawnStackPenalty = 0;
        for(final int pawnStack : pawnsOnColumnTable) {
            if(pawnStack > 1) {
                pawnStackPenalty += pawnStack;
            }
        }
        return pawnStackPenalty * DOUBLED_PAWN_PENALTY;
    }

    private static int calculateIsolatedPawnPenalty(final int[] pawnsOnColumnTable) {
        int numIsolatedPawns = 0;
        if(pawnsOnColumnTable[0] > 0 && pawnsOnColumnTable[1] == 0) {
            numIsolatedPawns += pawnsOnColumnTable[0];
        }
        if(pawnsOnColumnTable[7] > 0 && pawnsOnColumnTable[6] == 0) {
            numIsolatedPawns += pawnsOnColumnTable[7];
        }
        for(int i = 1; i < pawnsOnColumnTable.length - 1; i++) {
            if((pawnsOnColumnTable[i-1] == 0 && pawnsOnColumnTable[i+1] == 0)) {
                numIsolatedPawns += pawnsOnColumnTable[i];
            }
        }
        return numIsolatedPawns * ISOLATED_PAWN_PENALTY;
    }

    private static int[] createPawnColumnTable(final Collection<Piece> playerPawns) {
        final int[] table = new int[8];
        for(final Piece playerPawn : playerPawns) {
            table[playerPawn.getPiecePosition() % 8]++;
        }
        return table;
    }
}