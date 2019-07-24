package com.chess.game.board;

import com.chess.game.Alliance;
import com.chess.game.pieces.King;
import com.chess.game.pieces.Piece;
import com.chess.game.pieces.PieceType;

import java.util.List;

public class Player {
    private final Board board;
    private final Alliance alliance;
    private final King king;

    public Alliance getAlliance() {
        return this.alliance;
    }

    public List<Piece> getPlayerPieces() {
        return this.alliance == Alliance.WHITE ? this.board.getWhitePieces() : this.board.getBlackPieces();
    }

    private King getPlayerKing() {
        final List<Piece> pieces = this.getPlayerPieces();
        for (final Piece piece : pieces) {
            if (piece.getPieceType() == PieceType.KING) {
                return (King)piece;
            }
        }
        throw new RuntimeException("The player cannot be without king!");
    }

    Player(final Board board, final Alliance alliance) {
        this.board = board;
        this.alliance = alliance;
        this.king = this.getPlayerKing();
    }
}
