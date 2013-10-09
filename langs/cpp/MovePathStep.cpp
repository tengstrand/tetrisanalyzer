
#include "MovePathStep.h"

MovePathStep::MovePathStep()
{
}

MovePathStep::~MovePathStep()
{
}

void MovePathStep::reset(int v, int x, int y)
{
	this->v = v;
	this->x = x;
	this->y = y;
	this->fromv = -1;
	this->fromx = -1;
	this->fromy = -1;
	this->timev = 0;
	this->timex = 0;
	this->cost = -1;
	this->next = 0;
}
