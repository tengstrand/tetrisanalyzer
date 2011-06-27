
#include <iostream>
#include <string.h>
#include <time.h>

#include "Constants.h"
#include "GameSettings.h"
#include "Board.h"
#include "BrainJote20.h"

using namespace std;

GameSettings::GameSettings()
{
	seed = 0;
	maxp = 0;
	maxl = 0;
	maxg = 0;
	level = 2;
	preview = 2;
	width = 10;
	height = 20;
	stat = -1;
	size = 8;
	sliding = 2;
	pause = 0;
	stepMode = STEP_MODE_OFF;
	performStep = PERFORM_STEP_WAIT;
	animate = ANIMATE_ON_TIME;
	thinking = THINKING_YES;
	viewHelp = 0;
	viewBoard = 1;
	viewBoardInfo = 1;
	viewMoveList = 0;
	viewDiffMove = 0;
	currentKey = 0;

	strcpy(logFilename, "c:\\tetris.log");
	strcpy(logTmpFilename, "tmptetris.log");
	strcpy(wgameFilename, "");
	strcpy(rgameFilename, "");
	strcpy(whtmlFilename, "");

	strcpy(whtmlFilename, "");
}

GameSettings::~GameSettings()
{
}


void GameSettings::setDefaultParameters()
{
	argc = 0;
	level = 2;
	preview = 2;
	seed = time (NULL);
//	seed = 1;
//	sliding = SLIDING_OFF;
	sliding = SLIDING_ON1;
	animate = ANIMATE_ON;
	delay = 40;
	stepMode = STEP_MODE_ON;
	performStep = PERFORM_STEP_WAIT;
	frameRateDelay = 10;

	viewBoard = 1;
	viewBoardInfo = 1;
	viewMoveList = 1;
	viewDiffMove = 0;

	width = 10;
	height = 20;
}


void GameSettings::writeSettings(ofstream &logfile)
{
  Board *board = new Board(this->getWidth(), this->getHeight(), this->getSliding(), this->getPieceStartX());

	int maxp = this->getMaxp();
	int maxl = this->getMaxl();
	int maxg = this->getMaxg();

	logfile << "\n\"tetris";

	for (int i=1; i<argc; i++)
		logfile << " " << argv[i];

	logfile << "\"\n\n";

	logfile << "width = " << this->getWidth() << "\n";

	logfile << "height = " << this->getHeight() << "\n";

	logfile << "seed = " << this->getSeed() << "\n";

	logfile << "level = " << this->getLevel() << "\n";

	logfile << "preview = " << this->getPreview() << "\n";

	logfile << "sliding = " << this->getSliding() << "\n";

	logfile << "logfile = " << this->getLogFilename() << "\n";

	logfile << "tmplogfile = " << this->getLogTmpFilename() << "\n";

	logfile << "wgame = " << this->getWgameFilename() << "\n";

	logfile << "rgame = " << this->getRgameFilename() << "\n";

	logfile << "whtml = " << this->getWhtmlFilename() << "\n";

	if (maxg > 0)
 		logfile << "no of games = " << maxg << "\n";
	
	if (maxl > 0)
 		logfile << "no of lines = " << maxl << "\n";

	if (maxp > 0)
 		logfile << "no of pieces = " << maxp << "\n";

	logfile << "\nsy1..sy20 = ";

	for (i=1; i<=20; i++)
		logfile << BrainJote20::spacey[i] << "  ";

	logfile << "\nsyw1..syw20 = ";

	for (i=1; i<=20; i++)
		logfile << BrainJote20::spaceyw[i] << "  ";

	logfile << "\ns1..s9 = ";

	for (i=1; i<=9; i++)
 		logfile << BrainJote20::space[i] << "  ";

	logfile << "\ng1..g9 = ";

	for (i=1; i<=9; i++)
 		logfile << BrainJote20::linegap[i] << "  ";

	logfile << "\ny0..y20 = ";

	for (i=0; i<=20; i++)
 		logfile << BrainJote20::yadd[i] << "  ";

	logfile << "\n\n";

	delete board;
}


char* GameSettings::getLogFilename()
{
	return logFilename;
}

void GameSettings::printValidArgs()
{
	cout << TEXT_INFO1 << "\nCreated by Joakim Tengstrand - 2004\n" <<
		      "e-mail: " << TEXT_INFO2 << "\n\n" <<
		      "Use the format:\n\ntetris [-logfile x] [-tmplogfile x]\n" <<
		      "[-wgame x] [-rgame x] [-whtml]\n" <<
		      "[-param x] [-from] [-to] [-step] [-fromi x] [-toi x]\n" <<
			    "[-maxg x] [-maxl x] [-maxp x]\n" <<
				  "[-width x] [-height x]\n" <<
				  "[-level x] [-preview x]\n" <<
					"[-sliding x] [-seed x]\n" <<
				  "[-g1..g9 x] [-s1..s9 x] [-t3..t9 x]\n" <<
				  "[-sy1..sy20 x] [-syw1..syw20 x]\n" <<
				  "[-size x]\n" <<
				  "\nUse the format: tetris -xxx to read the help " <<
				  "for the command xxx.\n";
}

void GameSettings::addDelay(int add)
{
	if (this->delay >= 500 && add > 0)
		delay += 100;
	else if (this->delay > 500 && add < 0)
		delay -= 100;
	else if (this->delay >= 30 && add > 0)
		delay += 10;
	else if (this->delay > 30 && add < 0)
		delay -= 10;
	else if (this->delay+add >= 0) this->delay += add;
}

void GameSettings::addFrameRateDelay(int add)
{
	if (this->frameRateDelay >= 1000 && add > 0)
		frameRateDelay += 1000;
	else if (this->frameRateDelay > 1000 && add < 0)
		frameRateDelay -= 1000;
	else if (this->frameRateDelay >= 200 && add > 0)
		frameRateDelay += 100;
	else if (this->frameRateDelay > 200 && add < 0)
		frameRateDelay -= 100;
	else if (this->frameRateDelay >= 30 && add > 0)
		frameRateDelay += 10;
	else if (this->frameRateDelay > 30 && add < 0)
		frameRateDelay -= 10;
	else if (this->frameRateDelay+add >= 0) this->frameRateDelay += add;
}

