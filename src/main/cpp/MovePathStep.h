#ifndef __movepathstep__
#define __movepathstep__

class MovePathStep
{
	public:
		int v;
		int x;
		int y;
		int fromv;
		int fromx;
		int fromy;
		int timev;
		int timex;
		int cost;
		MovePathStep *next;
		MovePathStep();
		~MovePathStep();
		void reset(int v, int x, int y);
};

#endif
