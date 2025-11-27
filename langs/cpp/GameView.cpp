
#if defined(__APPLE__)
#include <OpenGL/gl.h>
#include <OpenGL/glu.h>
#else
#include <GL/gl.h>
#include <GL/glu.h>
#endif
#include "Constants.h"
#include "Game.h"
#include "GameView.h"
#include "GameSettings.h"
#include "GameInfo.h"
#include "Piece.h"
#include "RenderText.h"
#include "GameView.h"
#include "Move.h"
#include "DiffMoveThList.h"
#include "BrainJote20.h"
#include "Platform.h"

#define VIEW_X1 10
#define VIEW_Y1 10
#define WALL_WIDTH_LEFT 6
#define WALL_WIDTH_RIGHT 2
#define WALL_WIDTH_X (WALL_WIDTH_LEFT+WALL_WIDTH_RIGHT)
#define WALL_WIDTH_BOTTOM 2

#define POS_NUM 14
#define POS_VX 37
#define POS_LEVEL 87
#define POS_LEVEL_SPACE 55

#define POS_TH1 10
#define POS_TH2 40
#define POS_TH3 102
#define POS_TH_SPACE 90

#define POS_MOVE1 38
#define POS_MOVE2 110
#define POS_MOVE3 220
#define POS_MOVE4 350


#define FONT_HEIGHT 13

GameView::GameView(Game *game, int showInfo, int x1, int y1, int width, int height)
{
	this->game = game;
	this->showInfo = showInfo;
	this->viewX1 = x1 + VIEW_X1;
	this->viewY1 = y1 + VIEW_Y1;
	this->viewWidth = width;
	this->viewHeight = height;
	
	this->board = 0;
	this->gi = 0;
	this->moveList = 0;
	this->milisec = 0;
	this->frameCnt = 0;
	this->copyBoardIndex = 0;
	this->gameSettings = game->getGameSettings();
	this->frameRateDelay = gameSettings->getFrameRateDelay();

	boardCopy[0] = new Board(gameSettings->getWidth()+WALL_WIDTH_X, gameSettings->getHeight()+WALL_WIDTH_BOTTOM, gameSettings->getSliding(), gameSettings->getPieceStartX()+WALL_WIDTH_X);
	boardCopy[1] = new Board(gameSettings->getWidth()+WALL_WIDTH_X, gameSettings->getHeight()+WALL_WIDTH_BOTTOM, gameSettings->getSliding(), gameSettings->getPieceStartX()+WALL_WIDTH_X);
}

GameView::~GameView()
{
}


void GameView::changeSize(int w, int h)
{
	viewWidth = w ;
	viewHeight = h;

	glViewport(viewX1, viewY1, w, h);
    glClearColor(1.0, 1.0, 1.0, 1.0);		// Sets the background to white
    glColor3f(0, 0, 0);
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	gluOrtho2D(viewX1, viewX1+viewWidth, viewY1, viewY1+viewHeight);

	glScalef(1, -1, 1);
	glTranslatef(0, -viewHeight, 0);
	frameCnt = 0;
}



void GameView::render()
{
	double size;
	int width = 0;
	int height = 0;

	if (board == 0)
		return;

	int origoX = viewHelp();

	origoX = viewBoard(origoX, size);

	if (gameSettings->getViewBoardInfo())
		origoX = viewBoardInfo(origoX);

	if (gameSettings->getViewMoveList())
		origoX = viewMoveList(origoX);

	if (gameSettings->getViewDiffMove())
		viewDiffMove(origoX);

	int delay = gameSettings->getFrameRateDelay();
	if (delay > 0)
		platform::sleepMillis(delay);
}


/*
 * viewHelp
 */
