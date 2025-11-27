
#include <iostream>
#include <time.h>
#include <math.h>

#include <fstream>
#include <string.h>

#include "Constants.h"
#include "Game.h"
#include "GameSettings.h"
#include "Board.h"
#include "Piece.h"
#include "Player.h"
#include "Random.h"
#include "Move.h"
#include "MoveList.h"
#include "MovePath.h"
#include "GameInfo.h"
#include "Platform.h"

//-height 10 -logfile d:\tetris\debug\game.txt -wgame d:\tetris\debug\game.bin -maxp 5 -printb 1 -level 3

//-logfile d:\tetris\debug\game2.txt -rgame d:\tetris\debug\game.bin

Game::Game(GameSettings *gameSettings)
{
	this->gameSettings = gameSettings;

	this->maxCntblockstat = gameSettings->getMaxcntblock();
	cntblockstat = new long[maxCntblockstat];

	gameObserverCnt = 0;

	reset();
}

Game::~Game()
{
	delete [] cntblockstat;
}


void Game::run()
{
	play();

	return;
}

void Game::reset()
{
	this->games = 0;
	this->totalLines = 0;
	this->totalPieces = 0;
	this->totalLines2 = 0;
	this->totalPieces2 = 0;
	this->totalMoves = 0;
	this->totalSlided = 0;
	this->minLines = -1;

	this->maxLines = 0;
	this->cntLines = 0;
	this->cntPieces = 0;
	this->cntBlock = 0;
	this->linesPerGame = 0;

	for (int i=0; i<maxCntblockstat; i++)
		cntblockstat[i] = 0;

	board = 0;
}


double Game::getCntBlockArea(int idx1, int idx2)
{
	double sum = 0;

	int y1, y2;
	
	y1 = cntblockstat[idx1];

	for (int i=idx1+1; i<=idx2; i++)
	{
		y2 = cntblockstat[i];

		if (y1 < y2)
			sum += y1 + (y2-y1)/2.0;
		else
			sum += y2 + (y1-y2)/2.0;

		y1 = y2;
	}

	return sum;
}


