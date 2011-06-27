
#include "stdafx.h"

#include "Board.h"
#include "Piece.h"
#include "MovePath.h"
#include "Constants.h"

#include <iostream>

using namespace std;

Board::Board(int width, int height, int sliding, int pieceStartX)
{
	size = width*height;
	maxEquity = 0;
	
	board = new char[size];
	validV = new int[width];
	possibleV = new int[width];

	this->width = width;
	this->height = height;
	this->sliding = sliding;
	this->pieceStartX = pieceStartX;
	
	for (int i=1; i<=MAX_LEVELS; i++)
		movePathList[i] = new MovePath(width, height, pieceStartX);

	for (i=0; i<size; i++)
		board[i] = 0;

  this->outline = new int[width+1];

	debug = 0;
}


Board::Board(Board *copyBoard)
{
	width = copyBoard->width;
	height = copyBoard->height;
	size = copyBoard->size;
	sliding = copyBoard->sliding;
	pieceStartX = copyBoard->pieceStartX;
	board = new char[size];
	validV = new int[width];
	possibleV = new int[width];

	for (int i=1; i<=MAX_LEVELS; i++)
		movePathList[i] = new MovePath(width, height, pieceStartX);

	for (i=0; i<size; i++)
		board[i] = copyBoard->board[i];

	for (i=0; i<width; i++)
	{
		validV[i] = copyBoard->validV[i];
		possibleV[i] = copyBoard->possibleV[i];
	}

    this->outline = new int[height + 1];

	int *copyOutline = copyBoard->getOutline();

	for (i=0; i<=width; i++)
		outline[i] = copyOutline[i];

	debug = copyBoard->debug;
}


Board::~Board()
{
	delete [] board;
	delete [] validV;
	delete [] possibleV;
	delete [] outline;

	for (int i=1; i<=MAX_LEVELS; i++)
		delete movePathList[i];
}


void Board::copy(Board *copyBoard)
{
	for (int i=0; i<size; i++)
		board[i] = copyBoard->board[i];
}


void Board::copyWithWalls(Board *copyBoard, int showNext)
{
	clear();

	int x,y;
	int x2 = copyBoard->getWidth();

	for (y=0; y<height; y++)
		for (x=0; x<width; x++)
			if (showNext && y<=1 && x<=4)
				set(x, y, 0);
			else
				set(x, y, 8);

	for (y=0; y<copyBoard->getHeight(); y++)
		for (x=0; x<x2; x++)
			set(x+6, y, copyBoard->get(x, y));
}


void Board::clear()
{
	for (int i=0; i<size; i++)
		board[i] = 0;
}


int Board::getClearedLines(int ymin, int pieceHeight)
{
	int clearedLinesBit = 0;

	for (int y=ymin+pieceHeight-1; y>=ymin; y--)
	{
		clearedLinesBit = clearedLinesBit << 1;

		if (countLine(y) == width)
			clearedLinesBit |= 1;
	}

	return clearedLinesBit;
}

void Board::flashLines(int ymin, int pieceHeight, int clearedLinesBit)
{
	for (int y=ymin; y<ymin+pieceHeight; y++)
	{
		if (clearedLinesBit & 1)
			for (int x=0; x<width; x++)
				toggle(x, y);

		clearedLinesBit = clearedLinesBit >> 1;
	}
}


int Board::clearLines(int ymin, int pieceHeight)
{
	int y1;
	int y2;
	int clearedLines = 0;

	y1 = ymin + pieceHeight;

	while (--y1 >= ymin)
	{
		if (countLine(y1) == width)
		{
			clearedLines++;
			break;
		}
	}
			
	if (clearedLines == 0)
		return 0;			// No rows to clear

	y2 = y1;

	while (y1 >= 0)
	{
		while (--y2 >= ymin)
			if (countLine(y2) == width)
				clearedLines++;
			else
				break;
				
		if (y2 >= 0)
			copyLine(y2, y1);
		else
			clearLine(y1);

		y1--;
	}
	
	return clearedLines;
}


