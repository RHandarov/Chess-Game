package com.chess.game.player.ai;

import com.chess.game.board.Board;
import com.chess.game.player.Move;

public interface MoveStrategy {
    Move execute(Board board);
}
