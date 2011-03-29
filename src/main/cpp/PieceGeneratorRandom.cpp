
#include <time.h>
#include "PieceGeneratorRandom.h"

PieceGeneratorRandom::PieceGeneratorRandom() : PieceGenerator()
{
	seed = time (NULL);
}

PieceGeneratorRandom::PieceGeneratorRandom(unsigned int seed) : PieceGenerator()
{
	this->seed = seed;
}

PieceGeneratorRandom::~PieceGeneratorRandom()
{
}

void PieceGeneratorRandom::setSeed(unsigned int seed)
{
	this->seed = seed;
}

int PieceGeneratorRandom::getPiece()
{
	seed *= (unsigned int)(1664525);
	seed += (unsigned int)(1013904223);

	return seed % 7 + 1;
}
