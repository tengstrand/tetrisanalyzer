
#ifndef __brain__
#define __brain__

#include "Board.h"

class Brain
{
	protected:
		Board *board;

	public:
		Brain(Board *board) { this->board = board; };
		virtual ~Brain() {};
		virtual double evaluate() = 0;
		virtual double getThreshold(int preview, int level) = 0;
		virtual void setThreshold(int preview, int level, double t) = 0;
};

#endif
