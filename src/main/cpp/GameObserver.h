
#ifndef __gameobserver__
#define __gameobserver__

#include "Board.h"
#include "GameInfo.h"
#include "MoveList.h"

class GameObserver
{
	public:
		GameObserver() {};
		virtual ~GameObserver() {};
		virtual void notify(MoveList *moveList) = 0;
		virtual void notify(Board *board, GameInfo *gameInfo, int p1, int p2, int showStartPiece) = 0;
};

#endif