void Game::play()
{
	long maxp = gameSettings->getMaxp();
	long maxl = gameSettings->getMaxl();
	long maxg = gameSettings->getMaxg();
	int level = gameSettings->getLevel();
	int preview = gameSettings->getPreview();
	int stat = gameSettings->getStat();
	int size = gameSettings->getSize();
	int sliding = gameSettings->getSliding();
	int delay = gameSettings->getDelay();
	int animate = gameSettings->getAnimate();
	int exit = gameSettings->getExit();
	int pieceStartX = gameSettings->getPieceStartX();
	int frameRateDelay = gameSettings->getFrameRateDelay();
	int nextPiece = 0;
	int viewBoard = gameSettings->getViewBoard();
	int stepMode = gameSettings->getStepMode();
	milisec1 = platform::getMilliseconds();
	milisec2 = platform::getMilliseconds();
	seconds = time(NULL);

	if (strlen(gameSettings->getWgameFilename()) != 0)
	{
		if (wGameFile.open(gameSettings->getWgameFilename()))
		{
			cout << "Could not open the file  \"" << gameSettings->getWgameFilename() << "\"" << endl;

			return;
		}
	}

	if (strlen(gameSettings->getRgameFilename()) != 0)
	{
		if (rGameFile.open(gameSettings->getRgameFilename()))
		{
			cout << "Could not open the file  \"" << gameSettings->getRgameFilename() << "\"" << endl;

			return;
		}
	}

	if (strlen(gameSettings->getWhtmlFilename()) != 0)
	{
		wHtml.open(gameSettings->getWhtmlFilename());

		if (!wHtml.good())
		{
			cout << "Could not open the file  \"" << gameSettings->getWhtmlFilename() << "\"" << endl;

			return;
		}
	}

  board = new Board(gameSettings->getWidth(), gameSettings->getHeight(), sliding, pieceStartX);

	Piece setStaticInitializersPiece(board, 1, sliding, 1);

	Player *player = new Player(board);

	Random random = Random(gameSettings->getSeed());
	GameInfo gameInfo(maxCntblockstat);

  writeTmpFile(board);

	double equity = player->evaluate();
	double maxDiff = 0;

	int pieces[10];

	if (preview > 1)
		pieces[0] = random.getRandomP();

	pieces[1] = 0;
	pieces[2] = 0;
	pieces[3] = 0;
	pieces[4] = 0;
	pieces[5] = 0;
	pieces[6] = 0;
	pieces[7] = 0;
	pieces[8] = 0;
	pieces[9] = 0;

	if (wGameFile.isOpen())
	{
		// 1. Version of the file
		wGameFile.writeShort(1);													// # Version (short)
		
		// 2. Header
		wGameFile.writeShort(DATA_HEADER);								// # Type of block
		wGameFile.writeShort(1);													// # Version
		wGameFile.writeShort(gameSettings->getWidth());		// # Width (short)
		wGameFile.writeShort(gameSettings->getHeight());	// # Height (short)
		wGameFile.writeInt(gameSettings->getSeed());			// # Seed (int)
		wGameFile.writeByte(gameSettings->getLevel());		// # Level (byte)
		wGameFile.writeByte(gameSettings->getPreview());	// # Preview (byte)
		wGameFile.writeByte(pieces[0]);      							// # Piece (byte)
	}

	while ((maxg==0 && maxl==0 && maxp==0) || 
		     ((maxg > 0 && games < maxg) ||
		      (maxl > 0 && totalLines < maxl) ||
				  (maxp > 0 && totalPieces < maxp)))
	{
		if (exit == 1 || gameSettings->getExit() == 1)
			break;

		//-------------------------------------------
		if (stepMode != gameSettings->getStepMode()) {
			stepMode = gameSettings->getStepMode();
			initTime();
		}
		
		if (level != gameSettings->getLevel()) {
			level = gameSettings->getLevel();
			initTime();
		}

		if (sliding != gameSettings->getSliding())
		{
			sliding = gameSettings->getSliding();
			board->setSliding(sliding);
			initTime();
		}

		if (preview != gameSettings->getPreview())
		{
			preview = gameSettings->getPreview();

			if (preview == 1)
				pieces[1] = 0;
			else
			{
				if (nextPiece == 0)
					nextPiece = pieces[1] = random.getRandomP();
				else
					pieces[1] = nextPiece;
			}

			initTime();
		}

		if (animate != gameSettings->getAnimate()) {
			animate = gameSettings->getAnimate();
			initTime();
		}

		if (delay != gameSettings->getDelay()) {
			delay = gameSettings->getDelay();
			initTime();
		}

		if (frameRateDelay != gameSettings->getFrameRateDelay()) {
			frameRateDelay = gameSettings->getFrameRateDelay();
			initTime();
		}

		if (viewBoard != gameSettings->getViewBoard()) {
			viewBoard = gameSettings->getViewBoard();
			initTime();
		}

		while (gameSettings->isPause())
			platform::sleepMillis(10);

		if (board->getWidth() != gameSettings->getWidth())
		{
			delete board;
			pieceStartX = gameSettings->getPieceStartX();
			board = new Board(gameSettings->getWidth(), gameSettings->getHeight(), sliding, pieceStartX);
			Piece setStaticInitializersPiece(board, 1, sliding, 1);			
		}
		//-------------------------------------------

		if (preview > 1)
			nextPiece = pieces[1] = random.getRandomP();
		else
			pieces[0] = random.getRandomP();

		if (wGameFile.isOpen())
			wGameFile.writeByte(pieces[1]);									// # Piece

		Piece bestPiece(board, pieces[0]);


		gameObserverList.notify(board, 
														&gameInfo, 
														pieces[0], 
														pieces[1], 
														1, 
														milisec1,
														milisec2,
														cntblockstat, 
														games,
														cntPieces,
														cntLines,
														totalMoves,
														totalSlided,
														totalLines,
														totalLines2,
														totalPieces,
														totalPieces2,
														minLines,
														maxLines,
														linesPerGame);


		if (!animate)
			platform::sleepMillis(static_cast<unsigned long>(delay * 10));

		int playAgain;
		Move *bestMove;
		MoveList *moveList;

		do
		{
			playAgain = 0;
			gameSettings->setIsThinking();

			if (animate != gameSettings->getAnimate()) {
				animate = gameSettings->getAnimate();
				initTime();
			}

			bestMove = player->play(pieces, level, preview, animate, &gameObserverList);

			if (bestMove != nullptr)
				bestPiece.set(bestMove->getV(), bestMove->getX(), bestMove->getY(), bestMove->getClearedLines());

			gameSettings->setIsNotThinking();

			moveList = board->getMoveList(level);

			gameObserverList.notify(moveList);

			while (gameSettings->isStepModeOn() && gameSettings->isStepWait())
			{
				if (level != gameSettings->getLevel()) {
					level = gameSettings->getLevel();
					initTime();
					playAgain = 1;
					break;
				}

				if (preview != gameSettings->getPreview())
				{
					preview = gameSettings->getPreview();

					if (preview == 1)
						pieces[1] = 0;
					else
					{
						if (nextPiece == 0)
							nextPiece = pieces[1] = random.getRandomP();
						else
							pieces[1] = nextPiece;
					}

					initTime();
					playAgain = 1;

					gameObserverList.notify(board, 
																	&gameInfo, 
																	pieces[0], 
																	pieces[1], 
																	1, 
																	milisec1,
																	milisec2,
																	cntblockstat, 
																	games,
																	cntPieces,
																	cntLines,
																	totalMoves,
																	totalSlided,
																	totalLines,
																	totalLines2,
																	totalPieces,
																	totalPieces2,
																	minLines,
																	maxLines,
																	linesPerGame);



					break;
				}

				if (sliding != gameSettings->getSliding() && bestMove != nullptr)
				{
					sliding = gameSettings->getSliding();
					board->setSliding(sliding);

					initTime();
					playAgain = 1;
					break;
				}

				platform::sleepMillis(10);
			}
		} while (playAgain);

		if (gameSettings->isStepModeOn())
			gameSettings->setStepWait();

		if (!animate && bestMove != nullptr)
		{
			bestPiece.setPiece();

			if (bestPiece.getClearedLines() > 0)
				board->clearLines(bestPiece.getY(), bestPiece.getHeight());
		}

		Move *move;
		int index = 0;

		int length = moveList->getLength();

		totalMoves += length;

		if (animate && bestMove != nullptr)
		{
			// Animate the moves.
			MovePath *movePath = board->getMovePath(level);
			MovePathStep *s = movePath->getPath(bestMove->getV(), bestMove->getX(), bestMove->getY());

			int skipFirst = 0;

			int prevy = -1;

			while (s != nullptr)
			{
				//----
				// Calculate the number of steps for the current line (y value).
				MovePathStep *s1 = s;
				int y1 = s1->y;
				int cnt;

				if (prevy != y1)
				{
					cnt = 0;

					while (s1 != nullptr && s1->y == y1) {
						cnt++;
						s1 = s1->next;
					}
					prevy = y1;
				}
				//----
				
				if (animate != gameSettings->getAnimate())
					break;

				if (delay != gameSettings->getDelay())
					delay = gameSettings->getDelay();

				int v = s->v;
				int x = s->x;
				int y = s->y;

				Piece piece(board, bestPiece.getP());
				piece.setPiece(v, x, y);

				if (skipFirst)
					gameObserverList.notify(board, 
																	&gameInfo, 
																	pieces[0], 
																	pieces[1], 
																	0, 
																	milisec1,
																	milisec2,
																	cntblockstat, 
																	games,
																	cntPieces,
																	cntLines,
																	totalMoves,
																	totalSlided,
																	totalLines,
																	totalLines2,
																	totalPieces,
																	totalPieces2,
																	minLines,
																	maxLines,
																	linesPerGame);

				if (gameSettings->isSlidingOn())
					platform::sleepMillis(static_cast<unsigned long>(delay / cnt));
				else
					platform::sleepMillis(static_cast<unsigned long>(delay));

				piece.clearPiece();
				skipFirst = 1;
				s = s->next;

				while (gameSettings->isPause())
					platform::sleepMillis(10);
			}

			bestPiece.setPiece();

			if (bestPiece.getClearedLines() > 0)
			{
				int clearedLinesBits = board->getClearedLines(bestPiece.getY(), bestPiece.getHeight());

				for (int n=0; n<4; n++)
				{
					board->flashLines(bestPiece.getY(), bestPiece.getHeight(), clearedLinesBits);
					
					if (n<3)
					{
						gameObserverList.notify(board, 
																		&gameInfo, 
																		pieces[0], 
																		pieces[1], 
																		0, 
																		milisec1,
																		milisec2,
																		cntblockstat, 
																		games,
																		cntPieces,
																		cntLines,
																		totalMoves,
																		totalSlided,
																		totalLines,
																		totalLines2,
																		totalPieces,
																		totalPieces2,
																		minLines,
																		maxLines,
																		linesPerGame);

						if (delay > 0)
						{
							if (delay >= 50)
								platform::sleepMillis(150);
							else
								platform::sleepMillis(static_cast<unsigned long>(150-(50-delay)*2));
						}
					}
				}

				board->clearLines(bestPiece.getY(), bestPiece.getHeight());
			}
		}

		cntPieces++;
		totalPieces++;
		
		if (bestMove != nullptr)
			totalSlided += bestMove->isSlided();

		// Log the moves
		if (wGameFile.isOpen())
		{
			wGameFile.writeShort(length);											// # Number of legal moves.
			wGameFile.writeByte(bestPiece.getV());						// # V
			wGameFile.writeShort(bestPiece.getX());						// # X
			wGameFile.writeShort(bestPiece.getY());						// # Y

			move = moveList->getFirstMove();

			while (move != 0)
			{																									// Move:
				wGameFile.writeByte(move->getV());							// # V
				wGameFile.writeShort(move->getX());							// # X
				wGameFile.writeShort(move->getY());							// # Y

				for (int i=preview; i<=level; i++)							// Equity (e.g):
					wGameFile.writeFloat(move->getEquity(i));			// L2, L3, L4

				move = moveList->getNextMove();
			}
		}

		if (preview > 1)
			pieces[0] = pieces[1];

  	int clearedLines = bestPiece.getClearedLines();

  	if (bestMove == 0) 
		{
  		games++;
  		
			linesPerGame = totalLines/games;
		}
		else
		{
			cntLines += clearedLines;
			totalLines += clearedLines;

			cntBlock += 4 - clearedLines * board->getWidth();

			if (DEBUG == 0)
			{
				int xxx = cntBlock/4;
				if (xxx < maxCntblockstat)
					cntblockstat[xxx]++;
			}
		}

  	if (cntLines > maxLines)
  		maxLines = cntLines;

		if (bestMove == 0)
		{
  		if (cntLines < minLines || minLines==-1)
  			minLines = cntLines;
			
			cntBlock = 0;
			cntPieces = 0;
  		cntLines = 0;
		  board->clear();
			pieces[0] = random.getRandomP();
		}
	}

	delete board;
	delete player;

	if (wGameFile.isOpen())
	{
		wGameFile.writeByte(END_PIECE);				// # Piece
		wGameFile.close();
	}

	if (rGameFile.isOpen() && strlen(gameSettings->getWhtmlFilename()) > 0)
	{
		readGame(maxp, size);
		rGameFile.close();
	}

	gameSettings->setExit(2);
}
  

