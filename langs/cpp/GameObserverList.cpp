
#include "Platform.h"
#include "GameObserverList.h"

GameObserverList::GameObserverList()
{
	index = 0;
}

GameObserverList::~GameObserverList()
{
}


void GameObserverList::attach(GameObserver *gameObserver)
{
	if (index >= MAX_OBSERVERS-1)
		return;

	gameObservers[index++] = gameObserver;
}

void GameObserverList::detach(GameObserver *gameObserver)
{
	if (index <= 0)
		return;

	index--;
}

void GameObserverList::notify(MoveList *moveList)
{
	for (int i=0; i<index; i++)
		gameObservers[i]->notify(moveList);
}


void GameObserverList::notify(
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
	long linesPerGame)
{
	double sec = (platform::getMilliseconds() - milisec2)/1000.0;

	double s = (platform::getMilliseconds() - milisec1)/1000.0;
	int d = s / (3600*24);
	s -= d * 3600*24;
	int h = s / 3600;
	s -= h*3600;
	int m = s / 60;
	s -= m*60;

	if (index > 0)
		gameInfo->set(
		    cntblockstat,
		    games,
			cntPieces,
			cntLines,
			totalPieces,
			totalLines,
			totalMoves,
			totalSlided,
			(sec == 0) ? 0 : (totalLines-totalLines2) / sec,
			(sec == 0) ? 0 : (totalPieces-totalPieces2) / sec,
			d, h, m, s,
			minLines,
			maxLines,
			linesPerGame);

	for (int i=0; i<index; i++)
		gameObservers[i]->notify(board, gameInfo, p1, p2, showStartPiece);
}
