
#include "stdafx.h"

#include <gl/gl.h>
#include <gl/glu.h>
#include <gl/glaux.h>

#include "Render.h"

Render::Render()
{
	this->x = 0;
	this->dx = 1;
}

Render::Render(Board *board)
{
	this->board = board;
}


Render::~Render()
{
}


void Render::render()
{
    glPushMatrix();

	glBegin(GL_QUADS);
    glColor3f(0.0f, 0.9f, 0.9f);

	glVertex2i(0+x,2);
	glVertex2i(2+x,2);
	glVertex2i(2+x,1);
	glVertex2i(0+x,1);
    glEnd();

	glBegin(GL_QUADS);
	glVertex2i(1+x,1);
	glVertex2i(3+x,1);
	glVertex2i(3+x,0);
	glVertex2i(1+x,0);
    glEnd();

    glPopMatrix();

	x += dx;

	if (x <= 0 || x>= 50)
		dx = -dx;
}
