# Tetris Analyzer

A Tetris playing AI, by Joakim Tengstrand.

## The Scala version

![Tetris Analyzer - Scala](langs/scala/tetris-analyzer.png)

Watch the computer play!
This highly optimized program can play 76,000 pieces per second on a MacBook Air M4 (set next piece to `off`, and turn on `max speed`)!

To run the program, clone this repo and go to `langs/scala` and execute:

```bash
./build.sh
./run.sh
```

## The C++ version

![Tetris Analyzer - C++](langs/cpp/tetris-analyzer-cpp.png)

Watch the computer play, up to 9 moves ahead!
This version plays 26,0000 pieces per second on a MacBook Air M4.

For more instructions, see [langs/cpp/README.md](langs/cpp/README.md)

## The Java version (if you want to optimize the board evaluator):

![Tetris Analyzer screenshot](langs/java/images/tetris-analyzer-tool.png)

To run the program, clone this repo and go to `langs/java` and execute:

```bash
./build.sh
./run.sh example/system.yaml example/five-games-parameter-areaWidthFactor1.yaml
```

For more instructions, see [langs/java/README.md](langs/java/README.md)


Copyright (c) Joakim Tengstrand. All rights reserved.

The use and distribution terms for this software are covered by the
Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
which can be found in the file epl-v10.html at the root of this distribution.
By using this software in any fashion, you are agreeing to be bound by the terms of this license.
You must not remove this notice, or any other, from this software.
