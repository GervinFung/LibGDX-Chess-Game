package com.mygdx.game.chess.engine.pieces;

public enum PieceType {
    PAWN("P", 100) {
        @Override
        public boolean isKing() {
            return false;
        }

        @Override
        public boolean isRook() {
            return false;
        }
    },

    KNIGHT("N", 300) {
        @Override
        public boolean isKing() {
            return false;
        }

        @Override
        public boolean isRook() {
            return false;
        }
    },
    BISHOP("B", 300) {
        @Override
        public boolean isKing() {
            return false;
        }

        @Override
        public boolean isRook() {
            return false;
        }
    },
    ROOK("R", 500) {
        @Override
        public boolean isKing() {
            return false;
        }

        @Override
        public boolean isRook() {
            return true;
        }
    },
    QUEEN("Q", 900) {
        @Override
        public boolean isKing() {
            return false;
        }

        @Override
        public boolean isRook() {
            return false;
        }
    },
    KING("K", 10000) {
        @Override
        public boolean isKing() {
            return true;
        }

        @Override
        public boolean isRook() {
            return false;
        }
    };

    private final String pieceName;
    private final int pieceValue;

    PieceType(final String pieceName, final int pieceValue) {

        this.pieceName = pieceName;
        this.pieceValue = pieceValue;
    }

    @Override
    public String toString() {
        return this.pieceName;
    }

    public int getPieceValue() {
        return this.pieceValue;
    }

    public abstract boolean isKing();

    public abstract boolean isRook();
}