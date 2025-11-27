
#ifndef __brainjote20__
#define __brainjote20__

#include "Brain.h"
#include "Board.h"

class BrainJote20 : public Brain
{
	private:
	  double *lineVal;
		double maxEquity;

	public:
		BrainJote20(Board *board);
		virtual ~BrainJote20();
		virtual double evaluate();
		double getThreshold(int preview, int level) { return threshold[preview-1][level]; }
		void setThreshold(int preview, int level, double t) { threshold[preview-1][level] = t; }

		static double threshold[MAX_PREVIEW][MAX_LEVELS+1];
		static double yadd[21];
		static double linegap[10];
		static double space[11];
		static double spacey[21];
		static double spaceyw[21];
};

#endif
