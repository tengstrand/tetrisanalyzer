
#include <GL/glut.h>
#include "RenderText.h"

RenderText::RenderText(int x1, int y1, int height, float r, float g, float b)
{
	this->row = 0;
	this->x1 = x1;
	this->y1 = y1;
	this->height = height;
	this->r = r;
	this->g = g;
	this->b = b;
}

RenderText::~RenderText()
{
}


void RenderText::print(char *text)
{
    char *c;

	int y = y1 + row*height;

	glColor3f(r, g, b);

    glRasterPos3f(x1, y, 0);

    for (c=text; *c != '\0'; c++)
        glutBitmapCharacter(GLUT_BITMAP_HELVETICA_12, *c);
}

void RenderText::println(char *text)
{
	print(text);
	row++;
}

void RenderText::print(char *text, double val, int pos, int decimals)
{
	int width = getWidth(text);

	print(text);
	print(val, width + pos, decimals);
}


void RenderText::println(char *text, double val, int pos, int decimals)
{
	print(text, val, pos, decimals);
	row++;
}



void RenderText::print(double val, int pos, int decimals)
{
	char buffer[100];

	switch (decimals)
	{
		case 0:
			sprintf(buffer, "%.0f", val);
			break;
		case 2:
			sprintf(buffer, "%.2f", val);
			break;
		case 4:
			sprintf(buffer, "%.4f", val);
			break;
	}

	int length = strlen(buffer);

	if (decimals > 0)
		length -= decimals+1;

	int spaces = (length-1)/3;

  char *c;

	int y = y1 + row*height;

	glColor3f(r, g, b);

    glRasterPos3f(x1+pos, y, 0);

    for (c=buffer; *c != '\0'; c++) {
        glutBitmapCharacter(GLUT_BITMAP_HELVETICA_12, *c);
		    length--;

	    if (length > decimals && length % 3 == 0)
		    glutBitmapCharacter(GLUT_BITMAP_HELVETICA_12, ' ');
	}
}


void RenderText::print(char *text, double val, int pos)
{
	int width = getWidth(text);

	print(text);
	print(val, width + pos);
}


void RenderText::println(char *text, double val, int pos)
{
	print(text, val, pos);
	row++;
}


void RenderText::printr(double val, int pos, int decimals)
{
	char buffer[100];

	switch (decimals)
	{
		case 0:
			sprintf(buffer, "%.0f", val);
			break;
		case 1:
			sprintf(buffer, "%.1f", val);
			break;
		case 2:
			sprintf(buffer, "%.2f", val);
			break;
		case 4:
			sprintf(buffer, "%.4f", val);
			break;
	}

	int length = strlen(buffer);
	int spaces = (length-1)/3;

    char *c;

	int y = y1 + row*height;
	int width = getWidth(buffer) + getWidth(" ") * spaces;

	glColor3f(r, g, b);

    glRasterPos3f(x1 + pos - width, y, 0);

    for (c=buffer; *c != '\0'; c++) {
        glutBitmapCharacter(GLUT_BITMAP_HELVETICA_12, *c);
		length--;

		if (length > decimals && length % 3 == 0)
			glutBitmapCharacter(GLUT_BITMAP_HELVETICA_12, ' ');
	}
}

void RenderText::printr(char *text, int pos)
{
  char *c;

	int y = y1 + row*height;
	int width = getWidth(text);

	glColor3f(r, g, b);

    glRasterPos3f(x1 + pos - width, y, 0);

    for (c=text; *c != '\0'; c++)
        glutBitmapCharacter(GLUT_BITMAP_HELVETICA_12, *c);
}


void RenderText::render3(char *text, char *valText, int pos)
{
	int length = strlen(valText);
	int spaces = (length-1)/3;

    char *c;

	int y = y1 + row*height;
	int width = getWidth(text) + getWidth(valText) + getWidth(" ") * spaces;

	glColor3f(r, g, b);

    glRasterPos3f(x1 + pos, y, 0);

    for (c=text; *c != '\0'; c++) {
        glutBitmapCharacter(GLUT_BITMAP_HELVETICA_12, *c);
		length--;

		if (length % 3 == 0)
			glutBitmapCharacter(GLUT_BITMAP_HELVETICA_12, ' ');
	}
}

void RenderText::renderR3(char *text, int pos)
{
	int length = strlen(text);
	int spaces = (length-1)/3;

    char *c;

	int y = y1 + row*height;
	int width = getWidth(text) + getWidth(" ") * spaces;

	glColor3f(r, g, b);

    glRasterPos3f(x1 + pos - width, y, 0);

    for (c=text; *c != '\0'; c++) {
        glutBitmapCharacter(GLUT_BITMAP_HELVETICA_12, *c);
		length--;

		if (length % 3 == 0)
			glutBitmapCharacter(GLUT_BITMAP_HELVETICA_12, ' ');
	}
}

int  RenderText::getWidth(char *text)
{
    char *c;
	int width = 0;

    for (c=text; *c != '\0'; c++)
        width += glutBitmapWidth(GLUT_BITMAP_HELVETICA_12, *c);

	return width;
}

void RenderText::lineFeed()
{
	row++;
}

void RenderText::halfLineFeed()
{
	row += 0.4;
}
