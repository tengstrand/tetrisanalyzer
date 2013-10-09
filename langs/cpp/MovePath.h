
#ifndef __movepath__
#define __movepath__

#include "MovePathStep.h"

class MovePath
{
	private:
		int width;
		int height;
		int pieceStartX;
		int animate;
		MovePathStep *movePathStep;

	public:
		MovePath(int width, int height, int pieceStartX);
		~MovePath();
		void init(int animate);
		void setAnimate(int animate) { this->animate = animate; }
		void copy(MovePath *movePathCopy);
		MovePathStep* getMovePathStep(int v, int x, int y) { return &movePathStep[y*width*4 + x*4 + v]; }
		void moveX(int fromx, int x, int y, int maxv, int flags, double ddx);
		void moveY(int x, int fromy, int y, int maxv, int flags);
		void rotateV(int x, int y, int maxv, int flags, double ddv);
		MovePathStep* getPath(int v, int x, int y);
};

#endif
