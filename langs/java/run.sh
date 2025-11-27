#!/bin/bash

# Run script for Tetris Analyzer

# Check if build directory exists
if [ ! -d "build/classes" ]; then
    echo "Build directory not found. Running build script first..."
    ./build.sh
fi

# Run the program
java -cp "build/classes:lib/yamlbeans-1.08.jar" \
     com.github.tetrisanalyzer.gui.TetrisAnalyzer \
     "$@"

