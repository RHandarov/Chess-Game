package com.chess.game.pieces;

import com.chess.game.Alliance;
import com.chess.game.board.Board;
import com.chess.game.player.Move;

import java.util.List;

public abstract class Piece {
    private final int piecePosition;
    private final Alliance pieceAlliance;

    protected Piece(final int piecePosition, final Alliance pieceAlliance) {
        this.pieceAlliance = pieceAlliance;
        this.piecePosition = piecePosition;
    }

    public abstract List<Move> calculateLegalMoves(final Board board);

    public Alliance getPieceAlliance() {
        return this.pieceAlliance;
    }

    public int getPiecePosition() {
        return this.piecePosition;
    }

    public abstract PieceType getPieceType();

    public static Piece createPiece(final PieceType pieceType, final int piecePosition, final Alliance pieceAlliance) {
        if (pieceType == PieceType.ROOK) {
            return new Rook(piecePosition, pieceAlliance);
        }
        if (pieceType == PieceType.PAWN) {
            return new Pawn(piecePosition, pieceAlliance);
        }
        if (pieceType == PieceType.KING) {
            return new King(piecePosition, pieceAlliance);
        }
        if (pieceType == PieceType.KNIGHT) {
            return new Knight(piecePosition, pieceAlliance);
        }
        if (pieceType == PieceType.QUEEN) {
            return new Queen(piecePosition, pieceAlliance);
        }
        if (pieceType == PieceType.BISHOP) {
            return new Bishop(piecePosition, pieceAlliance);
        }
        return null;
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof Piece)) {
            return false;
        }
        Piece piece = (Piece)other;
        return this.getPieceAlliance() == piece.getPieceAlliance() &&
                this.getPieceType() == piece.getPieceType() &&
                this.piecePosition == piece.getPiecePosition();
    }

    @Override
    public String toString() {
        String pieceLetter = this.getPieceType().toString();
        return this.getPieceAlliance() == Alliance.BLACK ? pieceLetter.toUpperCase() : pieceLetter.toLowerCase();
    }

    public abstract int getPieceValue();
}
