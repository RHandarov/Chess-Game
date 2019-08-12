package com.chess.game.pieces;

import com.chess.game.Alliance;
import com.chess.game.board.Board;
import com.chess.game.board.BoardUtils;
import com.chess.game.player.Move;
import com.chess.game.player.Move.AttackingMove;
import com.chess.game.player.Move.NormalMove;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Pawn extends Piece {
    private static final int[] ROW_OFFSETS = {1, 2, 1, 1};
    private static final int[] COLUMN_OFFSETS = {0, 0, 1, -1};

    public Pawn(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance);
    }

    @Override
    public List<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        final Pair<Integer, Integer> pieceRowAndColumnCoordinates = BoardUtils.getPieceRowAndColumnCoordinates(this.getPiecePosition());
        final boolean canMakeJump;
        if (this.getPieceAlliance() == Alliance.WHITE && pieceRowAndColumnCoordinates.getKey() == 7) {
            canMakeJump = true;
        } else if (this.getPieceAlliance() == Alliance.BLACK && pieceRowAndColumnCoordinates.getKey() == 2) {
            canMakeJump = true;
        } else {
            canMakeJump = false;
        }
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                final int pieceRowCandidate = pieceRowAndColumnCoordinates.getKey() + (ROW_OFFSETS[i] * this.getPieceAlliance().getDirection());
                final int pieceColumnCandidate = pieceRowAndColumnCoordinates.getValue() + COLUMN_OFFSETS[i];
                if (BoardUtils.isValidTile(new Pair<Integer, Integer>(pieceRowCandidate, pieceColumnCandidate))) {
                    final int pieceCoordinateCandidate = BoardUtils.getPieceCoordinate(new Pair<Integer, Integer>(pieceRowCandidate, pieceColumnCandidate));
                    if (!board.getTile(pieceCoordinateCandidate).isTileOccupied()) {
                        legalMoves.add(new NormalMove(pieceCoordinateCandidate, this));
                    }
                }
            } else if (i == 1 && canMakeJump) {
                final int pieceRowCandidate = pieceRowAndColumnCoordinates.getKey() + (ROW_OFFSETS[i] * this.getPieceAlliance().getDirection());
                final int pieceColumnCandidate = pieceRowAndColumnCoordinates.getValue() + COLUMN_OFFSETS[i];
                if (BoardUtils.isValidTile(new Pair<Integer, Integer>(pieceRowCandidate, pieceColumnCandidate))) {
                    final int pieceCoordinateCandidate = BoardUtils.getPieceCoordinate(new Pair<Integer, Integer>(pieceRowCandidate, pieceColumnCandidate));
                    if (!board.getTile(pieceCoordinateCandidate).isTileOccupied()) {
                        legalMoves.add(new NormalMove(pieceCoordinateCandidate, this));
                    }
                }
            }
            else {
                final int pieceRowCandidate = pieceRowAndColumnCoordinates.getKey() + (ROW_OFFSETS[i] * this.getPieceAlliance().getDirection());
                final int pieceColumnCandidate = pieceRowAndColumnCoordinates.getValue() + (COLUMN_OFFSETS[i] * this.getPieceAlliance().getDirection());
                if (BoardUtils.isValidTile(new Pair<Integer, Integer>(pieceRowCandidate, pieceColumnCandidate))) {
                    final int pieceCoordinateCandidate = BoardUtils.getPieceCoordinate(new Pair<Integer, Integer>(pieceRowCandidate, pieceColumnCandidate));
                    if (board.getTile(pieceCoordinateCandidate).isTileOccupied()) {
                        Piece attackingPiece = board.getTile(pieceCoordinateCandidate).getPiece();
                        legalMoves.add(new AttackingMove(pieceCoordinateCandidate, this, attackingPiece));
                    }
                }
            }
        }
        return Collections.unmodifiableList(legalMoves);
    }

    @Override
    public PieceType getPieceType() {
        return PieceType.PAWN;
    }
}
