package com.chess.game.pieces;

import com.chess.game.Alliance;
import com.chess.game.board.Board;
import com.chess.game.board.Move;

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

    @Override
    public String toString() {
        String pieceLetter = this.getPieceType().toString();
        return this.getPieceAlliance() == Alliance.BLACK ? pieceLetter.toUpperCase() : pieceLetter.toLowerCase();
    }
}
