
#include "Move.h"
#include "MoveList.h"
#include "DiffMove.h"
#include "Piece.h"
#include "Constants.h"

MoveList::MoveList(int maxLength)
{
	index = 0;
	length = 0;
    list = new Move[maxLength];

	for (int i=1; i<= MAX_LEVELS; i++)
		bestMove[i] = -1;
}

MoveList::~MoveList()
{
	delete [] list;
}

void MoveList::copy(MoveList *moveList)
{
	index = moveList->index;
	length = moveList->length;
	level = moveList->level;
	preview = moveList->preview;
	maxEquity = moveList->maxEquity;

	for (int i=0; i<MAX_LEVELS+1; i++)
		bestMove[i] = moveList->bestMove[i];

	for (int i=0; i<length; i++)
		list[i] = moveList->list[i];
}


Move* MoveList::getFirstMove()
{
	index = 0;

	return getNextMove();
}

Move* MoveList::getNextMove()
{
	if (index >= length)
		return 0;

	return &list[index++];
}


void MoveList::add(int v, int x, int y)
{
	list[index].set(index, v, x, y);

	for (int i=1; i<= level; i++)
		list[index].setEquity(i, MAX_EQUITY);

	index++;
	length = index;
}

void MoveList::swap(int index1, int index2)
{
	Move move;

	move.copy(&list[index1]);
	
	list[index1].copy(&list[index2]);
	list[index2].copy(&move);

	for (int i=preview; i<=level; i++)
	{
		if (index1 == bestMove[i])
			bestMove[i] = index2;
		else if (index2 == bestMove[i])
			bestMove[i] = index1;
	}
}


void MoveList::sort()
{
	for (int i=0; i<length-1; i++)
	{
		for (int j=i+1; j<length; j++)
		{
			Move *move1 = &list[0];
			Move *move2 = &list[1];
			Move *move3 = &list[2];
			if (list[j].lessThan(&list[i], preview, level))
				swap(i, j);
		}
	}
}

void MoveList::cleanEquity()
{
	for (int i=0; i<length; i++)
		list[i].cleanEquity(preview, level);
}


int MoveList::skipLevel(double equity, double equityThreshold, MoveList *posMoveList)
{
	if (equity != MAX_EQUITY)
	{
		int cnt = 0;
		Move *move = getFirstMove();

		while (move)
		{
			if (move->getEquity(level-1) <= equityThreshold)
			{
				cnt++;
				if (cnt > 1)
					return 0;
			}
			
			move = getNextMove();
		}
	}

	return 1;
}


void MoveList::init(int level, int preview, double maxEquity)
{
	this->index = 0;
	this->length = 0;
	this->level = level;
	this->preview = preview;
	this->maxEquity = maxEquity;

	for (int i=1; i<= level; i++)
		bestMove[i] = -1;
}




Move* MoveList::getBestMove()
{
	for (int i=level; i>=preview; i--)
		if (bestMove[i] != -1)
			return &list[bestMove[i]];

	return 0;
}

void MoveList::setDiffMove(DiffMove *diffMove, int level1, int level2)
{
	if (bestMove[level1] == -1 || bestMove[level2] == -1 ||
		  bestMove[level1] == bestMove[level2]) {
		diffMove->add(0, 0);
		return;
	}

	Move *move1 = &list[bestMove[level1]];
	Move *move2 = &list[bestMove[level2]];

	double error = move1->getEquity(level2) - move2->getEquity(level2);

	double equity1 = move1->getEquity(level1);
	double equity2 = move1->getEquity(level2);
	double diff = equity2 - equity1;

	if (equity1 == MAX_EQUITY || equity2 == MAX_EQUITY) {
		diffMove->add(0, 0);
		return;
	}

	if (level1 == 8 && level2 == 9)
		level1 = level1;

	diffMove->add(diff, error);
}

double MoveList::getBestEquity(int level)
{ 
	if (bestMove[level] == -1)
		return MAX_EQUITY;

	Move *move = &list[bestMove[level]];
	
	return move->getEquity(level);
}


void MoveList::updateBestMove(Move *move, double equity)
{
	if (bestMove[level] == -1)
		bestMove[level] = move->getIndex();
	else if (equity < list[bestMove[level]].getEquity(level))
		bestMove[level] = move->getIndex();

	if (equity < move->getEquity(level))
		move->setEquity(level, equity);
}


int MoveList::hasSlidedMoves()
{
	Move *move = getFirstMove();

	int slided = 0;

	while (move)
	{
		if (move->isSlided())
			return 1;
		
		move = getNextMove();
	}

	return 0;
}
