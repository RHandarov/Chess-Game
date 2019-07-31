package com.chess.game.player;

import com.chess.game.pieces.Piece;

public abstract class Move {
    protected final int newPieceCoordinate;
    protected final Piece movingPiece;

    private Move(final int newPieceCoordinate, final Piece movingPiece) {
        this.newPieceCoordinate = newPieceCoordinate;
        this.movingPiece = movingPiece;
    }

    public static final class NormalMove extends Move {
        public NormalMove(final int newPieceCoordinate, final Piece movingPiece) {
            super(newPieceCoordinate, movingPiece);
        }
    }

    public static final class AttackingMove extends Move {
        private final Piece attackingPiece;

        public AttackingMove(final int newPieceCoordinate, final Piece movingPiece, final Piece attackingPiece) {
            super(newPieceCoordinate, movingPiece);
            this.attackingPiece = attackingPiece;
        }
    }
}