int GameView::viewHelp()
{
	if (!gameSettings->getViewHelp())
		return viewX1;

	RenderText h(viewX1, 36, FONT_HEIGHT, 0.5, 0.0, 0.0);
	RenderText t(viewX1+80, 36, FONT_HEIGHT);

	h.println("[F1]");
	t.println("Toggle help.");
	h.println("[F2]");
	t.println("Toggle board.");
	h.println("[F3]");
	t.println("Toggle board info.");
	h.println("[F4]");
	t.println("Toggle move list.");
	h.println("[F5]");
	t.println("Toggle threshold list");

	h.lineFeed();
	t.lineFeed();

	h.println("Board:");
	h.println("=====================================================================");
	t.lineFeed();
	t.lineFeed();

	h.println("[P]");
	t.println("Toggle pause.");

	h.println("[D] / [d]");
	t.println("Change the speed of the pieces. 0=Max speed.");

	h.halfLineFeed();
	t.halfLineFeed();
	h.println("[N]");
	t.println("Toggle show next piece.");
	
	h.println("[1]..[9]");
	t.println("Set level.");
	t.println("If [N]ext=off, the search depth is Level-1.");
	t.println("If [Next=on, the search depth is Level-2.");
	t.println("A search depth of 0 will only take the known (visible) pieces in account.");
	t.println("A search depth of 1 will also take the next seven possible pieces in");
	t.println("account and then make the stastistically best move.");
	h.lineFeed();
	h.lineFeed();
	h.lineFeed();
	h.lineFeed();
	h.lineFeed();

	h.println("[L]");
	t.println("Toggle sliding. When set to off, the piece is moved to its");
	t.println("x-position and then dropped.");
	h.lineFeed();
	h.println("[S]");
	t.println("Toggle step mode.");
	t.println("When set to on, drop next piece by pressing the [Arrow-down] key.");
	h.lineFeed();

	h.println("[A]");
	t.println("Toggle Animate. Choose if the pieces should be animated or not.");
	h.println("[F] / [f]");
	t.println("Change how often the display will be updated.");
	
	if (gameSettings->isStepModeOn()) {
		h.println("[Arrow-down]");
		t.println("Move current piece (when in step mode).");
		h.lineFeed();
	};

	t.lineFeed();
	h.lineFeed();
	t.println(TEXT_INFO1);
	t.println(TEXT_INFO2);
	h.lineFeed();
	h.lineFeed();
	h.lineFeed();
	t.lineFeed();
	
	return 520;
}



/*
 * viewBoard
 */
