# Tetris Analyzer

A Tetris playing AI, by Joakim Tengstrand.

## The Scala version

![Tetris Analyzer - Scala](langs/scala/tetris-analyzer.png)

Watch the computer play!
This highly optimized program can play 76,000 pieces per second on a MacBook Air M4! (set next piece to `off`, and turn on `max speed`).

To run the program, clone this repo and go to `langs/scala` and execute:

```bash
./build.sh
./run.sh
```

Press <F1> for help.

## The C++ version

![Tetris Analyzer - C++](langs/cpp/tetris-analyzer-cpp.png)

Watch the computer play, up to 9 moves ahead!
This version is not as performance-optimized as the Scala version, and can "only" play 26,0000 pieces per second on a MacBook Air M4.

For more instructions, see the [readme](langs/cpp/README.md)

## The parameter optimizing tool (in Java)

![Tetris Analyzer screenshot](langs/java/images/tetris-analyzer-tool.png)

This tool helps you find better parameter values used by the board evaluator, which decides how well it plays (more rows in average per game is better).

To run the program, clone this repo and go to `langs/java` and execute:

```bash
./build.sh
./run.sh example/system.yaml example/five-games-parameter-areaWidthFactor1.yaml
```

This tool is specifically written for the Scala version, but can also be used by versions written in other languages.

For more instructions, see the [readme](langs/java/README.md).


Copyright (c) Joakim Tengstrand. All rights reserved.

The use and distribution terms for this software are covered by the
Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
which can be found in the file epl-v10.html at the root of this distribution.
By using this software in any fashion, you are agreeing to be bound by the terms of this license.
You must not remove this notice, or any other, from this software.
