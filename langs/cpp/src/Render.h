#ifndef __render__
#define __render__

#include "Board.h"

class Render
{
	private:
		int x;
		int dx;
		Board *board;

	public:
		Render();
		Render(Board *board);
		~Render();
		void render();
};

#endif
