
#include "Constants.h"
#include "DiffMoveGroup.h"
#include "DiffMoveThList.h"

DiffMoveGroup::DiffMoveGroup()
{
	for (int i=0; i<=1; i++) {
		for (int j=1; j<=MAX_LEVELS; j++) {
			cntMove[i][j] = 0;
			cntPos[i][j] = 0;
		}
	}
}

void DiffMoveGroup::addCnt(int level, int preview, int move, int totalPos, int pos)
{
	cntTotalMove[preview-1][level]++;
	cntMove[preview-1][level] += move;
	cntTotalPos[preview-1][level] += totalPos;
	cntPos[preview-1][level] += pos;
}

void addCnt(int level, int preview, int move, int totalPos, int pos);


void DiffMoveGroup::reset()
{
	for (int i=1; i<=MAX_LEVELS-1; i++) {
		for (int j=i+1; j<=MAX_LEVELS; j++) {
			diffMoveList[0][i][j].reset();
			diffMoveList[1][i][j].reset();
		}
	}
}

void DiffMoveGroup::add(MoveList *moveList, int level, int preview)
{
	if (preview < 1 || preview > 2 || level < 1 || level > 9)
		return;

	for (int i=1; i<=level-1; i++)
		for (int j=i+1; j<=level; j++)
			moveList->setDiffMove(&diffMoveList[preview-1][i][j], i, j);
}

void DiffMoveGroup::skipLevel(int level, int preview)
{
	if (preview < 1 || preview > 2 || level < 1 || level > 9)
		return;

	for (int i=1; i<=level-1; i++)
		for (int j=i+1; j<=level; j++)
			diffMoveList[preview-1][i][j].add(0, 0);
}

void DiffMoveGroup::getThreshold(DiffMoveThList *diffList, int level1, int level2, int preview)
{
	if (preview < 1 || preview > 2 || level1 < 1 || level1 > 9 || level2 < 1 || level2 > 9)
		return;

	diffMoveList[preview-1][level1][level2].getThreshold(diffList, level1, level2);
}
