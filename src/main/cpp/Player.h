#ifndef __player__
#define __player__

#include "Brain.h"
#include "Board.h"
#include "Piece.h"
#include "Move.h"
#include "DiffMoveGroup.h"
#include "GameObserverList.h"

class Player
{
	private:
		Brain *brain;
		Board *board;
		Board *boardCopy[MAX_LEVELS+1];
		
	public:
		Player(Board *board);
		~Player();

		int dropPiece(Piece *piece, int x);
		Move * play(int *pieces, int level, int preview, int calculatePath, GameObserverList *gameObserverList);
		MoveList * play(int *pieces, int depth, int level, int preview, int calculatePath, Move *posMove, MoveList *posMoveList, GameObserverList *gameObserverList, DiffMoveGroup *diffMoveGroup);
		void play(int *pieces, int depth, int level, int preview, int calculatePath, MoveList *moveList, Move *posMove, MoveList *posMoveList, GameObserverList *gameObserverList, DiffMoveGroup *diffMoveGroup);

		double evaluate();
		double getMaxEquity() { return board->getMaxEquity(); }
};

#endif
