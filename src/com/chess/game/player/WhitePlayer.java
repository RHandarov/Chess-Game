package com.chess.game.player;

import com.chess.game.board.Board;
import com.chess.game.pieces.Piece;

import java.util.List;

public final class WhitePlayer extends Player {
    public WhitePlayer(final Board board, final List<Move> legalMoves, final List<Move> opponentLegalMoves) {
        super(board, legalMoves, opponentLegalMoves);
    }

    @Override
    public Player getOpponent() {
        return this.board.getBlackPlayer();
    }

    @Override
    public List<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }
}
