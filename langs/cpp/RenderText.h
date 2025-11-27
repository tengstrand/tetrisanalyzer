
#ifndef __renderText__
#define __renderText__

#include "Game.h"

class RenderText
{
	private:
		double row;
		int x1;
		int y1;
		int height;
		float r;
		float g;
		float b;

	public:
		RenderText(int x1, int y1, int height, float r=0.0, float g=0.0, float b=0.0);
		~RenderText();
		int  getWidth(char *text);
		static int measureWidth(const char *text);
		static int measureHeight(const char *text);

		void print(char *text);
		void print(double val, int pos=0, int decimals=0);
		void print(char *text, double val, int pos=0);
		void print(char *text, double val, int pos, int decimals);
		void println(char *text);
		void println(char *text, double val, int pos=0);
		void printr(char *text, int pos);
		void printr(double val, int pos, int decimals=0);
		void println(char *text, double val, int pos, int decimals);
		void render3(char *text, char *valText, int pos);
		void renderR3(char *text, int pos);
		void lineFeed();
		void halfLineFeed();
};

#endif

