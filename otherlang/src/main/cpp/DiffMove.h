
#ifndef __diffmove__
#define __diffmove__

#include "DiffMoveThList.h"

class DiffMove
{
	private:
		int index;
		long cnt;
		long cntDiff;
		long maxSize;
		long *diff;
		double errorSum;
		double *error;

		double step;
		double maxDiff;
		char buffer[50];
		
	public:
		DiffMove(double step=0.1, double maxDiff=10);
		~DiffMove();
		void copy(DiffMove *diffMove);
		void add(double d, double e);
		int hasMoreElements();
		void reset();
		long getMoves() { return cnt; }
		double getNext(double &equity);
		void getThreshold(DiffMoveThList *diffList, int level1, int level2);
		double getThreshold(double match, double &equity);
		double getStep() { return step; }
		double getMaxDiff() { return maxDiff; }
		double getDiffPercent() { return cntDiff/(double)cnt * 100.0; }
		double getTotalError() { return errorSum/cnt; }

};

#endif
