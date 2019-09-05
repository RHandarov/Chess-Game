package com.chess.game.player.ai;

import com.chess.game.board.Board;
import com.chess.game.pieces.Piece;
import com.chess.game.player.Player;

public final class StandardBoardEvaluator implements BoardEvaluator {
    private static int CHECK_BONUS = 50;
    private static int CHECK_MATE_BONUS = 10000;
    private static int DEPTH_BONUS = 100;
    private static int CASTLE_BONUS = 60;

    @Override
    public int evaluate(final Board board, final int depth) {
        return this.scorePlayer(board, board.getWhitePlayer(), depth) - this.scorePlayer(board, board.getBlackPlayer(), depth);
    }

    private int scorePlayer(final Board board, final Player player, final int depth) {
        return pieceValue(player) +
                mubility(player) +
                check(player) +
                checkMate(player, depth) +
                castled(player);
    }

    private static int checkMate(final Player player, final int depth) {
        return player.getOpponent().isInCheckMate() ? CHECK_MATE_BONUS * depthBonus(depth) : 0;
    }

    private static int depthBonus(final int depth) {
        return depth == 0 ? 1 : depth * DEPTH_BONUS;
    }

    private static int castled(final Player player) {
        return player.isCastled() ? CASTLE_BONUS : 0;
    }

    private static int mubility(final Player player) {
        return player.getLegalMoves().size();
    }

    private static int check(final Player player) {
        return player.getOpponent().isInCheck() ? CHECK_BONUS : 0;
    }

    private static int pieceValue(final Player player) {
        int pieceValueScore = 0;
        for (final Piece piece : player.getActivePieces()) {
            pieceValueScore += piece.getPieceValue();
        }
        return pieceValueScore;
    }
}
