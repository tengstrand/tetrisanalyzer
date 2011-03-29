#ifndef __random__
#define __random__

class Random
{
	private:
		unsigned int seed;

	public:
		Random();
		Random(unsigned int seed);
		~Random();
		void setSeed(int seed) { this->seed = seed; }
		unsigned int getRandom();
		int getRandomP();
};

#endif
