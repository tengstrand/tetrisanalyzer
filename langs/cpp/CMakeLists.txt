cmake_minimum_required(VERSION 3.16)
project(TetrisAnalyzer LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)

set(SOURCES
    Board.cpp
    BrainJote20.cpp
    DiffMove.cpp
    DiffMoveGroup.cpp
    DiffMoveTh.cpp
    DiffMoveThList.cpp
    FileReader.cpp
    FileWriter.cpp
    Game.cpp
    GameInfo.cpp
    GameObserverList.cpp
    GameSettings.cpp
    GameView.cpp
    Move.cpp
    MoveList.cpp
    MovePath.cpp
    MovePathStep.cpp
    Piece.cpp
    PieceGeneratorRandom.cpp
    Player.cpp
    Random.cpp
    Render.cpp
    RenderText.cpp
    StdAfx.cpp
    TetrisApp.cpp
)

find_package(OpenGL REQUIRED)
find_package(GLUT REQUIRED)

add_executable(TetrisAnalyzer ${SOURCES})
target_include_directories(TetrisAnalyzer PRIVATE
    ${CMAKE_CURRENT_SOURCE_DIR}
    ${OPENGL_INCLUDE_DIR}
    ${OPENGL_INCLUDE_DIRS}
    ${GLUT_INCLUDE_DIR}
    ${GLUT_INCLUDE_DIRS}
)
target_link_libraries(TetrisAnalyzer PRIVATE ${OPENGL_LIBRARIES} ${GLUT_LIBRARIES})

if (APPLE)
    # freeglut installed via Homebrew provides GLUT-compatible headers/libs
    target_compile_definitions(TetrisAnalyzer PRIVATE GL_SILENCE_DEPRECATION)
endif ()

