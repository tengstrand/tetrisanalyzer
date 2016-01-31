package com.github.tetrisanalyzer.gui;

public enum ViewMode {
    HELP("", false),
    DISTRIBUTION("Distribution:", false),
    DISTRIBUTION_AREA("Distribution area:", true),
    ROWS_PER_GAME("Rows per game:", true),
    EQUITY_DIFF("Equity diff:", true);

    public final boolean isArea;
    public final String viewName;

    ViewMode(String viewName, boolean isArea) {
        this.viewName = viewName;
        this.isArea = isArea;
    }
}
