package com.chess.game;

import com.chess.game.player.Move;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Utiles {
    private Utiles() {
        throw new RuntimeException();
    }

    public static List<Move> appendMoveLists(final List<Move> first, final List<Move> second) {
        final List<Move> result = new ArrayList<>();
        result.addAll(first);
        result.addAll(second);
        return Collections.unmodifiableList(result);
    }
}
