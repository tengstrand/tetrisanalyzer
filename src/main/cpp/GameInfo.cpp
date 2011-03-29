
#include "GameInfo.h"

GameInfo::GameInfo(int maxCntblockbtat)
{
	this->maxCntblockstat = maxCntblockbtat;

	cntblockstat = new long[maxCntblockstat];
}

GameInfo::~GameInfo()
{
	delete [] cntblockstat;
}

void GameInfo::set(long *cntblockstat,
									 int games,
									 long cntPieces,
									 long cntLines,
									 long totalPieces, 
									 long totalLines,
									 long totalMoves,
									 long totalSlided,
									 double linesPerSecond,
									 double piecesPerSecond,
								 	 int d, int h, int m, double s,
									 long minLines,
									 long maxLines,
									 long linesPerGame)
{
	for (int i=0; i<maxCntblockstat; i++)
		this->cntblockstat[i] = cntblockstat[i];

	this->games = games;
	this->cntPieces = cntPieces;
	this->cntLines = cntLines;
	this->totalPieces = totalPieces;
	this->totalLines = totalLines;
	this->totalPieces = totalPieces;
	this->totalMoves = totalMoves;
	this->totalSlided = totalSlided;
	this->linesPerSecond = linesPerSecond;
	this->piecesPerSecond = piecesPerSecond;
	this->d = d;
	this->h = h;
	this->m = m;
	this->s = s;

	if (minLines == -1)
		this->minLines = 0;
	else
		this->minLines = minLines;
	this->maxLines = maxLines;
	this->linesPerGame = linesPerGame;
}

void GameInfo::getTime(int &d, int &h, int &m, double &s)
{
	d = this->d;
	h = this->h;
	m = this->m;
	s = this->s;
}
