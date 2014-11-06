package com.github.tetrisanalyzer.settings;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomSystemSettings implements SystemSettings {
    private Map settings;
    private Map<String,GameSettings> gameSettings = new HashMap<>();

    public static CustomSystemSettings fromString(String settings) {
        try {
            return new CustomSystemSettings((Map) new YamlReader(settings).read());
        } catch (YamlException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private CustomSystemSettings(Map settings) {
        this.settings = settings;

        if (!settings.containsKey("game rules")) {
            throw new IllegalArgumentException("Expected to find attribute 'game rules'");
        }
        List<Map> gameRules = (List) settings.get("game rules");

        for (Map map : gameRules) {
            if (!map.containsKey("id")) {
                throw new IllegalArgumentException("Expected the attribute 'id' in the game rule: " + map);
            }
            gameSettings.put((String) map.get("id"), CustomGameSettings.fromMap(map));
        }

        int xx = 1;
    }
}
