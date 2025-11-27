
#ifndef __gameview__
#define __gameview__

#include "Game.h"
#include "GameSettings.h"
#include "GameObserver.h"
#include "Board.h"
#include "GameInfo.h"
#include "MoveList.h"
#include "DiffMoveGroup.h"

class GameView : public GameObserver
{
	private:
		int viewWidth;
		int viewHeight;
		int viewX1;
		int viewY1;
		int viewX2;
		int viewY2;
		int showInfo;
		unsigned long milisec;
		int frameCnt;
		int copyBoardIndex;
		int frameRateDelay;
		Game *game;
		GameSettings *gameSettings;
		Board *board;
		Board *boardCopy[2];
		GameInfo *gi;
		MoveList *moveList;
		MoveList moveListData;
		DiffMoveGroup *diffMoveGroup;
		int viewHelp();
		int viewBoard(int origoX, double &size);
		int viewBoardInfo(int origoX);
		int viewMoveList(int origoX);
		void viewDiffMove(int origoX);
		void renderString(float x, float y, char *string);

	public:
		GameView(Game *game, int showInfo, int x1, int y1, int width, int height);
		~GameView();
		void render();
		void changeSize(int w, int h);
		void notify(MoveList *moveList);
		void notify(Board *board, GameInfo *gameInfo, int p1, int p2, int showStartPiece);
};

#endif