void Board::copyLine(int from_y, int to_y)
{
	char *board1 = board + from_y * width;
	char *board2 = board + to_y * width;

	for (int x=0; x<width; x++)
		board2[x] = board1[x];
}


void Board::clearLine(int y)
{
	char *line = board + y*width;
	
	for (int x=0; x<width; x++)
		line[x] = 0;
}

int Board::countLine(int y)
{
	int cnt = 0;
	char *line = board + y*width;
	
	for (int x=0; x<width; x++)
		if (line[x] != 0)
			cnt++;
			
	return cnt;
}

int Board::initOutline()
{
    // Calculate the outline
    int minOutline = height;

    for (int x=0; x<width; x++)
    {
		outline[x] = height;

        for (int y=0; y<height; y++)
        {
            if (get(x, y) != 0)
            {
                if (minOutline > y)
                minOutline = y;

                outline[x] = y;
                break;
            }
        }
    }
  
    return minOutline;
}


void Board::setPossibleMoves(MoveList *moveList, int level, int preview, int p, int animate)
{
	moveList->init(level, preview, maxEquity);
	
	MovePath *movePath = getMovePath(level);

	if (animate)
		movePath->init(animate);

	Piece piece = Piece(this, p, level);
	
	if (piece.testPiece(0, pieceStartX, 0) == -1)
		return;		// Quit immediately if the start position is occupied

	// Clean the "legal moves" flags
	for (int i=0; i<width; i++) {
		validV[i] = 0;
		possibleV[i] = 0;
	}

	validV[pieceStartX] = 1;		// 1 = bit #0 set, legal move for v=0

	int x,y;
	int flags, vflags, pflags, bitflags, shifts;
	int maxv = Piece::getLength(p);

	if (maxv == 1) {
		bitflags = 0;
		shifts = 0;
	}
	else if (maxv == 2) {
		bitflags = 3;
		shifts = 1;
	}
	else {
		bitflags = 15;
		shifts = 3;
	}

	double ddv = 0.6;
	double ddx = 0.6;

	for (y=0; y<height; y++)
	{
		// 1. Set possible rotations in possibleV[].
		for (x=0; x<width; x++)
			possibleV[x] = piece.getPossibleRotations(x, y);

		vflags = 0;
		int fromx = 0;

		// 2. Rotate and move from left to right.
		if (isSlidingOn() || (!isSlidingOn() && y==0))
		{
			if (!isSlidingOn() && animate == ANIMATE_ON_TIME)
				movePath->setAnimate(ANIMATE_ON);

			for (x=0; x<width; x++)
			{
				setValidRotations(movePath, fromx, x, y, ddv, ddx, maxv, shifts, bitflags, vflags, animate);
				fromx = x;
			}

			vflags = 0;
			fromx = width-1;

			// 3. Rotate and move from right to left
			for (x=width-1; x>=0; x--)
			{
				setValidRotations(movePath, fromx, x, y, ddv, ddx, maxv, shifts, bitflags, vflags, animate);
				fromx = x;

				// Also test for x-1 if rotation is occupied.
				// if (sliding == SLIDING_ON2)
				//	vflags |= (vflags<<1 | vflags>>shifts) & ~vflags;
			}

			if (!isSlidingOn() && animate == ANIMATE_ON_TIME)
				movePath->setAnimate(ANIMATE_ON_TIME);
		}

		if (!isSlidingOn() && y==0)
		{
			int mx;
			int my;
			int index = 0;

			for (int v=0; v<maxv; v++)
			{
				piece.setV(v);
				mx = width - piece.getWidth();
				my = height - piece.getHeight();

				for (int x=0; x<=mx; x++)
				{
					long gc = globalCnt;
					int y2 = piece.dropPiece(x, 0);

					if (y2 < 0)
						continue;

					vflags = validV[x];
					pflags = possibleV[x];
					flags = vflags & pflags;

					if (flags & 1<<v)
					{
						moveList->add(v, x, y2);

						if (animate)
						{
							// Calculate the movePath for dropping the piece from possition x.
							for (int y3=0; y3<y2; y3++)
								movePath->moveY(x, y3, y3+1, maxv, flags);
						}
					}
				}
			}

			break;
		}
		else
		{
			if (y == height-1)
				for (x=0; x<width; x++)
					possibleV[x] = 0;
			else
				for (x=0; x<width; x++)
					possibleV[x] = piece.getPossibleRotations(x, y+1);

			// 4. Store possible moves.
			for (x=0; x<width; x++)
			{
				vflags = validV[x];
				pflags = possibleV[x];
				flags = vflags & ~pflags;

				if (flags > 0)
				{
					for (int v=0; v<maxv; v++)
					{
						if (flags & 1<<v)
						{
							if (animate)
							{
								MovePathStep *s = movePath->getMovePathStep(v, x, y);

								if (s->cost >= 0)
									moveList->add(v, x, y);
							}
							else
								moveList->add(v, x, y);
						}
					}
				}

				if (y < height-1)
				{
					flags = vflags & pflags;
					
					if (animate)
						movePath->moveY(x, y, y+1, maxv, flags);
					
					validV[x] = flags;
				}
			}
		}
	}
}



