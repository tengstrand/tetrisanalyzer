# TetrisAnalyzer (C++)

The C++ version of **TetrisAnalyzer** now uses [freeglut](https://freeglut.sourceforge.net/), an actively
maintained, open-source drop-in replacement for the original GLUT.

## Building with Microsoft Visual C++ 6.0

1. Install Microsoft Visual C++ 6.0.  
2. Download the freeglut binaries and copy:
   - `freeglut.h` → `C:\Program Files\Microsoft Visual Studio\VC98\Include\GL`
   - `freeglut.lib` → `C:\Program Files\Microsoft Visual Studio\VC98\Lib`
   - `freeglut.dll` somewhere on your `PATH` (for example `C:\Windows\System32`) or beside the executable.
3. Open `TetrisApp.dsw` in Visual C++.
4. Configure Precompiled Headers via `Project → Settings`:
   - `StdAfx.cpp`: *Create* precompiled header, through header `stdafx.h`.
   - `TetrisApp.cpp`: *Use* the precompiled header file (`.pch`).
5. Build and run.

## Building on macOS (and other modern toolchains)

1. Install dependencies (Homebrew example):
   ```bash
   brew install cmake freeglut
   ```
2. Configure and build with CMake:
   ```bash
   cmake -S . -B build
   cmake --build build
   ```
3. Launch the analyzer:
   ```bash
   ./build/TetrisAnalyzer
   ```

The same workflow also applies to Linux; install the equivalent packages (e.g. `freeglut3-dev`, `cmake`, etc.) and run the same CMake commands.

