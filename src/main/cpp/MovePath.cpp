
#include "MovePath.h"
#include "Piece.h"

#define TIME_MOVEX 500
#define TIME_MOVEY 500
#define TIME_ROTATEV 400

MovePath::MovePath(int width, int height, int pieceStartX)
{
	this->width = width;
	this->height = height;
	this->pieceStartX = pieceStartX;
	
	movePathStep = new MovePathStep[width*height*4];
}

MovePath::~MovePath()
{
	delete [] movePathStep;
}


void MovePath::init(int animate)
{
	this->animate = animate;

	MovePathStep *s = movePathStep;

	for (int y=0; y<height; y++) {
		for (int x=0; x<width; x++) {
			for (int v=0; v<4; v++) {
				s->reset(v, x, y);
				s++;
			}
		}
	}

	s = getMovePathStep(0, pieceStartX, 0);
	s->timex = TIME_MOVEY;
	s->timev = TIME_MOVEY;
	s->cost = 0;
	s->fromv = 0;
	s->fromx = pieceStartX;
	s->fromy = -1;
}


void MovePath::moveX(int fromx, int x, int y, int maxv, int flags, double ddx)
{
	for (int v=0; v<maxv; v++)
	{
		if (flags & 1<<v)
		{
			MovePathStep *s1 = getMovePathStep(v, fromx, y);

			if (s1->cost >= 0)
			{
				MovePathStep *s2 = getMovePathStep(v, x, y);
				int timex = s1->timex - TIME_MOVEX;

				if (s2->cost == -1 || s1->cost < s2->cost)
				{
					if (animate == ANIMATE_ON)
					{
						if (timex < 0)
							s2->cost = s1->cost - timex;
						else
							s2->cost = s1->cost;
					}
					else if (animate == ANIMATE_ON_TIME && timex >= 0)
						s2->cost = s1->cost;

					s2->timex = timex;
					s2->fromv = v;
					s2->fromx = fromx;
					s2->fromy = y;
				}
			}
		}
	}
}

void MovePath::rotateV(int x, int y, int maxv, int flags, double ddv)
{
	int fromv = maxv-1;

	for (int v=0; v<maxv; v++)
	{
		if (flags & 1<<v)
		{
			MovePathStep *s1 = getMovePathStep(fromv, x, y);

			if (s1->cost >= 0)
			{
				MovePathStep *s2 = getMovePathStep(v, x, y);
				int timev = s1->timev - TIME_ROTATEV;

				if (s2->cost == -1 || s1->cost < s2->cost)
				{
					if (animate == ANIMATE_ON)
					{
						if (timev < 0)
							s2->cost = s1->cost - timev;
						else
							s2->cost = s1->cost;
					}
					else if (animate == ANIMATE_ON_TIME && timev >= 0)
						s2->cost = s1->cost;

					s2->timev = timev;
					s2->fromv = fromv;
					s2->fromx = x;
					s2->fromy = y;
				}
			}
		}

		fromv = v;
	}
}


void MovePath::moveY(int x, int fromy, int y, int maxv, int flags)
{
	for (int v=0; v<maxv; v++)
	{
		if (flags & 1<<v)
		{
			MovePathStep *s1 = getMovePathStep(v, x, fromy);

			if (s1->cost >= 0)
			{
				MovePathStep *s2 = getMovePathStep(v, x, y);
				
				if (s1->timex < TIME_MOVEX)
					s2->timex = s1->timex + TIME_MOVEY;
				else
					s2->timex = s1->timex;

				if (s1->timev < TIME_ROTATEV)
					s2->timev = s1->timev + TIME_MOVEY;
				else
					s2->timev = s1->timev;

				s2->fromv = v;
				s2->fromx = x;
				s2->fromy = fromy;
				s2->cost = s1->cost;
			}
		}
	}
}


MovePathStep* MovePath::getPath(int v, int x, int y)
{
	MovePathStep *s = getMovePathStep(v, x, y);
	MovePathStep *prev = 0;

	while (1)
	{
		v = s->fromv;
		x = s->fromx;
		y = s->fromy;

		s->next = prev;

		if (y == -1)
			break;

		prev = s;
		s = getMovePathStep(v, x, y);
	} 

	return s;
}
