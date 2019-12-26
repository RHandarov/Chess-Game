package com.chess.game.player;

import com.chess.game.board.Board;
import com.chess.game.pieces.Pawn;
import com.chess.game.pieces.Piece;

import java.util.List;

import static com.chess.game.board.Board.Builder;

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

    public abstract boolean isAttack();

    public abstract Piece getAttackingPiece();

    public static class NormalMove extends Move {
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

        @Override
        public boolean isAttack() {
            return false;
        }

        @Override
        public Piece getAttackingPiece() {
            return null;
        }
    }

    public static class AttackingMove extends Move {
        protected final Piece attackingPiece;

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

        @Override
        public boolean isAttack() {
            return true;
        }

        @Override
        public Piece getAttackingPiece() {
            return this.attackingPiece;
        }
    }

    public static final class PawnEnPassantAttackMove extends AttackingMove {

        public PawnEnPassantAttackMove(int newPieceCoordinate, Piece movingPiece, Piece attackingPiece) {
            super(newPieceCoordinate, movingPiece, attackingPiece);
        }

        @Override
        public Board execute(final Board board) {
            final Builder builder = new Builder();
            for (final Piece piece : board.getCurrentPlayer().getActivePieces()) {
                if (!this.movingPiece.equals(piece)) {
                    builder.setPiece(piece.getPiecePosition(), piece);
                }
            }
            for (final Piece piece : board.getCurrentPlayer().getOpponent().getActivePieces()) {
                if (!piece.equals(this.attackingPiece)) {
                    builder.setPiece(piece.getPiecePosition(), piece);
                }
            }
            builder.setPiece(this.newPieceCoordinate, Piece.createPiece(this.movingPiece.getPieceType(),
                                                                        this.newPieceCoordinate,
                                                                        this.movingPiece.getPieceAlliance()));
            builder.setNextMoveMaker(board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    public static final class PawnJump extends NormalMove {

        public PawnJump(int newPieceCoordinate, Piece movingPiece) {
            super(newPieceCoordinate, movingPiece);
        }

        @Override
        public Board execute(final Board board) {
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
            final Piece newPiece = Piece.createPiece(this.movingPiece.getPieceType(), this.newPieceCoordinate, this.movingPiece.getPieceAlliance());
            builder.setPiece(this.newPieceCoordinate, newPiece);
            builder.setEnPassantPawn((Pawn)newPiece);
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

        @Override
        public boolean isAttack() {
            return false;
        }

        @Override
        public Piece getAttackingPiece() {
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
