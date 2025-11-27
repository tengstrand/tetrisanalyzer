
#include <iostream>
#include <fstream>
#include <string.h>

#include "FileWriter.h"

using namespace std;

FileWriter::FileWriter()
{
	openFlag = 0;
}


FileWriter::~FileWriter()
{
	if (openFlag)
		close();
}


void FileWriter::close()
{
	openFlag = 0;
	file.close();
}

int FileWriter::open(char *filename)
{
	if (strlen(filename) == 0)
		return 1;

	file.open(filename, ios::out | ios::binary);

	if (!file.good())
	{
		cout << "Could not create the file \"" << filename << "\"\n";
		return 1;
	}

	openFlag = 1;

	return 0;
}

		
void FileWriter::writeByte(unsigned char charval)
{
	file.write((const char *)&charval, sizeof(char));
}

void FileWriter::writeShort(unsigned short shortval)
{
	file.write((const char *)&shortval, sizeof(short));
}

void FileWriter::writeInt(int intval)
{
	file.write((const char *)&intval, sizeof(int));
}

void FileWriter::writeFloat(float floatval)
{
	file.write((const char *)&floatval, sizeof(float));
}

void FileWriter::writeDouble(double doubleval)
{
	file.write((const char *)&doubleval, sizeof(double));
}
