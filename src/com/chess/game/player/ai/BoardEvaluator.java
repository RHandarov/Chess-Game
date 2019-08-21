package com.chess.game.player.ai;

import com.chess.game.board.Board;

public interface BoardEvaluator {
    int evaluate(Board board, int depth);
}
