package com.chess.game.player;

import com.chess.game.Alliance;
import com.chess.game.board.Board;
import com.chess.game.pieces.King;
import com.chess.game.pieces.Piece;
import com.chess.game.pieces.PieceType;
import com.chess.game.player.Move.AttackingMove;

import java.util.List;

public abstract class Player {
    protected final Board board;
    protected final List<Move> legalMoves;
    protected final King king;
    private boolean isInCheck;

    private King establishKing() {
        List<Piece> pieces = this.getActivePieces();
        for (final Piece piece : pieces) {
            if (piece.getPieceType() == PieceType.KING) {
                return (King)piece;
            }
        }
        throw new RuntimeException("You must not reach here! The game board is invalid!");
    }

    private boolean isTileAttacked(final int tilePosition, final List<Move> opponentLegalMoves) {
        for (Move move : opponentLegalMoves) {
            if (move.newPieceCoordinate == tilePosition) {
                return true;
            }
        }
        return false;
    }

    public boolean isCastled() {
        return this.king.isCastled();
    }

    public Player(final Board board, final List<Move> legalMoves, final List<Move> opponentLegalMoves) {
        this.board = board;
        this.legalMoves = legalMoves;
        this.king = this.establishKing();
        this.isInCheck = this.isTileAttacked(this.king.getPiecePosition(), opponentLegalMoves);
    }

    public boolean isInCheck() {
        return this.isInCheck;
    }

    public King getKing() {
        return this.king;
    }

    public List<Move> getLegalMoves() {
        return this.legalMoves;
    }

    public abstract Alliance getAlliance();

    public boolean isLegalMove(final Move move) {
        return this.legalMoves.contains(move);
    }

    public MoveTransition makeMove(final Move move) {
        if (!this.isLegalMove(move)) {
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
        }
        if (move instanceof AttackingMove) {
            final AttackingMove attackingMove = (AttackingMove)move;
            if (attackingMove.getAttackingPiece().getPieceType() == PieceType.KING) {
                return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
            }
        }
        Board transitionBoard = move.execute(this.board);
        Player currentPlayer = transitionBoard.getCurrentPlayer().getOpponent();
        if (currentPlayer.isTileAttacked(currentPlayer.getKing().getPiecePosition(), currentPlayer.getOpponent().getLegalMoves())) {
            return new MoveTransition(this.board, move, MoveStatus.LEAVE_PLAYER_IN_CHECK);
        }
        return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
    }

    private boolean hasEscapeMove() {
        for (Move move : this.legalMoves) {
            if (this.makeMove(move).getMoveStatus() == MoveStatus.DONE) {
                return true;
            }
        }
        return false;
    }

    public boolean isInCheckMate() {
        return this.isInCheck && !this.hasEscapeMove();
    }

    public boolean isInStaleMate() {
        return !this.isInCheck && !this.hasEscapeMove();
    }

    public abstract Player getOpponent();
    public abstract List<Piece> getActivePieces();
}
