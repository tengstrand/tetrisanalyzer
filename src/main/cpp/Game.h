
#ifndef __game__
#define __game__

#include "GameSettings.h"
#include "FileWriter.h"
#include "FileReader.h"
#include "Board.h"
#include "Thread.h"
#include "GameInfo.h"
#include "GameObserver.h"
#include "GameObserverList.h"

#define DATA_HEADER 1

#define MAX_OBSERVERS 100

using namespace std;

class Game : public IRunnable
{
	private:
		int gameObserverCnt;
		int games;
		long totalLines;
		long totalPieces;
		long totalLines2;
		long totalPieces2;
		long totalMoves;
		long totalSlided;
		long minLines;
		long maxLines;
		long cntLines;
		long cntPieces;
		long cntBlock;
		long maxCntblockstat;
		long *cntblockstat;
		long linesPerGame;
		time_t milisec1;
		time_t milisec2;
		time_t seconds;
		ofstream wHtml;
		ofstream wTmp;
		FileWriter wGameFile;
		FileReader rGameFile;
		GameSettings *gameSettings;
		Board *board;
		GameObserver *gameObservers[MAX_OBSERVERS];
		GameObserverList gameObserverList;

		void initTime();
		void notify(GameObserver *gameObserver, GameInfo *gameInfo, int preview, int p1, int p2, int piecesStartX, int showStartPiece);

	public:
		Game(GameSettings *gameSettings);
		~Game();
		Board *getBoardCopy();
		int openLogFile(char *logFilename);
		void reset();
		int getMaxCntblockstat() { return maxCntblockstat; }
		double getCntBlockArea(int idx1, int idx2);
		void play();
		void readGame(int maxp, int size);
		void writeTmpFile(Board *board);
		void run();
		Board *getInfo(GameInfo &gameInfo);
		GameSettings *getGameSettings() { return gameSettings; }
		void attach(GameObserver *gameObserver);
		void detach(GameObserver *gameObserver);
};

#endif
