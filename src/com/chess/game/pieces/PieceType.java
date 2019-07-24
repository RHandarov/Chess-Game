package com.chess.game.pieces;

public enum PieceType {
    BISHOP {
        @Override
        public String toString() {
            return "b";
        }
    },
    KING {
        @Override
        public String toString() {
            return "k";
        }
    },
    KNIGHT {
        @Override
        public String toString() {
            return "n";
        }
    },
    PAWN {
        @Override
        public String toString() {
            return "p";
        }
    },
    QUEEN {
        @Override
        public String toString() {
            return "q";
        }
    },
    ROOK {
        @Override
        public String toString() {
            return "r";
        }
    };

    @Override
    public abstract String toString();
}
