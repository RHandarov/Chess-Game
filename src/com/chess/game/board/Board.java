package com.chess.game.board;

import com.chess.game.Alliance;
import com.chess.game.Utiles;
import com.chess.game.pieces.*;
import com.chess.game.player.BlackPlayer;
import com.chess.game.player.Move;
import com.chess.game.player.Player;
import com.chess.game.player.WhitePlayer;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Board {
    private final List<Tile> gameBoard;
    private final List<Piece> whitePieces;
    private final List<Piece> blackPieces;
    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;

    private static List<Tile> createBoardTiles(final Builder builder) {
        List<Tile> tiles = new ArrayList<>();
        for (int i = 0; i < BoardUtils.NUMBER_OF_BOARD_TILES; i++) {
            tiles.add(Tile.createTile(i, builder.getPiece(i)));
        }
        return Collections.unmodifiableList(tiles);
    }

    private List<Piece> calculatePieces(final Alliance pieceAlliance) {
        List<Piece> pieces = new ArrayList<>();
        for (final Tile tile : this.gameBoard) {
            if (tile.isTileOccupied()) {
                Piece piece = tile.getPiece();
                if (piece.getPieceAlliance() == pieceAlliance) {
                    pieces.add(piece);
                }
            }
        }
        return Collections.unmodifiableList(pieces);
    }

    private List<Piece> getActivePieces(final Alliance pieceAlliance) {
        return pieceAlliance == Alliance.WHITE ? this.whitePieces : this.blackPieces;
    }

    public List<Tile> getTiles() {
        return this.gameBoard;
    }

    private List<Move> calculateLegalMoves(final Alliance pieceAlliance) {
        List<Move> legalMoves = new ArrayList<>();
        List<Piece> activePieces = this.getActivePieces(pieceAlliance);
        for (final Piece piece : activePieces) {
            legalMoves.addAll(piece.calculateLegalMoves(this));
        }
        return Collections.unmodifiableList(legalMoves);
    }

    public Tile getTile(final int tilePosition) {
        return this.gameBoard.get(tilePosition);
    }

    public WhitePlayer getWhitePlayer() {
        return this.whitePlayer;
    }

    public BlackPlayer getBlackPlayer() {
        return this.blackPlayer;
    }

    public List<Piece> getWhitePieces() {
        return this.whitePieces;
    }

    public List<Piece> getBlackPieces() {
        return this.blackPieces;
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public List<Move> getAllLegalMoves() {
        return Stream.concat(this.whitePlayer.getLegalMoves().stream(), this.blackPlayer.getLegalMoves().stream()).collect(Collectors.toList());
    }

    private Board(final Builder builder) {
        this.gameBoard = Board.createBoardTiles(builder);
        this.whitePieces = this.calculatePieces(Alliance.WHITE);
        this.blackPieces = this.calculatePieces(Alliance.BLACK);
        final List<Move> whiteLegalMoves = this.calculateLegalMoves(Alliance.WHITE);
        final List<Move> blackLegalMoves = this.calculateLegalMoves(Alliance.BLACK);
        this.whitePlayer = new WhitePlayer(this, whiteLegalMoves, blackLegalMoves);
        this.blackPlayer = new BlackPlayer(this, blackLegalMoves, whiteLegalMoves);
        this.currentPlayer = builder.nextMoveMaker == Alliance.WHITE ? this.whitePlayer : this.blackPlayer;
    }

    public static Board createStandardBoard() {
        Builder builder = new Builder();
        //Set black pieces
        builder.setPiece(0, new Rook(0, Alliance.BLACK));
        builder.setPiece(1, new Knight(1, Alliance.BLACK));
        builder.setPiece(2, new Bishop(2, Alliance.BLACK));
        builder.setPiece(3, new Queen(3, Alliance.BLACK));
        builder.setPiece(4, new King(4, Alliance.BLACK));
        builder.setPiece(5, new Bishop(5, Alliance.BLACK));
        builder.setPiece(6, new Knight(6, Alliance.BLACK));
        builder.setPiece(7, new Rook(7, Alliance.BLACK));
        builder.setPiece(8, new Pawn(8, Alliance.BLACK));
        builder.setPiece(9, new Pawn(9, Alliance.BLACK));
        builder.setPiece(10, new Pawn(10, Alliance.BLACK));
        builder.setPiece(11, new Pawn(11, Alliance.BLACK));
        builder.setPiece(12, new Pawn(12, Alliance.BLACK));
        builder.setPiece(13, new Pawn(13, Alliance.BLACK));
        builder.setPiece(14, new Pawn(14, Alliance.BLACK));
        builder.setPiece(15, new Pawn(15, Alliance.BLACK));
        //Set white pieces
        builder.setPiece(48, new Pawn(48, Alliance.WHITE));
        builder.setPiece(49, new Pawn(49, Alliance.WHITE));
        builder.setPiece(50, new Pawn(50, Alliance.WHITE));
        builder.setPiece(51, new Pawn(51, Alliance.WHITE));
        builder.setPiece(52, new Pawn(52, Alliance.WHITE));
        builder.setPiece(53, new Pawn(53, Alliance.WHITE));
        builder.setPiece(54, new Pawn(54, Alliance.WHITE));
        builder.setPiece(55, new Pawn(55, Alliance.WHITE));
        builder.setPiece(56, new Rook(56, Alliance.WHITE));
        builder.setPiece(57, new Knight(57, Alliance.WHITE));
        builder.setPiece(58, new Bishop(58, Alliance.WHITE));
        builder.setPiece(59, new Queen(59, Alliance.WHITE));
        builder.setPiece(60, new King(60, Alliance.WHITE));
        builder.setPiece(61, new Bishop(61, Alliance.WHITE));
        builder.setPiece(62, new Knight(62, Alliance.WHITE));
        builder.setPiece(63, new Rook(63, Alliance.WHITE));
        builder.setNextMoveMaker(Alliance.WHITE);
        return builder.build();
    }

    @Override
    public String toString() {
        StringBuilder boardString = new StringBuilder();
        for (int i = 1; i <= BoardUtils.BOARD_ROWS; i++) {
            for (int j = 1; j <= BoardUtils.BOARD_COLUMNS; j++) {
                boardString.append(this.getTile(BoardUtils.getPieceCoordinate(new Pair<>(i, j))).toString());
                if (j == BoardUtils.BOARD_COLUMNS) {
                    boardString.append('\n');
                } else {
                    boardString.append(' ');
                }
            }
        }
        return boardString.toString();
    }

    public static final class Builder {
        private final Map<Integer, Piece> boardConfig;
        private Alliance nextMoveMaker;

        public Builder() {
            this.boardConfig = new HashMap<>();
            this.nextMoveMaker = null;
        }

        public void setPiece(final int piecePosition, final Piece piece) {
            this.boardConfig.put(piecePosition, piece);
        }

        public void setNextMoveMaker(final Alliance nextMoveMakerAlliance) {
            this.nextMoveMaker = nextMoveMakerAlliance;
        }

        public Piece getPiece(final int piecePosition) {
            if (this.boardConfig.containsKey(piecePosition)) {
                return this.boardConfig.get(piecePosition);
            }
            return null;
        }

        public Board build() {
            if (this.nextMoveMaker == null) {
                throw new RuntimeException("Next player must be set!");
            }
            return new Board(this);
        }
    }
}
