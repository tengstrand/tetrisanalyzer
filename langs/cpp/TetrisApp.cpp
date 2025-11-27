
#include "StdAfx.h"
#ifdef _WIN32
#include "resource.h"
#endif

#define GLFW_INCLUDE_NONE
#include <GLFW/glfw3.h>

#if defined(__APPLE__)
#include <OpenGL/gl.h>
#include <OpenGL/glu.h>
#else
#include <GL/gl.h>
#include <GL/glu.h>
#endif

#include <cstdlib>
#include <iostream>

#include "Game.h"
#include "GameSettings.h"
#include "Thread.h"
#include "Piece.h"
#include "FileWriter.h"
#include "GameView.h"
#include "Platform.h"

#define WINDOW_WIDTH 800
#define WINDOW_HEIGHT 420

#define DELAY_ARR_LENGTH 7

long globalCnt = 0;

GameSettings gameSettings;
Game *game;
GameView *gameView;
GLFWwindow *mainWindow = nullptr;

void renderScene(void)
{
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	if (gameView)
		gameView->render();
}

void changeSize(int w, int h)
{
	gameView->changeSize(w, h);
}

void processSpecialKeys(int key, int x, int y)
{
	gameSettings.setCurrentKey(key);

	// [Arrow Down]
	if (key == 103)
		gameSettings.setStepNext();

	if (key == 1)
		gameSettings.toggleViewHelp();

	if (key == 2)
		gameSettings.toggleViewBoard();

	if (key == 3)
		gameSettings.toggleViewBoardInfo();

	if (key == 4)
		gameSettings.toggleViewMoveList();

	if (key == 5)
		gameSettings.toggleViewDiffMove();
}


void processNormalKeys(unsigned char key, int x, int y)
{
	// Set level (1-9)
	if (key >= 49 && key <= 57)
		gameSettings.setLevel(key - 48);

	// n, N = [N]ext
	if (key == 110 || key == 78)
		gameSettings.togglePreview();

	// l, L = S[l]iding
	if (key == 108 || key == 76)
		gameSettings.setNextSliding();

	if (key == 97 || key == 65)
		gameSettings.toggleAnimate();

/*
	// a
	if (key == 97)
		gameSettings.nextAnimate();

	// A
	if (key == 65)
		gameSettings.prevAnimate();
*/
	// f (frameRateDelay++)
	if (key == 102)
		gameSettings.addFrameRateDelay(1);

	// F (frameRateDelay--)
	if (key == 70)
		gameSettings.addFrameRateDelay(-1);

	// d ()
	if (key == 100)
		gameSettings.addDelay(1);

	// D
	if (key == 68)
		gameSettings.addDelay(-1);

	// p, P
	if (key == 80 || key == 112)
		gameSettings.togglePause();

	// s, S [S]tep mode
	if (key == 115 || key == 83)
		gameSettings.toggleStepMode();

	gameSettings.setCurrentKey(key);
}



int mapSpecialKey(int key)
{
	switch (key)
	{
		case GLFW_KEY_F1: return 1;
		case GLFW_KEY_F2: return 2;
		case GLFW_KEY_F3: return 3;
		case GLFW_KEY_F4: return 4;
		case GLFW_KEY_F5: return 5;
		case GLFW_KEY_LEFT: return 100;
		case GLFW_KEY_UP: return 101;
		case GLFW_KEY_RIGHT: return 102;
		case GLFW_KEY_DOWN: return 103;
		default: return -1;
	}
}

void framebufferSizeCallback(GLFWwindow*, int width, int height)
{
	changeSize(width, height);
}

void keyCallback(GLFWwindow* window, int key, int scancode, int action, int mods)
{
	(void) scancode;
	(void) mods;
	if (action == GLFW_PRESS && key == GLFW_KEY_ESCAPE)
	{
		glfwSetWindowShouldClose(window, GLFW_TRUE);
		return;
	}

	if (action == GLFW_PRESS || action == GLFW_REPEAT)
	{
		int special = mapSpecialKey(key);
		if (special != -1)
			processSpecialKeys(special, 0, 0);
	}
}

void charCallback(GLFWwindow*, unsigned int codepoint)
{
	if (codepoint <= 255)
		processNormalKeys(static_cast<unsigned char>(codepoint), 0, 0);
}

void windowCloseCallback(GLFWwindow* window)
{
	(void) window;
	gameSettings.setExit(1);
	glfwSetWindowShouldClose(window, GLFW_TRUE);
}

void errorCallback(int error, const char* description)
{
	std::cerr << "GLFW error (" << error << "): " << (description ? description : "") << std::endl;
}

static int runApplication(int argc, char **argv)
{
	gameSettings.setDefaultParameters();
	game = new Game(&gameSettings);
	gameView = new GameView(game, 1, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
	game->attach(gameView);

	glfwSetErrorCallback(errorCallback);

	if (!glfwInit())
	{
		std::cerr << "Failed to initialize GLFW" << std::endl;
		return -1;
	}

#ifdef _WIN32
	glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 2);
	glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);
#endif

	mainWindow = glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, "Tetris Analyzer - by Joakim Tengstrand", nullptr, nullptr);

	if (!mainWindow)
	{
		std::cerr << "Failed to create GLFW window" << std::endl;
		glfwTerminate();
		return -1;
	}

	glfwMakeContextCurrent(mainWindow);
	glfwSwapInterval(1);

	glfwSetFramebufferSizeCallback(mainWindow, framebufferSizeCallback);
	glfwSetKeyCallback(mainWindow, keyCallback);
	glfwSetCharCallback(mainWindow, charCallback);
	glfwSetWindowCloseCallback(mainWindow, windowCloseCallback);

	int fbWidth = 0;
	int fbHeight = 0;
	glfwGetFramebufferSize(mainWindow, &fbWidth, &fbHeight);
	changeSize(fbWidth == 0 ? WINDOW_WIDTH : fbWidth, fbHeight == 0 ? WINDOW_HEIGHT : fbHeight);

	Thread thread(game);
	thread.start();

	while (!glfwWindowShouldClose(mainWindow))
	{
		if (gameSettings.getExit() == 1)
			glfwSetWindowShouldClose(mainWindow, GLFW_TRUE);

		renderScene();
		glfwSwapBuffers(mainWindow);
		glfwPollEvents();
	}

	gameSettings.setExit(1);

	// Wait for game thread to finish, but with a timeout to avoid hanging
	int timeout = 100; // 0.1 second timeout
	while (gameSettings.getExit() != 2 && timeout > 0)
	{
		platform::sleepMillis(10);
		timeout -= 10;
	}

	game->detach(gameView);

	glfwDestroyWindow(mainWindow);
	glfwTerminate();

	return 0;
}

#ifdef _WIN32
int APIENTRY WinMain(HINSTANCE hInstance,
                     HINSTANCE hPrevInstance,
                     LPSTR     lpCmdLine,
                     int       nCmdShow)
{
	char arg0[] = "TetrisAnalyzer";
	char *argv[] = { arg0, nullptr };
	int argc = 1;
	return runApplication(argc, argv);
}
#else
int main(int argc, char **argv)
{
	return runApplication(argc, argv);
}
#endif

