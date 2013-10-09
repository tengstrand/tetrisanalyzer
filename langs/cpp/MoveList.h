
#ifndef __movelist__
#define __movelist__

#include "Move.h"
#include "DiffMove.h"
#include "Constants.h"

class MoveList
{
	private:
		int index;
		int length;
		int level;
		int preview;
		double maxEquity;
		int bestMove[MAX_LEVELS+1];
		Move *list;

		void swap(int index1, int index2);

	public:
		MoveList(int maxLength = MAX_MOVES);
		~MoveList();

		int getLength() { return length; };
		Move* getMove() { return &list[index]; }
		Move* getFirstMove();
		Move* getNextMove();
		void updateBestMove(Move *move, double equity);
		Move* getBestMove();
		void setDiffMove(DiffMove *diffMove, int level1, int level2);
		double getBestEquity(int level);
		int hasSlidedMoves();

		void cleanEquity();
		int skipLevel(double equity, double equityThreshold, MoveList *posMoveList);

		void init(int level, int preview, double maxEquity);
		void add(int v, int x, int y);
		void setLevel(int level) { this->level = level; }
		void sort();
		copy(MoveList *moveList);
};

#endif