void Game::notify(GameObserver *gameObserver, GameInfo *gameInfo, int preview, int p1, int p2, int pieceStartX, int showStartPiece)
{
	double sec = (platform::getMilliseconds() - milisec2)/1000.0;

	double s = (platform::getMilliseconds() - milisec1)/1000.0;
	int d = s / (3600*24);
	s -= d * 3600*24;
	int h = s / 3600;
	s -= h*3600;
	int m = s / 60;
	s -= m*60;

	gameInfo->set(cntblockstat,
			 				  games,
								cntPieces,
								cntLines,
								totalPieces,
								totalLines,
								totalMoves,
								totalSlided,
								(sec == 0) ? 0 : (totalLines-totalLines2) / sec,
								(sec == 0) ? 0 : (totalPieces-totalPieces2) / sec,
								d, h, m, s,
								minLines,
								maxLines,
								linesPerGame);

	gameObserver->notify(board, gameInfo, p1, p2, showStartPiece);
}


// Write to log file once per second
void Game::writeTmpFile(Board *board)
{
	time_t seconds2 = time(NULL);

	if (seconds2 == seconds)
		return;
	
	if (strlen(gameSettings->getLogTmpFilename()) == 0)
		return;

	seconds = seconds2;

	wTmp.open(gameSettings->getLogTmpFilename());

	if (!wTmp.good())
	{
		cout << "Could not open the file  \"" << gameSettings->getLogTmpFilename() << "\"" << endl;

		return;
	}

	time_t sec = time(NULL) - milisec1;
	if (sec == 0)
		sec = 1;

	wTmp << "games=" << games << "\n";
	wTmp << "L/G=" << linesPerGame << "\n";
	wTmp << "L/S=" << (totalLines / sec) << "\n";
	wTmp << "P/S=" << (totalPieces / sec) << "\n";
	wTmp << "lines=" << cntLines << " (total=" << totalLines << ")\n";
	wTmp << "pieces=" << cntPieces << " (total=" << totalPieces << ")\n";
	wTmp << "\n";

	wTmp << "### 0-" << maxCntblockstat-1 << " ###\n\n";

	for (int i=0; i<maxCntblockstat; i++)
		wTmp << cntblockstat[i] << "\n";

	wTmp << "\n";

	board->write(wTmp);

	wTmp.close();
}

