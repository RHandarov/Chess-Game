package com.chess.game.player;

import com.chess.game.board.Board;
import com.chess.game.pieces.King;
import com.chess.game.pieces.Piece;
import com.chess.game.pieces.PieceType;

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

    public Player(final Board board, final List<Move> legalMoves, final List<Move> opponentLegalMoves) {
        this.board = board;
        this.legalMoves = legalMoves;
        this.king = this.establishKing();
        this.isInCheck = this.isTileAttacked(this.king.getPiecePosition(), opponentLegalMoves);
    }

    public boolean isInCheck() {
        return this.isInCheck;
    }

    //TODO: implement these methods
    public MoveTransition makeMove(final Move move) {
        return null;
    }

    public boolean isInCheckMate() {
        return false;
    }

    public boolean isInStaleMate() {
        return false;
    }

    public abstract Player getOpponent();
    public abstract List<Piece> getActivePieces();
}
