package com.github.tetrisanalyzer.settings;

public class Setting {
    public final String name;
    public final String value;

    public static Setting setting(String name, Object value) {
        return new Setting(name, value == null ? null : value.toString());
    }

    private Setting(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