void Game::readGame(int maxp, int size)
{
	int cntmove = 1;
	int cntPiece = 0;

	// 1. Version of the file
	short version = rGameFile.readShort();	// # Version (short)

	// 2. Header
	short typeOfData = rGameFile.readShort();	// # DATA_HEADER
	rGameFile.readShort();										// # Header version

	short width = rGameFile.readShort();		// # Width (short)
	short height = rGameFile.readShort();		// # Height (short)
	int seed = rGameFile.readInt();					// # Seed (int)
	int level = rGameFile.readByte();				// # Level (byte)
	int preview = rGameFile.readByte();			// # Preview (byte)
	int piece1 = rGameFile.readByte();      // # Piece1 (byte)
	int piece2 = rGameFile.readByte();      // # Piece2 (byte)

	Board board(width, height, 0, 0);
	Board htmlBoard(width+8, height+5, 0, 0);
	Piece dummy(&board, piece1);					// Initialize static members for width x height.
	MoveList *moveList = board.getMoveList(level);

	htmlBoard.copyWithWalls(&board, preview-1);

	wHtml << "<html>\n"
		    << "<head>\n"
				<< "  <title>Test.</title>\n"
				<< "</head>\n"
				<< "<body>\n"
				<< "  <table border=1>\n";

	while (piece2 != END_PIECE)
	{
		Piece htmlPiece1(&htmlBoard, piece1);
		Piece htmlPiece2(&htmlBoard, piece2);
		htmlPiece1.setPiece(0, 10, 1, width);

		if (level > 1)
			htmlPiece2.setPiece(0, 1, 1, width);
		
		Piece piece(&board, piece1);

		int length = rGameFile.readShort();		// # Number of legal moves.
		int pieceV = rGameFile.readByte();		// # V
		int pieceX = rGameFile.readShort();		// # X
		int pieceY = rGameFile.readShort();		// # Y

		moveList->init(level, preview, board.getMaxEquity());

		if (length == 0)
			break;


		wHtml << "    <tr>\n"
					<< "      <td>\n"
					<< "        <table border=0 cellspacing=0 cellpadding=0>\n"
					<< "        <!-- Board -->\n";

		// 1. Board
		for (int y=0; y<height+5; y++)
		{
			wHtml	<< "          <tr><td>";

			for (int x=0; x<width+8; x++)
			{
				int p = htmlBoard.get(x, y);
				char c = Piece::getHtmlChar(p);

				if (width == 10 && y==height+3)
				{
					if (x==6)
						wHtml << "<IMG src=img/Wdigits.gif height=" << size << ">";
					else if (x >=7 && x<=width+6)
						continue;
				}

				wHtml << "<IMG src=img/" << c << ".gif height=" << size << " width=" << size << ">";
			}

			wHtml	<< "</td></tr>\n";
		}

		wHtml	<< "        </table>\n"
					<< "      </td>\n";


		// 2. Equity list header
		wHtml	<< "      <td>\n"
					<< "        <table border=0>\n"
					<< "        <!-- Equity list -->\n"
					<< "          <tr>\n"
					<< "            <td colspan=2 align=left><font face=Arial size=1>"
					<< cntmove << ".&nbsp;"
					<< Piece::getChar(piece1) << Piece::getChar(piece2)
					<< "</font></td>\n"
					<< "          </tr>\n"
					<< "          <tr>\n"
					<< "            <td align=right><font face=Arial size=1>&nbsp;</font></td>\n"
					<< "            <td align=right><font face=Arial size=1>vx</font></td>\n";

		for (int i=level; i>=preview; i--)
			wHtml << "            <td align=right><font face=Arial size=1>&nbsp;&nbsp;Level&nbsp;"
						<< i << "</font></td>\n";
					
		wHtml << "          </tr>\n";

		while (length-- > 0)
		{
			int v = rGameFile.readByte();				// # V
			int x = rGameFile.readShort();			// # X
			int y = rGameFile.readShort();			// # Y

			Move *move = moveList->getMove();
			moveList->add(v, x, y);

			for (int i=preview; i<=level; i++)	// Equity (e.g):
			{
				double equity = rGameFile.readFloat();		// L2, L3, L4

				move->setEquity(i, equity);
			}
		}

		moveList->sort();

		int movecnt = 0;
		Move *move = moveList->getFirstMove();

		int first[MAX_LEVELS+1];
		float bestEquity[MAX_LEVELS+1];

		if (move != 0)
		{
			for (int i=preview; i<=level; i++)
			{
				first[i] = 1;
				bestEquity[i] = (float)moveList->getBestEquity(i);
			}
		}

		// 2. Equity list list
		while (move != 0)
		{
			movecnt++;

			if (movecnt > height/2)
				break;

			int v = move->getV();
			int x = move->getX();
			int y = move->getY();

			wHtml << "          <tr>\n"
				    << "            <td align=right><font face=Arial size=1>"
						<< movecnt << ".</font></td>\n"
						<< "            <td align=right><font face=Arial size=1>"
						<< v << x << "</font></td>\n";

			for (int i=level; i>=preview; i--)	// Equity (e.g):
			{
				char equityBuf[20];
				float equity = move->getEquity(i);

				if (equity == 0)
					strcpy(equityBuf, "-");
				else
				{
					if (equity == bestEquity[i] && first[i])
					{
						first[i] = 0;
						sprintf(equityBuf, "%.3f", equity);
					}
					else
						sprintf(equityBuf, "+%.3f", equity-bestEquity[i]);
				}

				wHtml << "            <td align=right><font face=Arial size=1>" << equityBuf << "</font></td>\n";
			}

			wHtml << "          </tr>\n";

			move = moveList->getNextMove();
		}

		wHtml	<< "        </table>\n"
		    	<< "      </td>\n"
		    	<< "    </tr>\n";

		piece.setPiece(pieceV, pieceX, pieceY);
		
		board.clearLines(pieceY, piece.getHeight());
		htmlBoard.copyWithWalls(&board, preview-1);

		piece1 = piece2;
		piece2 = rGameFile.readByte();				// # Piece (byte)
		cntmove++;

		if (maxp > 0 && cntmove >= maxp)
			break;
	}

	wHtml	<< "  </table>\n"
				<< "</body>\n"
				<< "</html>\n";
}

void Game::attach(GameObserver *gameObserver)
{
	if (gameObserverCnt >= MAX_OBSERVERS-1)
		return;

	gameObserverList.attach(gameObserver);

	gameObservers[gameObserverCnt++] = gameObserver;
}

void Game::detach(GameObserver *gameObserver)
{
	if (gameObserverCnt <= 0)
		return;

	gameObserverList.detach(gameObserver);

	gameObserverCnt--;
}

void Game::initTime()
{
	totalLines2 = totalLines;
	totalPieces2 = totalPieces;
	milisec2 = platform::getMilliseconds();
}