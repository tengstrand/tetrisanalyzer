#!/bin/bash

# Build script for Tetris Analyzer

# Create output directory
mkdir -p build/classes

# Compile all Java files
echo "Compiling Java files..."
find src/main/java -name "*.java" > sources.txt
javac -d build/classes \
      -cp "lib/yamlbeans-1.08.jar" \
      @sources.txt
rm sources.txt

if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo ""
    echo "To run the program, use:"
    echo "  java -cp \"build/classes:lib/yamlbeans-1.08.jar\" com.github.tetrisanalyzer.gui.TetrisAnalyzer example/system.yaml example/five-games-parameter-areaWidthFactor1.yaml"
else
    echo "Compilation failed!"
    exit 1
fi

