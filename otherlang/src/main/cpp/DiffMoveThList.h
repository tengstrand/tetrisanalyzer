
#ifndef __diffmovethlist__
#define __diffmovethlist__

#include "DiffMoveTh.h"

#define MAX_DIFF_MOVE_LIST 10

class DiffMoveThList
{
	private:
		int index;
		int maxIndex;
		DiffMoveTh diffMoveList[MAX_DIFF_MOVE_LIST];
	public:
		DiffMoveThList();
		~DiffMoveThList();
		add(double thEquity);
		DiffMoveTh *getFirst();
		DiffMoveTh *getNext();
};

#endif
