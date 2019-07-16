package com.chess.game.pieces;

import com.chess.game.Alliance;
import com.chess.game.board.Board;
import com.chess.game.board.Move;

import java.util.Collection;

public abstract class Piece {
    protected final int piecePosition;
    protected final Alliance pieceAlliance;

    private Piece(final int piecePosition, final Alliance pieceAlliance) {
        this.pieceAlliance = pieceAlliance;
        this.piecePosition = piecePosition;
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board);
}
