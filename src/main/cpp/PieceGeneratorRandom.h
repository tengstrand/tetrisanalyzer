
#ifndef __piecegeneratorrandom__
#define __piecegeneratorrandom__

#include "PieceGenerator.h"

class PieceGeneratorRandom : public PieceGenerator
{
private:
	unsigned int seed;
   
public:
	PieceGeneratorRandom();
	PieceGeneratorRandom(unsigned int seed);
	virtual ~PieceGeneratorRandom();
	virtual int getPiece();
	void setSeed(unsigned int seed);
};

#endif
