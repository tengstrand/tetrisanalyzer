
#ifndef __gameinfo__
#define __gameinfo__

#include <stdio.h>
#include "Constants.h"

class GameInfo
{
	private:
		int games;
		long cntPieces;
		long cntLines;
		long totalPieces;
		long totalLines;
		long totalMoves;
		long totalSlided;
		double linesPerSecond;
		double piecesPerSecond;
		int d,h,m;
		double s;
		long minLines;
		long maxLines;
		long linesPerGame;
		int maxCntblockstat;
		long *cntblockstat;

	public:
		GameInfo(int maxCntBlockStat);
		~GameInfo();
		void set(long *cntblockstat,
						int games,
						long cntPieces,
		        long cntLines,
			      long totalPieces, 
			      long totalLines, 
						long totalMoves,
						long totalSlided,
						double linesPerSecond,
						double piecesPerSecond,
						int d, int h, int m, double s,
						long minLines,
						long maxLines,
						long linesPerGame);
		long *getCntblockstat() { return cntblockstat; }
		int getGames() { return games; }
		long getPieces() { return cntPieces; }
		long getLines() { return cntLines; }
		long getTotalPieces() { return totalPieces; }
		long getTotalLines() { return totalLines; }
		double getLinesPerSecond() { return linesPerSecond; }
		double getPiecesPerSecond() { return piecesPerSecond; }
		void getTime(int &d, int &h, int &m, double &s);
		long getMinLines() { return minLines; }
		long getMaxLines() { return maxLines; }
		long getLinesPerGame() { return linesPerGame; }
		double getMovesPerPiece() { return (totalPieces==0 ? 0 : (double)totalMoves / totalPieces); }
		double getTotalSlidedPercent() { return (totalPieces==0 ? 0 : (double)totalSlided / totalPieces * 100); }
};

#endif
