
#include "BrainJote20.h"
#include "Board.h"
#include "Piece.h"
#include "Player.h"
#include "Move.h"
#include "DiffMove.h"

#include <string.h>
#include <iostream>

#define START_INDEX -1

using namespace std;

Player::Player(Board *board)
{
	this->board = board;
	this->brain = new BrainJote20(board);
	
	int width = board->getHeight();
	int height = board->getWidth();

	for (int i=0; i<=MAX_LEVELS; i++)
		this->boardCopy[i] = new Board(width, height, 0, 0);
}


Player::~Player()
{	
	delete this->brain;

	for (int i=0; i<=MAX_LEVELS; i++)
		delete this->boardCopy[i];
}

Move * Player::play(int *pieces, int level, int preview, int calculatePath, GameObserverList *gameObserverList)
{
	DiffMoveGroup *diffMoveGroup = board->getDiffMoveGroup();
	MoveList *moveList = play(pieces, 0, level, preview, calculatePath, 0, 0, gameObserverList, diffMoveGroup);
	moveList->cleanEquity();

	Move *bestMove = moveList->getBestMove();

	if (bestMove > 0)
	{
		if (board->isSlidingOn())
		{
			Piece piece(board, *pieces, level);

			board->initOutline();
			Move *move = moveList->getFirstMove();

			while (move)
			{
				piece.setV(move->getV());
				int y = piece.dropPieceSkipInitOutline(move->getX(), 0);
				move->setSlided(move->getY() != y);
				move = moveList->getNextMove();
			}
		}
	}

	return bestMove;
}


MoveList* Player::play(int *pieces, int depth, int level, int preview, int calculatePath, Move *posMove, MoveList *posMoveList, GameObserverList *gameObserverList, DiffMoveGroup *diffMoveGroup)
{
	int p = *pieces;
	MoveList *moveList = board->getMoveList(level);

	if (p > 0)
		board->setPossibleMoves(moveList, level, preview, p, calculatePath);

	play(pieces, depth, level, preview, calculatePath, moveList, posMove, posMoveList, gameObserverList, diffMoveGroup);

	return moveList;
}


void Player::play(int *pieces, int depth, int level, int preview, int calculatePath, MoveList *moveList, Move *posMove, MoveList *posMoveList, GameObserverList *gameObserverList, DiffMoveGroup *diffMoveGroup)
{
	int p = *pieces;

	globalCnt++;
	long gc = globalCnt;

	if (p == 0)
	{
		double equity;
		double sumEquity = 0;
		
		for (int pp=1; pp<=7; pp++)
		{
			if (level == 1 && pp==5)
				pp=pp;

			*pieces = pp;
			moveList = play(pieces, depth, level, preview, calculatePath, 0, 0, gameObserverList, diffMoveGroup);

			equity = moveList->getBestEquity(level);

			if (equity == MAX_EQUITY)
				sumEquity +=  board->getMaxEquity();
			else
				sumEquity += equity;
		}

		*pieces = 0;
		equity = sumEquity / 7;

		posMoveList->updateBestMove(posMove, equity);

		return;
	}

	double equity;
	double equityThreshold;

	if (level > preview)
	{
		moveList->setLevel(level-1);
		play(pieces, depth, level-1, preview, calculatePath, moveList, 0, 0, gameObserverList, diffMoveGroup);

		if (depth == 0)
			gameObserverList->notify(moveList);

		equity = moveList->getBestEquity(level-1);
		moveList->setLevel(level);

		equityThreshold = equity + brain->getThreshold(preview, level-1);

		if (depth == 0 && moveList->skipLevel(equity, equityThreshold, posMoveList)) {
			diffMoveGroup->skipLevel(level, preview);
			diffMoveGroup->addCnt(level, preview, 0, moveList->getLength(), 0);
			return;
		}
	}

	boardCopy[level]->copy(board);

	Move *move = moveList->getFirstMove();
	Piece piece = Piece(board, p, level);

	while (move)
	{
		if (!(level > preview && move->getEquity(level-1) > equityThreshold))
		{
			piece.setPiece(move->getV(), move->getX(), move->getY());
			move->setClearedLines(board->clearLines(move->getY(), piece.getHeight()));

			if (level > 1)
			{
				if (posMove == 0)
					play(&pieces[1], depth+1, level-1, preview, calculatePath, move, moveList, gameObserverList, diffMoveGroup);
				else
					play(&pieces[1], depth+1, level-1, preview, calculatePath, posMove, posMoveList, gameObserverList, diffMoveGroup);
			}
			else
			{
				// level = 1
				double equity = evaluate();

				if (posMove == 0)
					moveList->updateBestMove(move, equity);
				else
					posMoveList->updateBestMove(posMove, equity);
			}

			if (move->getClearedLines() == 0)
				piece.clearPiece();
			else
				board->copy(boardCopy[level]);
		}

		move = moveList->getNextMove();
	}

	diffMoveGroup->add(moveList, level, preview);
	diffMoveGroup->addCnt(level, preview, 1, moveList->getLength(), moveList->getLength());
}


double Player::evaluate()
{		
	return brain->evaluate();
}
