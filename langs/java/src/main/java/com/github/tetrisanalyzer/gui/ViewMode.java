package com.github.tetrisanalyzer.gui;

public enum ViewMode {
    HELP(""), DISTRIBUTION("Distribution:"), AREAS("Area:"), GAMES("Games:");

    public final String viewName;


    ViewMode(String viewName) {
        this.viewName = viewName;
    }
}
