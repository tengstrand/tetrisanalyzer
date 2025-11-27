
#ifndef __gameobserverlist__
#define __gameobserverlist__

#include "GameObserver.h"

#define MAX_OBSERVERS 100

class GameObserverList
{
	private:
		int index;
		GameObserver *gameObservers[MAX_OBSERVERS];

	public:
		GameObserverList();
		~GameObserverList();
		void attach(GameObserver *gameObserver);
		void detach(GameObserver *gameObserver);
		void notify(MoveList *moveList);
		void notify(
		    Board *board,
		    GameInfo *gameInfo,
			int p1,
			int p2,
			int showStartPiece,
			unsigned long milisec1,
			unsigned long milisec2,
			long *cntblockstat,
			int games,
			long cntPieces,
			long cntLines,
			long totalMoves,
			long totalSlided,
			long totalLines,
			long totalLines2,
			long totalPieces,
			long totalPieces2,
			long minLines,
			long maxLines,
			long linesPerGame);
};

#endif
