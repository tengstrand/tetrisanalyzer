package com.github.tetrisanalyzer.gui;

public enum ViewMode {
    HELP(""),
    DISTRIBUTION("Distribution:"),
    DISTRIBUTION_AREA("Distribution area:"),
    ROWS_PER_GAME("Rows per game:"),
    EQUITY_DIFF("Equity diff");

    public final String viewName;

    ViewMode(String viewName) {
        this.viewName = viewName;
    }
}
