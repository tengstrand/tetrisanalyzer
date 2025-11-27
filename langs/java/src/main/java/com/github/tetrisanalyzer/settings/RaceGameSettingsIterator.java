package com.github.tetrisanalyzer.settings;

import java.util.Iterator;
import java.util.List;

public class RaceGameSettingsIterator implements Iterator<RaceGameSettings> {
    final boolean removeHidden;
    int index = 0;
    private final List<RaceGameSettings> games;

    public RaceGameSettingsIterator(List<RaceGameSettings> games, boolean removeHidden) {
        this.games = games;
        this.removeHidden = removeHidden;
    }

    @Override
    public boolean hasNext() {
        for (int i=index; i<games.size(); i++) {
            if (removeHidden && games.get(i).hide()) {
                continue;
            }
            return true;
        }
        return false;
    }

    @Override
    public RaceGameSettings next() {
        while (index < games.size() && (removeHidden && games.get(index).hide())) {
            index++;
        }
        if (index < games.size()) {
            return games.get(index++);
        }
        return null;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
