package com.github.tetrisanalyzer.settings;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.github.tetrisanalyzer.boardevaluator.BoardEvaluator;
import com.github.tetrisanalyzer.piecegenerator.PieceGenerator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomSystemSettings implements SystemSettings {
    public SettingsReader reader;
    public Map<String, GameSettings> gameSettings = new HashMap<>();
    public Map<String, PieceGenerator> pieceGenerators = new HashMap<>();
    public Map<String, BoardEvaluator> boardEvaluators = new HashMap<>();

    public static CustomSystemSettings fromString(String settings) {
        try {
            return new CustomSystemSettings((Map) new YamlReader(settings).read());
        } catch (YamlException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private CustomSystemSettings(Map settings) {
        this.reader = new SettingsReader(settings, "system settings");
        setGameRules();
        setPieceGenerators();
        setBoardEvaluators();
    }

    private void setGameRules() {
        List<Map> gameRules = reader.readMaps("game rules");

        for (Map map : gameRules) {
            String id = new SettingsReader(map, "system settings").readString("id");
            gameSettings.put(id, CustomGameSettings.fromMap(map));
        }
    }

    private void setPieceGenerators() {
        List<Map> pieceGenerators = reader.readMaps("piece generators");

        for (Map settings : pieceGenerators) {
            SettingsReader mapReader = new SettingsReader(settings, "piece generator");
            String id = mapReader.readString("id");
            Class clazz = mapReader.readClass("class");
            this.pieceGenerators.put(id, createPieceGenerator(clazz, settings));
        }
    }

    private void setBoardEvaluators() {
        List<Map> boardEvaluators = reader.readMaps("board evaluators");

        for (Map settings : boardEvaluators) {
            SettingsReader mapReader = new SettingsReader(settings, "piece generator");
            String id = mapReader.readString("id");
            Class clazz = mapReader.readClass("class");
            this.boardEvaluators.put(id, createBoardEvaluator(clazz, settings));
        }
    }

    private PieceGenerator createPieceGenerator(Class clazz, Map settings) {
        try {
            Constructor constructor = clazz.getConstructor(Map.class);
            return (PieceGenerator)constructor.newInstance(settings);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private BoardEvaluator createBoardEvaluator(Class clazz, Map settings) {
        try {
            Constructor constructor = clazz.getConstructor(Map.class);
            return (BoardEvaluator)constructor.newInstance(settings);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException(e.getTargetException());
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public GameSettings findGameRules(String id) {
        if (!gameSettings.containsKey(id)) {
            throw new IllegalArgumentException("Could not find game rule id '" + id + "' in " + gameSettings);
        }
        return gameSettings.get(id);
    }

    public BoardEvaluator findBoardEvaluator(String id) {
        if (!boardEvaluators.containsKey(id)) {
            throw new IllegalArgumentException("Could not find board evaluato id '" + id + "' in " + gameSettings);
        }
        return boardEvaluators.get(id);
    }
}
