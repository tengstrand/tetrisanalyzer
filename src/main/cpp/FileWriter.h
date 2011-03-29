
#ifndef __filewriter__
#define __filewriter__

#include <fstream>

class FileWriter
{
	private:
		short openFlag;
		std::ofstream file;

	public:
		FileWriter();
		~FileWriter();

		void close();
		int open(char *filename);
		int isOpen() { return openFlag; }
		void writeByte(unsigned char charval);
		void writeShort(unsigned short shortval);
		void writeInt(int intval);
		void writeFloat(float floatval);
		void writeDouble(double doubleval);
};

#endif
