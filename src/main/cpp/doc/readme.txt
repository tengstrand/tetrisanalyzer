
The C++ version, TetrisAnalyzer, is using GLUT: http://www.opengl.org/resources/libraries/glut.
GLUT is *not* open source and that's why I can't include it here!

TetrisAnalyzer was compiled using Microsoft Visual C++ 6.0

Set up the project in Microsoft Visual C++ 6.0
==============================================
- Install Microsoft Visual C++ 6.0.
- Download and "install" GLUT:
  - put glut.h in (e.g) C:\Program Files\Microsoft Visual Studio\VC98\Include\GL.
  - put glut32.lib in (e.g) C:\Program\Microsoft Visual Studio\VC98\Lib
  - put glut32.dll in your path (if running Windows, e.g. c:\windows\system32).
- Open TetrisApp.dsw with Visual C++.
- Select the menu 'Project -> Settings', Precompiled Headers:
  - StdAfx.cpp: Create compiled header, Through header: stdafx.h
  - TetrisApp.cpp: Use precompiled header file (.pch).
- Compile and run!
