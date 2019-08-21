package com.chess.game.player.ai;

import com.chess.game.board.Board;
import com.chess.game.player.Move;
import com.chess.game.player.MoveStatus;
import com.chess.game.player.MoveTransition;

public final class MiniMax implements MoveStrategy {
    private final BoardEvaluator boardEvaluator;

    public MiniMax() {
        this.boardEvaluator = null;
    }

    @Override
    public Move execute(final Board board, final int depth) {
        return null;
    }

    public int min(final Board board, final int depth) {
        if (depth == 0 /* or the game is over */) {
            return this.boardEvaluator.evaluate(board, depth);
        }
        int lowestSeenValue = Integer.MAX_VALUE;
        for (final Move move : board.getAllLegalMoves()) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus() == MoveStatus.DONE) {
                final int currentValue = this.max(moveTransition.getTransitionBoard(), depth - 1);
                if (currentValue < lowestSeenValue) {
                    lowestSeenValue = currentValue;
                }
            }
        }
        return lowestSeenValue;
    }

    public int max(final Board board, final int depth) {
        if (depth == 0 /* or the game is over */) {
            return this.boardEvaluator.evaluate(board, depth);
        }
        int highestSeenValue = Integer.MIN_VALUE;
        for (final Move move : board.getAllLegalMoves()) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus() == MoveStatus.DONE) {
                final int currentValue = this.min(moveTransition.getTransitionBoard(), depth - 1);
                if (currentValue > highestSeenValue) {
                    highestSeenValue = currentValue;
                }
            }
        }
        return highestSeenValue;
    }
}