int GameView::viewBoard(int origoX, double &size)
{
	size = 0;

	if (board == 0 || gi == 0 || !gameSettings->getViewBoard())
		return origoX;

	int maxCntblockstat = game->getMaxCntblockstat();

	// Render the board.
	int width = board->getWidth();
	int height = board->getHeight();
	int vwidth = viewWidth - VIEW_X1*2;
	int vheight = viewHeight - VIEW_X1*2;

	double bRatio = (double)width / (height == 0 ? 1 : height);
	double vRatio = (double)vwidth / (vheight == 0 ? 1 : vheight);

	if (bRatio < vRatio)
		size = (double)vheight / height;
	else
		size = (double)vwidth / width;

	double shade = 450-400/(2+size);

	int x1,y1,x2,y2;
	float r,g,b;
	
	for (int y = 0; y<height; y++)
	{
		for (int x=0; x<width; x++)
		{
			char p = board->get2(x, y);

			if (p > 0)
			{
				glBegin(GL_QUADS);

				r = Piece::getColorR(p) / 255.0;
				g = Piece::getColorG(p) / 255.0;
				b = Piece::getColorB(p) / 255.0;

				glColor3f(r, g, b);

				x1 = origoX + x*size;
				x2 = origoX + (x+1)*size;
				y1 = viewY1 + y*size;
				y2 = viewY1 + (y+1)*size;

				glVertex2f(x1,y2);
				glVertex2f(x2,y2);
				glVertex2f(x2,y1);
				glVertex2f(x1,y1);
				glEnd();
			}
		}
	}

	// Draw Grid
	for (int y = 0; y<height; y++)
	{
		for (int x=0; x<width; x++)
		{
			char p = board->get2(x, y);

			if (p == 0)
			{
				r = g = b = 255.0 / shade;
			}
			else
			{
				r = Piece::getColorR(p) / shade;
				g = Piece::getColorG(p) / shade;
				b = Piece::getColorB(p) / shade;
			}

			glBegin(GL_LINES);

			glColor3f(r, g, b);

			x1 = origoX + x*size;
			x2 = origoX + (x+1)*size;
			y1 = viewY1 + y*size;
			y2 = viewY1 + (y+1)*size;

			glVertex2f(x2,y1);
			glVertex2f(x2,y2);
			glVertex2f(x1,y2);
			glVertex2f(x2,y2);

			glEnd();
		}
	}

	glBegin(GL_LINES);

	glColor3f(r, g, b);

	x1 = origoX;
	x2 = x1 + width * size + 1;
	y1 = viewY1;
	y2 = y1 + height * size;

	glVertex2f(x1,y1);
	glVertex2f(x2,y1);
	glVertex2f(x1,y1);
	glVertex2f(x1,y2);

	glEnd();

	// The numbers in the bottom.
	if (gameSettings->getViewMoveList() && size >= 10)
	{
		int boardwidth = gameSettings->getWidth();
		const float cellTop = viewY1 + size * (height - WALL_WIDTH_BOTTOM);
		const float cellCenterY = cellTop + size * 0.5f;
		const int digitHeight = RenderText::measureHeight("0");
		for (int x=1; x<=boardwidth; x++)
		{
			char num[2];
			num[0] = static_cast<char>('0' + (x % 10));
			num[1] = '\0';

			float cellLeft = origoX + (WALL_WIDTH_LEFT + x - 1) * size;
			float cellCenterX = cellLeft + size * 0.5f;
			int textWidth = RenderText::measureWidth(num);
			// Center horizontally: cellCenterX - textWidth/2
			float textX = cellCenterX - textWidth * 0.5f;
			// Center vertically: cellCenterY - digitHeight/2
			// stb_easy_font uses top-left corner (y increases downward)
			// Add small offset to account for visual centering vs mathematical center
			float textY = cellCenterY - digitHeight * 0.5f + 1.0f;

			RenderText numbers(static_cast<int>(textX + 0.5f), static_cast<int>(textY + 0.5f), FONT_HEIGHT, 0.3f, 0.3f, 0.3f);
			numbers.println(num);
		}
	}

	return origoX + size*width + 20;
}


/*
 * viewBoardInfo
 */
int GameView::viewBoardInfo(int origoX)
{
	char buffer[100];
	RenderText txt(origoX, 22, FONT_HEIGHT);

	char onoff[2][4] = { "Off", "On" };
	GameSettings *gameSettings = game->getGameSettings();

	glColor3f(0, 0, 0);

	int preview = gameSettings->getPreview();
	sprintf(buffer, "[N]ext: %s", onoff[preview-1]);
	txt.println(buffer);

	txt.println("Level [1-9]: ", gameSettings->getLevel());

	char anim[3][8] = { "Off", "On", "On Time" };
	int sliding = gameSettings->getSliding();
	sprintf(buffer, "s[L]iding: %s", onoff[sliding]);
	txt.println(buffer);
	
	int stepMode = gameSettings->getStepMode();
	sprintf(buffer, "[S]tep: %s", onoff[stepMode]);
	txt.println(buffer);

	int animate = gameSettings->getAnimate();
	sprintf(buffer, "[A]nimate: %s", anim[animate]);
	txt.println(buffer);

	txt.println("[d/D]elay: ", gameSettings->getDelay());
	txt.println("[f/F]rame Delay: ", gameSettings->getFrameRateDelay());

	txt.lineFeed();

	sprintf(buffer, "Board: %i x %i", board->getWidth()-WALL_WIDTH_X, board->getHeight()-WALL_WIDTH_BOTTOM);
	txt.println(buffer);

	txt.println("Seed: ", gameSettings->getSeed());
	txt.lineFeed();

	txt.println("Games: ", gi->getGames());
	txt.println("Lines/game: ", gi->getLinesPerGame());
	txt.println("Min lines: ", gi->getMinLines());
	txt.println("Max lines: ", gi->getMaxLines());

	txt.lineFeed();
	txt.println("Pieces total: ", gi->getTotalPieces());
	txt.println("Lines total: ", gi->getTotalLines());
	txt.println("Slided (%): ", gi->getTotalSlidedPercent(), 0, 4);
	
	txt.lineFeed();

	if (frameCnt == 0)
		milisec = platform::getMilliseconds();

	double sec = (platform::getMilliseconds() - milisec)/1000.0;
	double framesPerSecond = (sec == 0) ? 0 : frameCnt / sec;
	frameCnt++;

	txt.println("Lines/sec: ", gi->getLinesPerSecond(), 0, 2);
	txt.println("Pieces/sec: ", gi->getPiecesPerSecond(),0, 2);

	sprintf(buffer, "Frames/sec: %.2f", framesPerSecond);
	txt.println(buffer);
	int d,h,m;

	double s;
	gi->getTime(d, h, m, s);

	if (d == 0)
		sprintf(buffer, "%ih %im %.1fs", h, m, s);
	else
		sprintf(buffer, "%id %ih %im %.1fs", d, h, m, s);

	txt.println(buffer);

	txt.lineFeed();
	txt.println("Pieces: ", gi->getPieces());
	txt.println("Lines: ", gi->getLines());

//	sprintf(buffer, "Key: %d", gameSettings->getCurrentKey());
//	txt.println(buffer);


	txt.lineFeed();
	txt.println("Press [F1] for help.");

	return origoX + 170;
}



