package com.chess.game.player.ai;

import com.chess.game.Alliance;
import com.chess.game.board.Board;
import com.chess.game.player.Move;
import com.chess.game.player.MoveStatus;
import com.chess.game.player.MoveTransition;

public final class MiniMax implements MoveStrategy {
    private final BoardEvaluator boardEvaluator;
    private final int searchDepth;

    public MiniMax(final int searchDepth) {
        this.boardEvaluator = new StandardBoardEvaluator();
        this.searchDepth = searchDepth;
    }

    @Override
    public Move execute(final Board board) {
        Move bestMove = null;
        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        for (final Move move : board.getCurrentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus() == MoveStatus.DONE) {
                final int currentValue = board.getCurrentPlayer().getAlliance() == Alliance.WHITE ?
                                         this.min(moveTransition.getTransitionBoard(), this.searchDepth - 1) :
                                         this.max(moveTransition.getTransitionBoard(), this.searchDepth - 1);
                if (board.getCurrentPlayer().getAlliance() == Alliance.WHITE && currentValue > highestSeenValue) {
                    highestSeenValue = currentValue;
                    bestMove = move;
                } else if (board.getCurrentPlayer().getAlliance() == Alliance.BLACK && currentValue < lowestSeenValue) {
                    lowestSeenValue = currentValue;
                    bestMove = move;
                }
            }
        }
        return bestMove;
    }

    public int min(final Board board, final int depth) {
        if (depth == 0 || isEndGameScenario(board)) {
            return this.boardEvaluator.evaluate(board, depth);
        }
        int lowestSeenValue = Integer.MAX_VALUE;
        for (final Move move : board.getCurrentPlayer().getLegalMoves()) {
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

    private static boolean isEndGameScenario(final Board board) {
        return board.getCurrentPlayer().isInCheckMate() ||
               board.getCurrentPlayer().isInStaleMate();
    }

    public int max(final Board board, final int depth) {
        if (depth == 0 || isEndGameScenario(board)) {
            return this.boardEvaluator.evaluate(board, depth);
        }
        int highestSeenValue = Integer.MIN_VALUE;
        for (final Move move : board.getCurrentPlayer().getLegalMoves()) {
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