void Board::setValidRotations(MovePath *movePath, int fromx, int x, int y, double ddv, double ddx, int maxv, int shifts, int bitflags, int &vflags, int animate)
{
	int pflags = possibleV[x];
	int flags = (vflags & pflags);		// Bit set = Possible x-moves.

	if (animate && flags > 0 && fromx != x)
		movePath->moveX(fromx, x, y, maxv, flags, ddx);

	vflags = validV[x] | flags;

	// do nothing if all bits are cleared or set.
	if ((animate && pflags > 0 && vflags > 0) ||
	    (!animate && pflags > 0 && vflags > 0 && (vflags & bitflags) < bitflags))
	{
		for (int v=1; v<maxv; v++)
		{
			flags = (vflags<<1 | vflags>>shifts) & pflags;	// Bit set = Possible rotations.
			vflags |= flags;

			if (animate && flags > 0)
				movePath->rotateV(x, y, maxv, flags, ddv);
		}
	}

	validV[x] = vflags;
}


void Board::write(ofstream &outfile)
{
	int width = this->width;
	int height = this->height;
	
	for (int y=0; y<height; y++)
	{
		outfile << "#";
		
		for (int x=0; x<width; x++)
		{
			char c = board[y*width + x]; 
			
			if (c == 0)
				outfile << "-";
			else
				outfile << Piece::getChar(c);
		}
		outfile << "#\n";
	}	
	
	for (int x=0; x<width; x++)
		outfile << "#";
		
	outfile << "##\n\n";	
}

void Board::write(fstream &outfile)
{
	int width = this->width;
	int height = this->height;
	
	for (int y=0; y<height; y++)
	{
		outfile << "#";
		
		for (int x=0; x<width; x++)
		{
			char c = board[y*width + x]; 
			
			if (c == 0)
				outfile << "-";
			else
				outfile << Piece::getChar(c);
		}
		outfile << "#\n";
	}	
	
	for (int x=0; x<width; x++)
		outfile << "#";
		
	outfile << "##\n\n";	
}

void Board::print()
{
	int width = this->width;
	int height = this->height;
	
	for (int y=0; y<height; y++)
	{
		cout << "#";
		
		for (int x=0; x<width; x++)
		{
			char c = board[y*width + x]; 
			
			if (c == 0)
				cout << "-";
			else
				cout << Piece::getChar(c);
		}
		cout << "#\n";
	}	
	
	for (int x=0; x<width; x++)
		cout << "#";
		
	cout << "##\n\n";	
}
