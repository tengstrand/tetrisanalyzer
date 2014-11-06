package com.github.tetrisanalyzer.settings;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.github.tetrisanalyzer.piecegenerator.PieceGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.tetrisanalyzer.settings.SettingsFunctions.checkKey;
import static com.github.tetrisanalyzer.settings.SettingsFunctions.classname;

public class CustomSystemSettings implements SystemSettings {
    private Map settings;
    private Map<String, GameSettings> gameSettings = new HashMap<>();
    private Map<String, PieceGenerator> pieceGenerators = new HashMap<>();

    public static CustomSystemSettings fromString(String settings) {
        try {
            return new CustomSystemSettings((Map) new YamlReader(settings).read());
        } catch (YamlException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private CustomSystemSettings(Map settings) {
        this.settings = settings;
        setGameRules();
        setPieceGenerators();

        int xx = 1;
    }

    private void setGameRules() {
        checkKey("game rules", settings);
        List<Map> gameRules = (List) settings.get("game rules");

        for (Map map : gameRules) {
            checkKey("id", map);
            gameSettings.put((String) map.get("id"), CustomGameSettings.fromMap(map));
        }
    }

    private void setPieceGenerators() {
        checkKey("piece generators", settings);

        List<Map> pieceGenerators = (List)settings.get("piece generators");

        for (Map pieceGenerator : pieceGenerators) {
            checkKey("id", pieceGenerator);
            Class clazz = classname(pieceGenerator);
            if (clazz == null) {
                throw new IllegalArgumentException("Expected parameter 'class' in map 'piece generators'");
            }

        }

        int xx = 1;
    }
}
