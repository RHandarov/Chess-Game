package com.chess.game.gui;

import com.chess.game.board.Board;
import com.chess.game.board.BoardUtils;
import com.chess.game.board.Tile;
import com.chess.game.pieces.Piece;
import com.chess.game.player.Move;
import com.chess.game.player.MoveStatus;
import com.chess.game.player.MoveTransition;
import com.chess.game.player.ai.MiniMax;
import com.chess.game.player.ai.MoveStrategy;
import javafx.scene.control.Tab;
import javafx.util.Pair;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutionException;

import static javax.swing.SwingUtilities.*;

public final class Table extends Observable {
    private final JFrame gameBoard;
    private final BoardPanel boardPanel;
    private final GameSetup gameSetup;
    private Board board;
    private BoardDirection boardDirection;
    private Tile sourceTile;
    private Piece movedPiece;
    private Tile destinationTile;

    private final static Dimension BOARD_TABLE_DIMENSION = new Dimension(600, 600);
    private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
    private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);

    private final static Color lightTileColor = Color.decode("#FFFACD");
    private final static Color darkTileColor = Color.decode("#593E1A");
    private final static Table INSTANCE = new Table();

    private Table() {
        this.gameBoard = new JFrame("Chess game");
        this.gameBoard.setLayout(new BorderLayout());
        this.gameBoard.setSize(Table.BOARD_TABLE_DIMENSION);
        this.gameBoard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.boardDirection = BoardDirection.NORMAL;
        this.board = Board.createStandardBoard();
        this.boardPanel = new BoardPanel();
        this.gameSetup = new GameSetup(this.gameBoard, true);
        this.addObserver(new TableGameAIWatcher());
        this.gameBoard.add(this.boardPanel, BorderLayout.CENTER);
        final JMenuBar menuBar = new JMenuBar();
        this.populateMenu(menuBar);
        this.gameBoard.setJMenuBar(menuBar);
        this.gameBoard.setVisible(true);
    }

    public static Table get() {
        return INSTANCE;
    }

    private GameSetup getGameSetup() {
        return this.gameSetup;
    }

    private Board getGameBoard() {
        return this.board;
    }

    private void clear() {
        this.boardDirection = BoardDirection.NORMAL;
        this.board = Board.createStandardBoard();
        this.boardPanel.drawBoard();
    }

    private void populateMenu(final JMenuBar menuBar) {
        menuBar.add(this.createFileMenu());
        menuBar.add(this.createPreferenceMenu());
        menuBar.add(this.createOptionsMenu());
    }

    private JMenu createPreferenceMenu() {
        final JMenu preferenceMenu = new JMenu("Preference");
        final JMenuItem flipBoard = new JMenuItem("Flip the board");
        flipBoard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Table.this.boardDirection = Table.this.boardDirection.getOpposite();
                Table.this.boardPanel.drawBoard();
            }
        });
        preferenceMenu.add(flipBoard);
        return preferenceMenu;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem newGameItem = new JMenuItem("New game");
        newGameItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Table.this.clear();
            }
        });
        fileMenu.add(newGameItem);
        fileMenu.addSeparator();
        final JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(exitItem);
        return fileMenu;
    }

    private JMenu createOptionsMenu() {
        final JMenu optionsMenu = new JMenu("Options");
        final JMenuItem gameSetupItem = new JMenuItem("Setup Game");
        gameSetupItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Table.get().getGameSetup().promptUser();
                Table.get().setupUpdate(Table.get().getGameSetup());
            }
        });
        optionsMenu.add(gameSetupItem);
        return optionsMenu;
    }

    private void setupUpdate(final GameSetup gameSetup) {
        this.setChanged();
        this.notifyObservers(gameSetup);
    }

    private void moveMadeUpdate(final PlayerType playerType) {
        this.setChanged();
        this.notifyObservers(playerType);
    }

    private void updateGameBoard(final Board board) {
        this.board = board;
    }

    private BoardPanel getBoardPanel() {
        return this.boardPanel;
    }

    private static class TableGameAIWatcher implements Observer {
        @Override
        public void update(Observable o, Object arg) {
            if (Table.get().getGameSetup().isAIPlayer(Table.get().getGameBoard().getCurrentPlayer()) &&
                !Table.get().getGameBoard().getCurrentPlayer().isInCheckMate() &&
                !Table.get().getGameBoard().getCurrentPlayer().isInStaleMate()) {
                    final AIThinkTank thinkTank = new AIThinkTank();
                    thinkTank.execute();
            }
        }
    }

    private static class AIThinkTank extends SwingWorker<Move, String> {
        private AIThinkTank() {

        }

        @Override
        protected Move doInBackground() throws Exception {
            final MoveStrategy miniMax = new MiniMax(4);
            final Move bestMove = miniMax.execute(Table.get().getGameBoard());
            return bestMove;
        }

        @Override
        public void done() {
            try {
                final Move bestMove = this.get();
                Table.get().updateGameBoard(Table.get().getGameBoard().getCurrentPlayer().makeMove(bestMove).getTransitionBoard());
                Table.get().getBoardPanel().drawBoard();
                Table.get().moveMadeUpdate(PlayerType.COMPUTER);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public final class BoardPanel extends JPanel {
        private final List<TilePanel> boardTiles;

        public BoardPanel() {
            super(new GridLayout(8, 8));
            this.boardTiles = new ArrayList<>();
            for (final Tile tile : Table.this.board.getTiles()) {
                final TilePanel tilePanel = new TilePanel(this, tile.getTileCoordinate());
                this.boardTiles.add(tilePanel);
                this.add(tilePanel);
            }
            this.setPreferredSize(Table.BOARD_PANEL_DIMENSION);
            this.validate();
        }

        public void drawBoard() {
            this.removeAll();
            for (final TilePanel tilePanel : Table.this.boardDirection.getTraverse(this.boardTiles)) {
                tilePanel.drawTile();
                this.add(tilePanel);
            }
            this.validate();
            this.repaint();
        }
    }

    public final class TilePanel extends JPanel {
        private final int tileId;

        public TilePanel(final BoardPanel boardPanel, final int tileId) {
            super(new GridBagLayout());
            this.tileId = tileId;
            this.assignTileColor();
            this.assignPieceImage();
            this.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        if (Table.this.sourceTile == null) {
                            Table.this.destinationTile = null;
                            Table.this.sourceTile = Table.this.board.getTile(TilePanel.this.tileId);
                            Table.this.movedPiece = Table.this.sourceTile.getPiece();
                            if (Table.this.movedPiece == null) {
                                Table.this.sourceTile = null;
                            } else if (Table.this.movedPiece.getPieceAlliance() != Table.this.board.getCurrentPlayer().getAlliance()) {
                                Table.this.sourceTile = null;
                                Table.this.movedPiece = null;
                            }
                        } else {
                            Table.this.destinationTile = Table.this.board.getTile(TilePanel.this.tileId);
                            final Move move = Move.MoveFactory.createMove(Table.this.board,
                                                                          Table.this.sourceTile.getTileCoordinate(),
                                                                          Table.this.destinationTile.getTileCoordinate());
                            final MoveTransition moveTransition = Table.this.board.getCurrentPlayer().makeMove(move);
                            if (moveTransition.getMoveStatus() == MoveStatus.DONE) {
                                Table.this.board = moveTransition.getTransitionBoard();
                                Table.this.sourceTile = null;
                                Table.this.movedPiece = null;
                                Table.this.destinationTile = null;
                            }
                        }
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        Table.this.sourceTile = null;
                        Table.this.movedPiece = null;
                        Table.this.destinationTile = null;
                    }
                    invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            Table.this.boardPanel.drawBoard();
                            if (Table.get().getGameSetup().isAIPlayer(Table.get().getGameBoard().getCurrentPlayer())) {
                                Table.get().moveMadeUpdate(PlayerType.HUMAN);
                            }
                        }
                    });
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
            this.setPreferredSize(Table.TILE_PANEL_DIMENSION);
            this.validate();
        }

        public void drawTile() {
            this.removeAll();
            this.assignTileColor();
            this.assignPieceImage();
            this.validate();
            this.repaint();
        }

        private void assignPieceImage() {
            this.removeAll();
            if (Table.this.board.getTile(this.tileId).isTileOccupied()) {
                final Piece piece = Table.this.board.getTile(this.tileId).getPiece();
                final String pieceImageFileName = piece.getPieceAlliance().toString().substring(0, 1) + piece.toString().toUpperCase();
                try {
                    final BufferedImage pieceImage = ImageIO.read(new File("art/" + pieceImageFileName + ".gif"));
                    this.add(new JLabel(new ImageIcon(pieceImage)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void assignTileColor() {
            final Pair<Integer, Integer> tileCoordinates = BoardUtils.getPieceRowAndColumnCoordinates(this.tileId);
            if (tileCoordinates.getKey() % 2 == tileCoordinates.getValue() % 2) {
                this.setBackground(Table.lightTileColor);
            } else {
                this.setBackground(Table.darkTileColor);
            }
        }
    }

    enum PlayerType {
        HUMAN,
        COMPUTER
    }
}
