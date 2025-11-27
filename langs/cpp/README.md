# TetrisAnalyzer (C++)

![TetrisAnalyzer](tetris-analyzer-cpp.png)

The C++ version of **TetrisAnalyzer** now uses [GLFW](https://www.glfw.org/), an actively
maintained, MIT-licensed alternative to GLUT that provides native windowing and input on every platform.

Note that this version is not as performance-optimized as the Scala implementation and is actually slower.
With proper optimization, it should be at least twice as fast as the Scala version.

## Building on macOS (and other modern toolchains)

1. Install dependencies (Homebrew example):
   ```bash
   brew install cmake glfw
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

GLFW is native on macOS (Cocoa), so no XQuartz or additional setup is required. On Linux, install the equivalent packages
(`cmake`, `libglfw3-dev`, etc.) and run the same commands. On Windows you can also use the CMake workflow by pointing to
the GLFW binaries and linking against `opengl32`, `glu32`, and `glfw3`.

## Building with Microsoft Visual C++ 6.0

1. Install Microsoft Visual C++ 6.0.  
2. Download the [GLFW prebuilt binaries](https://www.glfw.org/download.html) for Windows and copy:
   - `glfw3.h` (the whole `GLFW` include folder) → `C:\Program Files\Microsoft Visual Studio\VC98\Include`
   - `glfw3.lib` → `C:\Program Files\Microsoft Visual Studio\VC98\Lib`
   - `glfw3.dll` somewhere on your `PATH` (for example `C:\Windows\System32`) or next to the executable.
3. Open `legacy/TetrisApp.dsw` in Visual C++.
4. Configure Precompiled Headers via `Project → Settings`:
   - `StdAfx.cpp`: *Create* precompiled header, through header `stdafx.h`.
   - `TetrisApp.cpp`: *Use* the precompiled header file (`.pch`).
5. Link against `opengl32.lib`, `glu32.lib`, and `glfw3.lib`, then build and run.

