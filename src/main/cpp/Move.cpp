
#include "Move.h"
#include "Constants.h"

Move::Move()
{
	this->v = 0;
	this->x = 0;
	this->y = 0;
	this->index = 0;
	this->slided = 0;
	this->clearedLines = 0;
}


void Move::set(int index, int v, int x, int y)
{
	this->v = v;
	this->x = x;
	this->y = y;
	this->index = index;
	this->slided = 0;
}


void Move::copy(Move *move)
{
	this->v = move->v;
	this->x = move->x;
	this->y = move->y;
	this->index = move->index;
	this->slided = move->slided;
	this->clearedLines = move->clearedLines;

	for (int i=0; i<=MAX_LEVELS; i++)
		this->equityList[i] = move->equityList[i];
}

int Move::lessThan(Move *move, int preview, int level)
{
	for (int i=level; i>= preview; i--)
	{
		if (move->getEquity(i) == 0)
		{
			if (this->getEquity(i) != 0)
				return 1;
		}
		else
		{
			if (this->getEquity(i) == 0)
				return 0;

			if (this->getEquity(i) < move->getEquity(i))
				return 1;
			else
				return 0;
		}
	}

	return 0;
}


void Move::cleanEquity(int preview, int level)
{
	for (int i=preview; i<=level; i++)
		if (equityList[i] == MAX_EQUITY)
			equityList[i] = 0;
}
