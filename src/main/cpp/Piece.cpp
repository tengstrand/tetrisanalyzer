
#include "Piece.h"
#include "Board.h"

#include <string.h>
#include <iostream>

int Piece::init_flag;
int Piece::width[8][4];
int Piece::height[8][4];
int Piece::block[8][4][4];

Piece::Piece(Board *board, int p, int level, int init)
{
	this->board = board;
	this->level = level;
	this->p = p;
	this->v = 0;
	this->x = 0;
	this->y = 0;
	this->index = 0;
	this->clearedLines = 0;

	initStatic(init);
}

Piece::Piece(Piece *piece)
{
	this->board = piece->board;
	this->p = piece->p;	
	this->v = piece->v;
	this->x = piece->x;
	this->y = piece->y;	
	this->index = piece->index;
	this->clearedLines = piece->getClearedLines();

	initStatic();
}

Piece::~Piece()
{
}

void Piece::initStatic(int init)
{
	if (init_flag == 0 || init)
	{
		init_flag = 1;

		for (int p=1; p<8; p++)
		{
			for (int v=0; v<4; v++)
			{
				int width = 0;
				int height = 0;
				
				for (int i=0; i<4; i++)
				{
					const int *ptr = &Piece::shape[p][v][i<<1];
					int x = *(ptr);
					int y = *(ptr+1);

					if (x > width)
						width = x;
						
					if (y > height)
						height = y;
						
					block[p][v][i] = x + y * (board->getWidth());
				}
				
				Piece::width[p][v] = width+1;
				Piece::height[p][v] = height+1;
			}
		}
	}
}


void Piece::set(int v, int x, int y, int clearedLines)
{
	this->clearedLines = clearedLines;

	set(v, x, y);
}


void Piece::set(int v, int x, int y)
{
	this->v = v;
	this->x = x;
	this->y = y; 
	this->index = x + y*(board->getWidth());
}

void Piece::setPiece(int v, int x, int y)
{
	set(v, x, y);
	setPiece();
}	

void Piece::setPiece()
{
	int *piece = block[p][v];
	
	for (int i=0; i<4; i++)
		this->board->set(index + piece[i], p);
}

void Piece::setPiece(int v, int x, int y, int boardWidth)
{
	set(v, x, y);

	int *piece = block[p][v];
	int boardWidth2 = board->getWidth();

	for (int i=0; i<4; i++)
	{
		int pi = piece[i];
		x = pi % boardWidth;
		y = pi / boardWidth;

		this->board->set(index + x + y * boardWidth2, p);
	}
}

void Piece::clearPiece()
{
	int i;
	int *piece = block[p][v];
	
	for (i=0; i<4; i++)
		board->set(index + piece[i], 0);
}

void Piece::clearPiece(int v, int x, int y, int boardWidth)
{
	set(v, x, y);

	int *piece = block[p][v];
	int boardWidth2 = board->getWidth();

	for (int i=0; i<4; i++)
	{
		int pi = piece[i];
		x = pi % boardWidth;
		y = pi / boardWidth;

		this->board->set(index + x + y * boardWidth2, 0);
	}
}


int Piece::testPiece(int v, int x, int y)
{
	int i;

	int boardWidth = board->getWidth();
	int boardHeight = board->getHeight();
	if (x+getWidth(v) > boardWidth || y+getHeight(v) > boardHeight)
		return -1;

	int *piece = block[p][v];
	int index = x + y*(board->getWidth());

	for (i=0; i<4; i++)
	{
		if (board->get(index + piece[i]) != 0)
			return -1;
	}

	return 0;	// OK
}


int Piece::getPossibleRotations(int x, int y)
{
	int flags = 0;				// Possible rotations
	int maxv = Piece::getLength(p);
	int boardWidth = board->getWidth();
	int boardHeight = board->getHeight();

	for (int v=0; v<maxv; v++)
	{
		if (x+getWidth(v) > boardWidth || y+getHeight(v) > boardHeight)
			continue;

		if (testPiece(v, x, y) == 0)
			flags |= 1<<v;
	}

	return flags;
}


int Piece::dropPiece(int px, int setPieceFlag)
{
	board->initOutline();
	
	return dropPieceSkipInitOutline(px, setPieceFlag);
}


int Piece::dropPieceSkipInitOutline(int px, int setPieceFlag)
{
	int width = board->getWidth();
	int height = board->getHeight();

	const int *poutline = getOutline();
	int *outline = board->getOutline();
	
	int xmax = getWidth();
	
	int ymin = height;
	
	for (int x=0; x<xmax; x++)
	{
		int y = outline[x+px] - poutline[x];
		
		if (y < ymin)
			ymin = y;
	}

	if (ymin >= 0 && setPieceFlag)
	{
		setPiece(v, px, ymin);
		this->clearedLines = board->clearLines(ymin, this->getHeight());
	}
	else
		this->clearedLines = 0;

	return ymin;
}


