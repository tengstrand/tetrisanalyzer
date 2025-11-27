cmake_minimum_required(VERSION 3.16)
project(TetrisAnalyzer LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)

set(SOURCES
    src/Board.cpp
    src/BrainJote20.cpp
    src/DiffMove.cpp
    src/DiffMoveGroup.cpp
    src/DiffMoveTh.cpp
    src/DiffMoveThList.cpp
    src/FileReader.cpp
    src/FileWriter.cpp
    src/Game.cpp
    src/GameInfo.cpp
    src/GameObserverList.cpp
    src/GameSettings.cpp
    src/GameView.cpp
    src/Move.cpp
    src/MoveList.cpp
    src/MovePath.cpp
    src/MovePathStep.cpp
    src/Piece.cpp
    src/PieceGeneratorRandom.cpp
    src/Player.cpp
    src/Random.cpp
    src/Render.cpp
    src/RenderText.cpp
    src/StdAfx.cpp
    src/TetrisApp.cpp
)

find_package(OpenGL REQUIRED)
find_package(glfw3 3.3 QUIET CONFIG)

if (NOT glfw3_FOUND)
    find_path(GLFW3_INCLUDE_DIR
        NAMES GLFW/glfw3.h
        PATHS
            /opt/homebrew/include
            /opt/homebrew/opt/glfw/include
            /usr/local/include
            /usr/include
    )

    find_library(GLFW3_LIBRARY
        NAMES glfw3 glfw glfw3dll
        PATHS
            /opt/homebrew/lib
            /opt/homebrew/opt/glfw/lib
            /usr/local/lib
            /usr/lib
    )

    if (GLFW3_INCLUDE_DIR AND GLFW3_LIBRARY)
        set(GLFW3_INCLUDE_DIRS ${GLFW3_INCLUDE_DIR})
        set(GLFW3_LIBRARIES ${GLFW3_LIBRARY})
        set(glfw3_FOUND TRUE)
    else()
        message(FATAL_ERROR "Could not find GLFW. Install it or set glfw3_DIR to the GLFW CMake package.")
    endif()
endif()

add_executable(TetrisAnalyzer ${SOURCES})
target_include_directories(TetrisAnalyzer PRIVATE
    ${CMAKE_CURRENT_SOURCE_DIR}/src
    ${OPENGL_INCLUDE_DIR}
    ${OPENGL_INCLUDE_DIRS}
)

if (glfw3_FOUND)
    target_link_libraries(TetrisAnalyzer PRIVATE ${OPENGL_LIBRARIES} glfw)
else()
    target_include_directories(TetrisAnalyzer PRIVATE ${GLFW3_INCLUDE_DIRS})
    target_link_libraries(TetrisAnalyzer PRIVATE ${OPENGL_LIBRARIES} ${GLFW3_LIBRARIES})
endif()

if (APPLE)
    target_compile_definitions(TetrisAnalyzer PRIVATE GL_SILENCE_DEPRECATION)
endif ()

