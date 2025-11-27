
#ifndef __board__
#define __board__

#include <fstream>
#include "Move.h"
#include "MoveList.h"
#include "MovePath.h"
#include "DiffMoveGroup.h"
#include "Constants.h"

#define PIECE_BIT_MAX 128

class Board
{
	private:
		char *board;
		int *validV;
		int *possibleV;
        int *outline;
		int width;
		int height;
		int size;
		int sliding;
		int pieceStartX;
		double maxEquity;
		MovePath *movePathList[MAX_LEVELS+1];
		MoveList moveList[MAX_LEVELS+1];
		DiffMoveGroup diffMoveGroup;

		void setValidRotations(MovePath *movePath, int fromx, int x, int y, double ddv, double ddx, int maxv, int shifts, int bitflags, int &vflags, int animate);

	public:
		Board(Board *board);
		Board(int width, int height, int sliding, int pieceStartX);
		~Board();
		int debug;

		int getWidth() { return width; }
		int getHeight() { return height; }
		int getPieceStartX() { return pieceStartX; }
		char get(int index) { return *(board + index); }
		char get(int x, int y) { return *(board + x + width * y); }
		char get2(int x, int y) { int p = *(board + x + width * y); return (p>0 ? p : 0); }
		void set(int index, char p) { *(board + index) = p; }
		void set(int x, int y, char p) { *(board + x + width * y) = p; }
		void toggle(int x, int y) { *(board + x + width * y) ^= PIECE_BIT_MAX; }
		void setSliding(int sliding) { this->sliding = sliding; }
		int* getOutline() { return outline; }
		int getOutline(int x) { return outline[x]; }
		void setOutline(int x, int val) { outline[x] = val; }
		double getMaxEquity() { return maxEquity; }
		void setMaxEquity(double maxEquity) { this->maxEquity = maxEquity; }
		MoveList* getMoveList(int level) { return &moveList[level]; }
		MovePath* getMovePath(int level) { return movePathList[level]; }
		int isSlidingOn() { return (sliding != SLIDING_OFF); }

		DiffMoveGroup* getDiffMoveGroup() { return &diffMoveGroup; }
		int getMoves(int level, int p, double equity);
		void print();
		void clear();
		void write(std::fstream &outfile);
		void write(std::ofstream &outfile);
		void copy(Board *board);
		void copyWithWalls(Board *copyBoard, int showNext);
		void clearLine(int y);
		int clearLines(int pieceY, int PieceYmax);
		void flashLines(int ymin, int pieceHeight, int clearedLinesBit);
		int getClearedLines(int pieceY, int PieceYmax);
		void copyLine(int from_y, int to_y);
		int countLine(int y);
		int initOutline();
		void setPossibleMoves(MoveList *moveList, int level, int preview, int p, int animate);
};

#endif