/*
 * viewMoveList
 */
int GameView::viewMoveList(int origoX)
{
	if (moveList == 0)
		return origoX;

	char buffer[100];
	RenderText txtHeader(origoX, 20, FONT_HEIGHT, 0, 0, 1);
	RenderText txt(origoX, 36, FONT_HEIGHT);
	RenderText txtn(origoX, 36, FONT_HEIGHT, 0, 0, 1);

	this->moveList->sort();

	glColor3f(0.0, 0.0, 0.0);

	int level = gameSettings->getLevel();
	int preview = gameSettings->getPreview();

	txtHeader.printr("vx ", POS_VX);

	for (int i=level; i>=preview; i--) {
		sprintf(buffer, "Level %i", i);
		txtHeader.printr(buffer, POS_LEVEL + (level-i)*POS_LEVEL_SPACE);
	}

	Move *move = moveList->getFirstMove();

	int first[MAX_LEVELS+1];
	double bestEquity[MAX_LEVELS+1];

	glColor3f(0, 0, 0);

	if (move != 0)
	{
		for (int i=preview; i<=level; i++)
		{
			first[i] = 1;
			bestEquity[i] = moveList->getBestEquity(i);
			if (bestEquity[i] == MAX_EQUITY)
				bestEquity[i] = 0;
		}
	}

	int movecnt = 0;

	while (move != 0)
	{
		movecnt++;

		int v = move->getV();
		int x = move->getX() + 1; if (x>9) x=0;
		int y = move->getY();

		glColor3f(0.5, 0.5, 0.5);

		sprintf(buffer, "%i.", movecnt);
		txtn.printr(buffer, POS_NUM);
		txtn.lineFeed();

		glColor3f(0, 0, 0);

		if (move->isSlided())
			sprintf(buffer, "%i%i*", v, x);
		else
			sprintf(buffer, "%i%i ", v, x);
			
		txt.printr(buffer, POS_VX);

		for (int i=level; i>=preview; i--)
		{
			double equity = move->getEquity(i);

			if (equity == MAX_EQUITY)
				equity = 0;

			if (equity == 0)
				strcpy(buffer, " -");
			else
			{
				if (equity == bestEquity[i] && first[i])
				{
					first[i] = 0;
					sprintf(buffer, " %.3f", equity);
				}
				else
					sprintf(buffer, "+%.3f", equity-bestEquity[i]);
			}

			txt.printr(buffer, POS_LEVEL + (level-i)*POS_LEVEL_SPACE);
		}

		txt.lineFeed();
		move = moveList->getNextMove();
	}

	if (gameSettings->isThinking()) {
		char isthinking[] = "Thinking...";
		glColor3f(0.0, 0.0, 0.0);
		txt.println(isthinking);
	}

	return origoX + POS_LEVEL + (level-preview+0.6)*POS_LEVEL_SPACE;
}

