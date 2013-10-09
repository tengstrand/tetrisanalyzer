
#ifndef __move__
#define __move__

#include "Constants.h"

class Move
{
	private:
		int v;
		int x;
		int y;
		int index;
		int slided;
		int clearedLines;
		double equityList[MAX_LEVELS+1];

	public:
		Move();
		int getV() { return v; }
		int getX() { return x; }
		int getY() { return y; }
		int getIndex() { return index; }
		void set(int index, int v, int x, int y);
		void copy(Move *move);
		int isSlided() { return slided; }
		void setSlided(int slided) { this->slided = slided; }
		int lessThan(Move *move, int preview, int level);
		double getEquity(int level) { return equityList[level]; }
		void setEquity(int level, double equity) { equityList[level] = equity; }
		void cleanEquity(int preview, int level);
		int getClearedLines() { return clearedLines; }
		void setClearedLines(int clearedLines) { this->clearedLines = clearedLines; }
};

#endif
