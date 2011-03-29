
#include "Random.h"

Random::Random()
{
	this->seed = 0;
}

Random::Random(unsigned int seed)
{
	this->seed = seed;
}

Random::~Random()
{
}


unsigned int Random::getRandom()
{
	seed *= (unsigned int)(1664525);
	seed += (unsigned int)(1013904223);

	return seed;
}

int Random::getRandomP()
{
	unsigned int seed = getRandom();

	return seed % 7 + 1;
}
