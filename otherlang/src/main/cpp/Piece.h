
#ifndef __piece__
#define __piece__

#include "Board.h"
#include "Constants.h"

class Piece
{
	private:
		Board *board;
	    int p;
	    int v;
	    int x;
	    int y;
	    int index;
	    int level;
	    int clearedLines;
		static int init_flag;
	    static int width[8][4];
	    static int height[8][4];
		static int block[8][4][4];
		static const int length[8];
		static const char pieceChr[9];
		static const char pieceHtmlChr[9];
		static const int outline[8][4][4];
		static const int shape[8][4][8];
		static const int rgb[9][3];
		
	public:
		Piece(Board *board, int p, int level=0, int init=0);
		Piece(Piece *piece);
		~Piece();

		void initStatic(int init=0);

		int getP() { return p; }
		int getV() { return v; }
		int getX() { return x; }
		int getY() { return y; }
		int getIndex() { return index; }
		int getWidth() { return width[p][v]; }
		int getHeight() { return height[p][v]; }
		int getWidth(int v) { return width[p][v]; }
		int getHeight(int v) { return height[p][v]; }
		const int* getOutline() { return outline[p][v]; }
		void setV(int v) { this->v = v; }

		void setClearedLines(int clearedLines) { this->clearedLines = clearedLines; }
		int getClearedLines() { return clearedLines; }
		int getLevel() { return level; }
		void setLevel(int level) { this->level = level; }
		int getPossibleRotations(int x, int y);
		
		void clearPiece();
		void clearPiece(int v, int x, int y, int boardWidth);
		void set(int v, int x, int y);
		void set(int v, int x, int y, int clearedLines);
		void setPiece();
		void setPiece(int v, int x, int y);
		void setPiece(int v, int x, int y, int boardWidth);
		int dropPiece(int px, int setPieceFlag = 1);
		int dropPieceSkipInitOutline(int px, int setPieceFlag);
		int settest(int v, int x, int y);
		int testPiece(int v, int x, int y);
		int compareMove(Piece &piece);
		int compareMove(int v, int x);
		static const int getLength(int p) { return length[p]; }
		static const char getChar(int p) { return pieceChr[p]; }
		static const char getHtmlChar(int p) { return pieceHtmlChr[p]; }

		static int testStartPos(Board *board, int p);
		static int getColorR(int p) { return rgb[p][0]; }
		static int getColorG(int p) { return rgb[p][1]; }
		static int getColorB(int p) { return rgb[p][2]; }

};

#endif
