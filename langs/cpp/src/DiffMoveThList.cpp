
#include "DiffMoveThList.h"
#include "DiffMoveTh.h"

DiffMoveThList::DiffMoveThList()
{
	index = 0;
	maxIndex = 0;
}

DiffMoveThList::~DiffMoveThList()
{
}

void DiffMoveThList::add(double thEquity)
{
	diffMoveList[maxIndex].thEquity = thEquity;

	if (maxIndex < MAX_DIFF_MOVE_LIST-1)
		maxIndex++;
}

DiffMoveTh* DiffMoveThList::getFirst()
{
	index = 0;

	if (maxIndex == 0)
		return 0;

	return &diffMoveList[index++];
}

DiffMoveTh* DiffMoveThList::getNext()
{
	if (index >= maxIndex)
		return 0;

	return &diffMoveList[index++];
}
