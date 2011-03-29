
#include <iostream>
#include <fstream>
#include <string.h>

#include "FileReader.h"

using namespace std;

FileReader::FileReader()
{
	openFlag = 0;
}

FileReader::~FileReader()
{
	if (openFlag)
		close();
}

void FileReader::close()
{
	openFlag = 0;
	file.close();
}

int FileReader::open(char *filename)
{
	if (strlen(filename) == 0)
		return 1;
	
	file.open(filename, ios::in | ios::binary);

	if (!file.good())
	{
		cout << "Could not open the file \"" << filename << "\"\n";
		return 1;
	}

	openFlag = 1;

	return 0;
}

unsigned char FileReader::readByte()
{
	unsigned char bytevalue;
	
	file.read((char *)&bytevalue, sizeof(char));

	return bytevalue;
}

short FileReader::readShort()
{
	short shortvalue;
	
	file.read((char *)&shortvalue, sizeof(short));

	return shortvalue;
}

int FileReader::readInt()
{
	int intvalue;
	
	file.read((char *)&intvalue, sizeof(int));

	return intvalue;
}

float FileReader::readFloat()
{
	float floatvalue;
	
	file.read((char *)&floatvalue, sizeof(float));

	return floatvalue;
}
		
double FileReader::readDouble()
{
	double doublevalue;
	
	file.read((char *)&doublevalue, sizeof(double));

	return doublevalue;
}
