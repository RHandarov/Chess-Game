package com.chess.game.board;

import javafx.util.Pair;

public final class BoardUtils {
    public static final int BOARD_ROWS = 8;
    public static final int BOARD_COLUMNS = 8;
    public static final int NUMBER_OF_BOARD_TILES = 64;

    private BoardUtils() {
        throw new RuntimeException("You can create me!");
    }

    public static Pair<Integer, Integer> getPieceRowAndColumnCoordinates(int pieceCoordinate) {
        pieceCoordinate++;
        int pieceRow = pieceCoordinate / BoardUtils.BOARD_COLUMNS;
        int pieceColumn = pieceCoordinate % BoardUtils.BOARD_COLUMNS;
        if (pieceColumn == 0) {
            pieceColumn = BoardUtils.BOARD_COLUMNS;
        } else {
            pieceRow++;
        }
        return new Pair<Integer, Integer>(pieceRow, pieceColumn);
    }

    public static int getPieceCoordinate(final Pair<Integer, Integer> pieceRowAndColumnCoordinate) {
        return (pieceRowAndColumnCoordinate.getKey() - 1) * BoardUtils.BOARD_COLUMNS + pieceRowAndColumnCoordinate.getValue() - 1;
    }

    public static boolean isValidTile(final Pair<Integer, Integer> pieceRowAndColumnCoordinate) {
        return pieceRowAndColumnCoordinate.getKey() >= 1 &&
                pieceRowAndColumnCoordinate.getKey() <= BOARD_ROWS &&
                pieceRowAndColumnCoordinate.getValue() >= 1 &&
                pieceRowAndColumnCoordinate.getValue() <= BOARD_COLUMNS;
    }

    public static boolean isValidTile(final int pieceCoordinate) {
        return pieceCoordinate >= 0 && pieceCoordinate <= 63;
    }
}
