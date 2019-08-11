package com.chess.game.gui;

import com.chess.game.gui.Table.TilePanel;
import com.google.common.collect.Lists;

import java.util.List;

public enum BoardDirection {
    NORMAL {
        @Override
        public BoardDirection getOpposite() {
            return BoardDirection.REVERSED;
        }

        @Override
        public List<TilePanel> getTraverse(final List<TilePanel> tilePanels) {
            return tilePanels;
        }
    },
    REVERSED {
        @Override
        public BoardDirection getOpposite() {
            return BoardDirection.NORMAL;
        }

        @Override
        public List<TilePanel> getTraverse(final List<TilePanel> tilePanels) {
            return Lists.reverse(tilePanels);
        }
    };

    public abstract BoardDirection getOpposite();
    public abstract List<TilePanel> getTraverse(final List<TilePanel> piecss);
}
