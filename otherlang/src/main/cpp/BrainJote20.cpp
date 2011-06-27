#include "Piece.h"
#include "BrainJote20.h"

BrainJote20::BrainJote20(Board *board) : Brain(board) {
  this->board = board;

  int width = board->getHeight();
  int height = board->getWidth();

  this->lineVal = new double[width];

  this->maxEquity = width*(height-1) + yadd[0]*width;
  board->setMaxEquity(maxEquity);
}

BrainJote20::~BrainJote20() {
  delete [] lineVal;
}

double BrainJote20::evaluate() {
  globalCnt++;

  int x;
  int cnt;
  int minGap;
  double sc;
  double equity = 0;

  int width = board->getWidth();
  int height = board->getHeight();

  // 1. Calculate based on hollows

  int minOutline = board->initOutline();
  int *outline = board->getOutline();

  for (int y=minOutline; y<height; y++) {
    cnt = 0;
    minGap = height;

    for (x=0; x<width; x++) {
      if (board->get(x, y) != 0)
        cnt++;
      else if (outline[x] < minGap && outline[x] < y)
        minGap = outline[x];
    }

    lineVal[y] = linegap[cnt];

    if (minGap < height) {
      sc = 1;

      for (int i=minGap; i<=y; i++)
        sc *= lineVal[i];

      equity += (1 - sc) * width;
    }
  }

  // 2. Calculate based on outline

  for (x=0; x<width; x++)
    equity += yadd[outline[x]];

  outline[width] = 0;

  for (x=1; x<=width; x++) {
    int xx;
	int even = -1;
	int spaceheight = 0;
    int prevcntspace = 0;

	int ymin = outline[x];
	int starty = (x == width) ? minOutline : ymin;

	for (int y=starty; y<=height; y++) {
      int cntspace = 0;

      // Calculate the size of the gap (cntspc).
      for (xx=x-1; xx>=0; xx--) {
        if (outline[xx] <= y) {
          if (even == -1)
			even = (outline[xx] == ymin);

          break;
		}
        cntspace++;
      }

      if (cntspace > 0 && (cntspace == prevcntspace || prevcntspace == 0))
        spaceheight++;
      else {
        if (even)
          equity += space[prevcntspace] * spacey[spaceheight];
        else
          equity += space[prevcntspace] * spaceyw[spaceheight];

        spaceheight = 1;
        even = -1;
      }
      prevcntspace = cntspace;
    }
  }

  cnt = 0;

  for (int p=0; p<=6; p++) {
    if (Piece::testStartPos(board, p) == -1)
      cnt++;
  }

  if (cnt == 0)
    return equity;

  if (cnt == 7)
    return maxEquity;

  return ((7-cnt) * equity + cnt * maxEquity) / 7;
}

double BrainJote20::yadd[21] = {
	7,
	7,
	2.5,
	2.2,
	1.8,
	1.3,
	1.0,
	0.9,
	0.7,
	0.6,
	0.5,
	0.4,
	0.3,
	0.25,
	0.2,
	0.15,
	0.1,
	0.1,
	0.1,
	0.1,
	0.1 };

double BrainJote20::linegap[10] = {
	0,
    0,
    0,
    0.1,
    0.2,
    0.3,
    0.4,
    0.5,
    0.6,
    0.553
};

double BrainJote20::space[11] = {
	0,
	4.25,
	2.39,
	3.1,
	2.21,
	2.05,
	1.87,
	1.52,
	1.34,
	1.18,
	0
};

double BrainJote20::spacey[21] = {
	0,
	0.42,
	1.05,
	2.2,
	3.1,
	4.6,
	5.6,
	6.6,
	7.6,
	8.6,
	9.6,
	10.6,
	11.6,
	12.6,
	13.6,
	14.6,
	15.6,
	16.6,
	17.6,
	18.6,
	19.6
};


double BrainJote20::spaceyw[21] =
{
	0.0,
	0.5,
	1.19,
	2.3,
	3.1,
	4.6,
	5.6,
	6.6,
	7.6,
	8.6,
	9.6,
	10.6,
	11.6,
	12.6,
	13.6,
	14.6,
	15.6,
	16.6,
	17.6,
	18.6,
	19.6
};

double BrainJote20::threshold[MAX_PREVIEW][MAX_LEVELS+1] =
{
	{ 0, 5.6, 2.6, 1.4, 0.8, 0.4, 0.2, 0.1, 0.05, 0 },
	{ 0, 0, 4.4, 2.1, 0.8, 0.4, 0.2, 0.1, 0.05, 0 }
};
