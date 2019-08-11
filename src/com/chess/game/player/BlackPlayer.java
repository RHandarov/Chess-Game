package com.chess.game.player;

import com.chess.game.Alliance;
import com.chess.game.board.Board;
import com.chess.game.pieces.Piece;

import java.util.List;

public final class BlackPlayer extends Player {
    public BlackPlayer(final Board board, final List<Move> legalMoves, final List<Move> opponentLegalMoves) {
        super(board, legalMoves, opponentLegalMoves);
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.BLACK;
    }

    @Override
    public Player getOpponent() {
        return this.board.getWhitePlayer();
    }

    @Override
    public List<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }
}
