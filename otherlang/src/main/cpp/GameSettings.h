
#ifndef __gamesettings__
#define __gamesettings__

#include <fstream>

#include "Constants.h"

class GameSettings
{
	private:
		int argc;
		char **argv;
		int seed;
		int maxp;
		int maxl;
		int maxg;
		int level;
		int preview;
		int width;
		int height;
		int stat;
		int size;
		int sliding;
		int animate;
		int delay;
		int frameRateDelay;
		int exit;
		int pause;
		int stepMode;
		int performStep;
		int thinking;
		int currentKey;
		int viewHelp;
		int viewBoard;
		int viewBoardInfo;
		int viewMoveList;
		int viewDiffMove;

		char logFilename[MAX_FILENAME];
        char logTmpFilename[MAX_FILENAME];
		char wgameFilename[MAX_FILENAME];
		char rgameFilename[MAX_FILENAME];
		char whtmlFilename[MAX_FILENAME];

	public:
		GameSettings();
		~GameSettings();
		char *getLogFilename();
		char *getLogTmpFilename() { return logTmpFilename; }
		char *getWgameFilename() { return wgameFilename; }
		char *getRgameFilename() { return rgameFilename; }
		char *getWhtmlFilename() { return whtmlFilename; }
		void changeWidth(int dx) { width += dx; if (width<4) width=4; };
		void changeHeight(int dy) { height += dy; if (height<4) height=4; };
		int getSeed() { return seed; }
		void setSeed(int seed) { this->seed = seed; }
		int getMaxp() { return maxp; }
		void setMaxp(int maxp) { this->maxp = maxp; }
		int getMaxl() { return maxl; }
		void setMaxl(int maxl) { this->maxl = maxl; }
		int getMaxg() { return maxg; }
		void setMaxgp(int maxg) { this->maxg = maxg; }
		int getLevel() { return level; }
		void setLevel(int level) { this->level = level; if (level < preview) preview=level; }
		int getPreview() { return preview; }
		void setPreview(int preview) { this->preview = preview; if (level<preview) level=preview; }
		void togglePreview() { this->preview = 3-preview; if (level<preview) level=preview; }
		int getWidth() { return width; }
		void addWidth(int dx) { if (width+dx>=4) width += dx; }
		int getHeight() { return height; }
		void setHeight(int height) { this->height = height; }
		int getStat() { return stat; }
		void setStat(int stat) { this->stat = stat; }
		int getSize() { return size; }
		int getSliding() { return sliding; }
		int isSlidingOn() { return sliding != SLIDING_OFF; }
		int getDelay() { return delay; }
		void setDelay(int delay) { this->delay = delay; }
		void addDelay(int add);
		int getFrameRateDelay() { return frameRateDelay; }
		void setFrameRateDelay(int frameRateDelay) { this->frameRateDelay = frameRateDelay; }
		void addFrameRateDelay(int add);
		int getAnimate() { return animate; }
		void setAnimate(int animate) { this->animate = animate; }
		void nextAnimate() { this->animate = (this->animate == 2 ? 0 : this->animate+1); }
		void prevAnimate() { this->animate = (this->animate == 0 ? 2 : this->animate-1); }
		void toggleAnimate() { animate = (animate ^= 1) & 1; }
		int getExit() { return exit; }
		void setExit(int exit) { this->exit = exit; }
		void setSliding(int sliding) { this->sliding = sliding; }
		void setNextSliding() { sliding = (sliding+1)%2; }  // OBS �ndra ev. till 3 senare.
		int getMaxcntblock() { return width*height/4; }
		int getPieceStartX() { if (width<=4) return 0; else return (width-2)/2; }
		void setCurrentKey(int currentKey) { this->currentKey = currentKey; }
		int getCurrentKey() { return currentKey; }
		void togglePause() { pause ^= 1; }
		int isPause() { return pause; }
		int getStepMode() { return stepMode; }
		void toggleStepMode() { stepMode ^= 1; }
		int getPerformStep() { return performStep; }
		void setPerformStep(int performStep) { this->performStep = performStep; }
		void setStepNext() { performStep = PERFORM_STEP_NEXT; }
		void setStepWait() { performStep = PERFORM_STEP_WAIT; }
		int isStepWait() { return performStep == PERFORM_STEP_WAIT; }
		int isStepModeOn() { return stepMode == STEP_MODE_ON; }
		int isThinking() { return thinking; }
		void setIsThinking() { thinking = THINKING_YES; }
		void setIsNotThinking() { thinking = THINKING_NO; }

		void toggleViewBoard() { viewBoard ^= 1; }
		int getViewBoard() { return viewBoard; }
		void toggleViewHelp() { viewHelp ^= 1; }
		int getViewHelp() { return viewHelp; }
		void toggleViewBoardInfo() { viewBoardInfo ^= 1; }
		int getViewBoardInfo() { return viewBoardInfo; }
		void toggleViewMoveList() { viewMoveList ^= 1; }
		int getViewMoveList() { return viewMoveList; }
		void toggleViewDiffMove() { viewDiffMove ^=1; }
		int getViewDiffMove() { return viewDiffMove; }

		void setDefaultParameters();
		void setLogFilename(double param);
		void printValidArgs();
		void writeSettings(std::ofstream &outfile);
};

#endif
