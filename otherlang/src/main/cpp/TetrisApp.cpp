
#include "stdafx.h"
#include "resource.h"

#include <iostream.h>
#include <string.h>
#include <fstream>
#include <GL/glut.h>

#include "Game.h"
#include "GameSettings.h"
#include "Thread.h"
#include "Piece.h"
#include "FileWriter.h"
#include "GameView.h"

#define WINDOW_WIDTH 800
#define WINDOW_HEIGHT 420

#define DELAY_ARR_LENGTH 7

long globalCnt = 0;

GameSettings gameSettings;
Game *game;
GameView *gameView;

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



void renderScene(void)
{
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	gameView->render();

	glutSwapBuffers();

	//glFlush();
}


int APIENTRY WinMain(HINSTANCE hInstance,
                     HINSTANCE hPrevInstance,
                     LPSTR     lpCmdLine,
                     int       nCmdShow)
{
	int argc = 1;
	char xx[1000] = "jote";
	char *ptr = xx;
	char **argv = (char **)&ptr;

	gameSettings.setDefaultParameters();
	game = new Game(&gameSettings);
	gameView = new GameView(game, 1, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
	game->attach(gameView);

	glutInit(&argc, argv);
//	glutInitDisplayMode(GLUT_DEPTH | GLUT_SINGLE | GLUT_RGBA);
	glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGBA);
	glutInitWindowPosition(50,100);
	glutInitWindowSize(WINDOW_WIDTH, WINDOW_HEIGHT);
	glutCreateWindow("Tetris Analyzer - by Joakim Tengstrand");
	gameView->changeSize(WINDOW_WIDTH, WINDOW_HEIGHT);

	glutDisplayFunc(renderScene);
	glutIdleFunc(renderScene);
	glutReshapeFunc(changeSize);
	glutKeyboardFunc(processNormalKeys);
	glutSpecialFunc(processSpecialKeys);

	Thread thread(game);
	thread.start();

	glutMainLoop();

	game->detach(gameView);

	return 0;
}

