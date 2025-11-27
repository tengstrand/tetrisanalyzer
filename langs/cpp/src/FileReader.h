
#ifndef __filereader__
#define __filereader__

#include <fstream>

class FileReader
{
	private:
		short openFlag;
		std::ifstream file;

	public:
		FileReader();
		~FileReader();

		void close();
		int open(char *filename);
		int isOpen() { return openFlag; }
		unsigned char readByte();
		short readShort();
		int readInt();
		float readFloat();
		double readDouble();
};

#endif
