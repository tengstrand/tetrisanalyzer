package com.github.tetrisanalyzer.settings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RaceGamesSettings implements Iterable<RaceGameSettings> {
    public final List<RaceGameSettings> games = new ArrayList<>();

    public void add(RaceGameSettings settings) {
        games.add(settings);
    }

    public RaceGameSettings get(int index) {
        Iterator<RaceGameSettings> iterator = iterator();

        for (int i=0; i<index; i++) {
            iterator.next();
        }
        return iterator.next();
    }

    public int size() {
        Iterator<RaceGameSettings> iterator = iterator();

        int cnt = 0;
        while (iterator.hasNext()) {
            iterator.next();
            cnt++;
        }
        return cnt;
    }

    @Override
    public Iterator<RaceGameSettings> iterator() {
        return new RaceGameSettingsIterator(games, true);
    }

    public Iterator<RaceGameSettings> allGamesIterator() {
        return new RaceGameSettingsIterator(games, false);
    }
}