/*
 * viewDiffMove
 */
void GameView::viewDiffMove(int origoX)
{
	if (moveList == 0 || diffMoveGroup == 0)
		return;

	char buffer[100];
	RenderText txth(origoX, 20, FONT_HEIGHT, 0, 0, 1);
	RenderText txt(origoX, 20, FONT_HEIGHT);

	double n = 0;
	diffMoveGroup->reset();

	DiffMoveThList diffList;
	
	diffList.add(0.01);
	diffList.add(0.05);
	diffList.add(0.001);
	diffList.add(0.0001);
	diffList.add(0.00001);
	diffList.add(0.0000000001);

	int c = 0;
	DiffMoveTh *diff = diffList.getFirst();

	while (diff) {
		sprintf(buffer, "%.5f", diff->thEquity);
		txth.printr(buffer, POS_TH3 + c++ * POS_TH_SPACE);

		diff = diffList.getNext();
	}

	txt.lineFeed();
	txth.lineFeed();

	int level = gameSettings->getLevel();
	int preview = gameSettings->getPreview();

	for (int level1=preview; level1<=level-1; level1++)
	{
		for (int level2=level1+1; level2<=level; level2++)
		{
			diffMoveGroup->getThreshold(&diffList, level1, level2, preview);	// level1, level2, preview

			c = 0;
			diff = diffList.getFirst();

			while (diff) {
				if (c==0) {
					sprintf(buffer, "%i:%i", level1, level2);
					txth.printr(buffer, POS_TH1 + c*POS_TH_SPACE);
				}

				sprintf(buffer, "%.1f", diff->threshold);
				txt.printr(buffer, POS_TH2 + c*POS_TH_SPACE);

				sprintf(buffer, "%.5f", diff->percent+0.00000000001);
				txt.printr(buffer, POS_TH3 + c*POS_TH_SPACE);
				diff = diffList.getNext();
				c++;
			}

			txt.lineFeed();
			txth.lineFeed();
		}
	}

	txt.lineFeed();
	txt.lineFeed();
	txth.lineFeed();

	txth.printr("Threshold", POS_MOVE2);
	txth.printr("Moves", POS_MOVE3);
	txth.printr("Possitions", POS_MOVE4);
	txth.lineFeed();

	for (int i=preview; i<=level; i++) {
		sprintf(buffer, "Level %i:", i);
		txth.printr(buffer, POS_MOVE1);

		txt.printr(BrainJote20::threshold[preview-1][i], POS_MOVE2, 1);
		txt.printr(diffMoveGroup->getCntMove(i, preview), POS_MOVE3);
		txt.printr(diffMoveGroup->getCntPos(i, preview), POS_MOVE4);

		txt.lineFeed();
		txth.lineFeed();
	}
}


void GameView::notify(Board *board, GameInfo *gameInfo, int p1, int p2, int showStartPiece)
{
	int preview = gameSettings->getPreview();
	int pieceStartX = gameSettings->getPieceStartX();

	this->diffMoveGroup = board->getDiffMoveGroup();

	Board *boardcopy = boardCopy[1-copyBoardIndex];

	boardcopy->copyWithWalls(board, preview-1);

	Piece copyPiece1(boardcopy, p1);
	Piece copyPiece2(boardcopy, p2);

	if (showStartPiece)
		copyPiece1.setPiece(0, 6+pieceStartX, 0, board->getWidth());

	if (preview == 2)
		copyPiece2.setPiece(0, 1, 0, board->getWidth());

	copyBoardIndex = 1-copyBoardIndex;

	this->board = boardcopy;

	this->gi = gameInfo;

	if (this->frameRateDelay != gameSettings->getFrameRateDelay()) {
		frameCnt = 0;
		this->frameRateDelay = gameSettings->getFrameRateDelay();
	}
}

void GameView::notify(MoveList *moveList)
{
	moveListData.copy(moveList);
	this->moveList = &moveListData;
}