package com.chess.game;

import com.chess.game.board.Board;

public final class Engine {
    public static void main(String args[]) {
        Board board = Board.createStandardBoard();
        System.out.println(board);
    }
}