int Piece::compareMove(Piece &piece)
{
	return (piece.x == x && piece.y == y && piece.v == v);
}

int Piece::compareMove(int v, int x)
{
	return (v == this->v && x == this->x);
}

int Piece::testStartPos(Board *board, int p)
{
	// p = 0..6
	const int *piece = &shape[p][0][0];

	for (int i=0; i<8; i+= 2)
		if (board->get(piece[i] + board->getPieceStartX(), piece[i+1]) != 0)
			return -1;

	return 0;
}

const int Piece::length[8] = { 0, 2, 2, 2, 4, 4, 4, 1 };

const char Piece::pieceChr[9] = { '-', 'I', 'Z', 'S', 'J', 'L', 'T', 'O', '#' };
const char Piece::pieceHtmlChr[9] = { 'B', 'I', 'Z', 'S', 'J', 'L', 'T', 'O', 'W' };

const int Piece::outline[8][4][4] = 
{ { {0,0,0,0}, {0,0,0,0}, {0,0,0,0}, {0,0,0,0} },
	{ {1,1,1,1}, {4,0,0,0}, {1,1,1,1}, {4,0,0,0} }, 	// 1 = I, Red
	{ {1,2,2,0}, {3,2,0,0}, {1,2,2,0}, {3,2,0,0} },		// 2 = Z, Orange
	{ {2,2,1,0}, {2,3,0,0}, {2,2,1,0}, {2,3,0,0} },		// 3 = S, Cyan
	{ {1,1,2,0}, {3,1,0,0}, {2,2,2,0}, {3,3,0,0} },		// 4 = J, Yellow
	{ {2,1,1,0}, {3,3,0,0}, {2,2,2,0}, {1,3,0,0} },		// 5 = L, Violette
	{ {1,2,1,0}, {3,2,0,0}, {2,2,2,0}, {2,3,0,0} },		// 6 = T, Green
	{ {2,2,0,0}, {2,2,0,0}, {2,2,0,0}, {2,2,0,0} } 		// 7 = O, Blue
};

const int Piece::shape[8][4][8] = 
{ { {0,0, 0,0, 0,0, 0,0},
    {0,0, 0,0, 0,0, 0,0},
    {0,0, 0,0, 0,0, 0,0},
    {0,0, 0,0, 0,0, 0,0} },
	{ {0,0, 1,0, 2,0, 3,0}, 	// 1 = I, Red
    {0,0, 0,1, 0,2, 0,3},
    {0,0, 1,0, 2,0, 3,0},
    {0,0, 0,1, 0,2, 0,3} },
    
	{	{0,0, 1,0, 1,1, 2,1},		// 2 = Z, Orange
		{1,0, 0,1, 1,1, 0,2},
		{0,0, 1,0, 1,1, 2,1},
		{1,0, 0,1, 1,1, 0,2} },

	{	{1,0, 2,0, 0,1, 1,1},		// 3 = S, Cyan
		{0,0, 0,1, 1,1, 1,2},
		{1,0, 2,0, 0,1, 1,1},
		{0,0, 0,1, 1,1, 1,2} },

	{	{0,0, 1,0, 2,0, 2,1},		// 4 = J, Yellow
		{0,0, 1,0, 0,1, 0,2},
		{0,0, 0,1, 1,1, 2,1},
		{1,0, 1,1, 0,2, 1,2} },

	{	{0,0, 1,0, 2,0, 0,1},		// 5 = L, Violette
		{0,0, 0,1, 0,2, 1,2},
		{2,0, 0,1, 1,1, 2,1},
		{0,0, 1,0, 1,1, 1,2} },

	{	{0,0, 1,0, 2,0, 1,1},		// 6 = T, Green
		{0,0, 0,1, 1,1, 0,2},
		{1,0, 0,1, 1,1, 2,1},
		{1,0, 0,1, 1,1, 1,2} },
		
	{	{0,0, 1,0, 0,1, 1,1},		// 7 = O, Blue
		{0,0, 1,0, 0,1, 1,1},
		{0,0, 1,0, 0,1, 1,1},
		{0,0, 1,0, 0,1, 1,1} }
};

const int Piece::rgb[9][3] = 
{   { 0, 0, 0 },		// 0 = Black
	{ 255, 0, 0 },		// 1 = I, Red
	{ 255, 151, 0 },	// 2 = Z, Orange
	{ 0, 184, 151 },	// 3 = S, Cyan
	{ 222, 184, 0 },	// 4 = J, Yellow
	{ 222, 0, 222 },	// 5 = L, Purple
	{ 71, 184, 0 },		// 6 = T, Green
	{ 0, 71, 222},		// 7 = O, Blue
	{ 200, 200, 200 }	// 8 = Wall
};
