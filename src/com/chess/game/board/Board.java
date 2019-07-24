package com.chess.game.board;

import com.chess.game.Alliance;
import com.chess.game.pieces.*;
import javafx.util.Pair;

import java.util.*;

public final class Board {
    private final List<Tile> boardTiles;
    private final List<Piece> whitePieces;
    private final List<Piece> blackPieces;
    private final Player whitePlayer;
    private final Player blackPlayer;
    private final Alliance currentPlayer;

    public Tile getTile(int tileCoordinate) {
        return this.boardTiles.get(tileCoordinate);
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer == Alliance.WHITE ? this.whitePlayer : this.blackPlayer;
    }

    public Player getOpponentPlayer(final Player player) {
        return player.getAlliance() == Alliance.WHITE ? this.blackPlayer : this.whitePlayer;
    }

    public List<Piece> getWhitePieces() {
        return this.whitePieces;
    }

    public List<Piece> getBlackPieces() {
        return this.blackPieces;
    }

    private List<Piece> getActivePieces(final Alliance playerType) {
        List<Piece> pieces = new ArrayList<>();
        for (int i = 0; i < BoardUtils.NUMBER_OF_BOARD_TILES; i++) {
            if (this.getTile(i).isTileOccupied()) {
                Piece currentPiece = this.getTile(i).getPiece();
                if (playerType == currentPiece.getPieceAlliance()) {
                    pieces.add(currentPiece);
                }
            }
        }
        return Collections.unmodifiableList(pieces);
    }

    private Board(final BoardBuilder builder) {
        this.boardTiles = Board.createBoard(builder);
        this.whitePieces = this.getActivePieces(Alliance.WHITE);
        this.blackPieces = this.getActivePieces(Alliance.BLACK);
        this.whitePlayer = new Player(this, Alliance.WHITE);
        this.blackPlayer = new Player(this, Alliance.BLACK);
        this.currentPlayer = builder.getMoveMaker();
    }

    private static List<Tile> createBoard(final BoardBuilder boardBuilder) {
        List<Tile> board = new ArrayList<>();
        for (int i = 0; i < BoardUtils.NUMBER_OF_BOARD_TILES; i++) {
            board.add(Tile.createTile(i, boardBuilder.getPiece(i)));
        }
        return Collections.unmodifiableList(board);
    }

    public static Board createStandardBoard() {
        BoardBuilder builder = new BoardBuilder();
        //Set black pieces
        builder.setPiece(new Rook(0, Alliance.BLACK));
        builder.setPiece(new Knight(1, Alliance.BLACK));
        builder.setPiece(new Bishop(2, Alliance.BLACK));
        builder.setPiece(new King(3, Alliance.BLACK));
        builder.setPiece(new Queen(4, Alliance.BLACK));
        builder.setPiece(new Bishop(5, Alliance.BLACK));
        builder.setPiece(new Knight(6, Alliance.BLACK));
        builder.setPiece(new Rook(7, Alliance.BLACK));
        builder.setPiece(new Pawn(8, Alliance.BLACK));
        builder.setPiece(new Pawn(9, Alliance.BLACK));
        builder.setPiece(new Pawn(10, Alliance.BLACK));
        builder.setPiece(new Pawn(11, Alliance.BLACK));
        builder.setPiece(new Pawn(12, Alliance.BLACK));
        builder.setPiece(new Pawn(13, Alliance.BLACK));
        builder.setPiece(new Pawn(14, Alliance.BLACK));
        builder.setPiece(new Pawn(15, Alliance.BLACK));
        //Set white pieces
        builder.setPiece(new Pawn(48, Alliance.WHITE));
        builder.setPiece(new Pawn(49, Alliance.WHITE));
        builder.setPiece(new Pawn(50, Alliance.WHITE));
        builder.setPiece(new Pawn(51, Alliance.WHITE));
        builder.setPiece(new Pawn(52, Alliance.WHITE));
        builder.setPiece(new Pawn(53, Alliance.WHITE));
        builder.setPiece(new Pawn(54, Alliance.WHITE));
        builder.setPiece(new Pawn(55, Alliance.WHITE));
        builder.setPiece(new Rook(56, Alliance.WHITE));
        builder.setPiece(new Knight(57, Alliance.WHITE));
        builder.setPiece(new Bishop(58, Alliance.WHITE));
        builder.setPiece(new King(59, Alliance.WHITE));
        builder.setPiece(new Queen(60, Alliance.WHITE));
        builder.setPiece(new Bishop(61, Alliance.WHITE));
        builder.setPiece(new Knight(62, Alliance.WHITE));
        builder.setPiece(new Rook(63, Alliance.WHITE));
        return builder.build();
    }

    @Override
    public String toString() {
        StringBuilder boardString = new StringBuilder();
        for (int row = 1; row <= BoardUtils.BOARD_ROWS; row++) {
            for (int col = 1; col <= BoardUtils.BOARD_COLUMNS; col++) {
                int tileCoordinate = BoardUtils.getPieceCoordinate(new Pair<Integer, Integer>(row, col));
                boardString.append(this.getTile(tileCoordinate).toString());
                if (col < 8) {
                    boardString.append(' ');
                } else {
                    boardString.append('\n');
                }
            }
        }
        return boardString.toString();
    }

    public static final class BoardBuilder {
      private final Map<Integer, Piece> pieces;
      private Alliance currentPlayer;

      public void setMoveMaker(final Alliance player) {
          this.currentPlayer = player;
      }

      public Alliance getMoveMaker() {
          return this.currentPlayer;
      }

      public BoardBuilder() {
          this.pieces = new HashMap<>();
      }

      public BoardBuilder setPiece(final Piece piece) {
          if (piece == null) {
              return this;
          }
          if (!this.pieces.containsKey(piece.getPiecePosition())) {
              this.pieces.put(piece.getPiecePosition(), piece);
          }
          return this;
      }

      public BoardBuilder clearPiece(int pieceCoordinate) {
          if (this.pieces.containsKey(pieceCoordinate)) {
              this.pieces.remove(pieceCoordinate);
          }
          return this;
      }

      public Piece getPiece(int pieceCoordinate) {
          return this.pieces.containsKey(pieceCoordinate) ? this.pieces.get(pieceCoordinate) : null;
      }

      public Board build() {
          return new Board(this);
      }
    }
}
