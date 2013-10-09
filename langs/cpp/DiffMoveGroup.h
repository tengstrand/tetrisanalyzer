
#ifndef __diffmovegroup__
#define __diffmovegroup__

#include "Constants.h"
#include "MoveList.h"

class DiffMoveGroup
{
	private:
		DiffMove diffMoveList[2][MAX_LEVELS+1][MAX_LEVELS+1];
		double cntMove[2][MAX_LEVELS+1];
		double cntPos[2][MAX_LEVELS+1];
		double cntTotalMove[2][MAX_LEVELS+1];
		double cntTotalPos[2][MAX_LEVELS+1];

	public:
		DiffMoveGroup();
		~DiffMoveGroup() {};
		void reset();
		void addCnt(int level, int preview, int move, int totalPos, int pos);
		void add(MoveList *moveList, int level, int preview);
		double getCntMove(int level, int preview) { return cntMove[preview-1][level]; }
		double getCntPos(int level, int preview) { return cntPos[preview-1][level]; }
		double getCntTotalMove(int level, int preview) { return cntTotalMove[preview-1][level]; }
		double getCntTotalPos(int level, int preview) { return cntTotalPos[preview-1][level]; }
		void skipLevel(int level, int preview);
		void getThreshold(DiffMoveThList *diffList, int level1, int level2, int preview);
};

#endif
