package com.chess.game.player;

import com.chess.game.board.Board;
import com.chess.game.pieces.Piece;
import com.sun.corba.se.impl.interceptors.SlotTableStack;

import java.util.List;

import static com.chess.game.board.Board.*;

public abstract class Move {
    protected final int newPieceCoordinate;
    protected final Piece movingPiece;

    public int getSourceDestination() {
        return this.movingPiece.getPiecePosition();
    }

    public int getDestinationDestination() {
        return this.newPieceCoordinate;
    }

    private Move(final int newPieceCoordinate, final Piece movingPiece) {
        this.newPieceCoordinate = newPieceCoordinate;
        this.movingPiece = movingPiece;
    }

    public abstract Board execute(final Board board);

    public static final class NormalMove extends Move {
        public NormalMove(final int newPieceCoordinate, final Piece movingPiece) {
            super(newPieceCoordinate, movingPiece);
        }

        @Override
        public Board execute(Board board) {
            Builder builder = new Builder();
            List<Piece> whitePieces = board.getWhitePieces();
            List<Piece> blackPieces = board.getBlackPieces();
            for (Piece piece : whitePieces) {
                if (!this.movingPiece.equals(piece)) {
                    builder.setPiece(piece.getPiecePosition(), piece);
                }
            }
            for (Piece piece : blackPieces) {
                if (!this.movingPiece.equals(piece)) {
                    builder.setPiece(piece.getPiecePosition(), piece);
                }
            }
            builder.setPiece(this.newPieceCoordinate, Piece.createPiece(this.movingPiece.getPieceType(), this.newPieceCoordinate, this.movingPiece.getPieceAlliance()));
            builder.setNextMoveMaker(board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    public static final class AttackingMove extends Move {
        private final Piece attackingPiece;

        public AttackingMove(final int newPieceCoordinate, final Piece movingPiece, final Piece attackingPiece) {
            super(newPieceCoordinate, movingPiece);
            this.attackingPiece = attackingPiece;
        }

        @Override
        public Board execute(Board board) {
            final Builder builder = new Builder();
            for (final Piece piece : board.getWhitePieces()) {
                if (!piece.equals(this.movingPiece) && !piece.equals(this.attackingPiece)) {
                    builder.setPiece(piece.getPiecePosition(), piece);
                }
            }
            for (final Piece piece : board.getBlackPieces()) {
                if (!piece.equals(this.movingPiece) && !piece.equals(this.attackingPiece)) {
                    builder.setPiece(piece.getPiecePosition(), piece);
                }
            }
            builder.setPiece(this.newPieceCoordinate, Piece.createPiece(this.movingPiece.getPieceType(), this.newPieceCoordinate, this.movingPiece.getPieceAlliance()));
            builder.setNextMoveMaker(board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    public static final class NullMove extends Move {
        public NullMove() {
            super(-1, null);
        }

        @Override
        public Board execute(Board board) {
            return null;
        }
    }

    public static final class MoveFactory {
        private static Move MULL_MOVE = new NullMove();

        private MoveFactory() {
            throw new RuntimeException();
        }

        public static Move createMove(final Board board, final int source, final int sink) {
            for (Move move : board.getAllLegalMoves()) {
                if (source == move.getSourceDestination() && sink == move.getDestinationDestination()) {
                    return move;
                }
            }
            return MoveFactory.MULL_MOVE;
        }
    }
}
