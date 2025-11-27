
#include <cstdio>
#include <cstring>
#include <string>

#if defined(__APPLE__)
#include <OpenGL/gl.h>
#else
#include <GL/gl.h>
#endif

#define STB_EASY_FONT_IMPLEMENTATION
#include "third_party/stb_easy_font.h"

#include "RenderText.h"

namespace
{
void drawString(float x, float y, const char *text, float r, float g, float b)
{
	if (text == nullptr || *text == '\0')
		return;

	static char buffer[64 * 1024];
	int numQuads = stb_easy_font_print(x, y, const_cast<char*>(text), nullptr, buffer, sizeof(buffer));

	glColor3f(r, g, b);
	glBegin(GL_QUADS);
	for (int i = 0; i < numQuads * 4; ++i)
	{
		char *ptr = buffer + i * 16;
		float vx = *reinterpret_cast<float*>(ptr + 0);
		float vy = *reinterpret_cast<float*>(ptr + 4);
		glVertex2f(vx, vy);
	}
	glEnd();
}

std::string addGrouping(const std::string &value)
{
	if (value.empty())
		return value;

	std::string result = value;
	const size_t decimalPos = result.find('.');
	const size_t end = (decimalPos == std::string::npos) ? result.size() : decimalPos;
	const size_t start = (result[0] == '-') ? 1 : 0;

	size_t insertPos = end;
	while (insertPos > start + 3)
	{
		insertPos -= 3;
		result.insert(insertPos, " ");
	}

	return result;
}

std::string formatNumber(double val, int decimals)
{
	char buffer[128];
	char fmt[16];
	std::snprintf(fmt, sizeof(fmt), "%%.%df", decimals);
	std::snprintf(buffer, sizeof(buffer), fmt, val);
	return buffer;
}
}

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
	const char *safeText = text ? text : "";
	drawString(static_cast<float>(x1), static_cast<float>(y1 + row * height), safeText, r, g, b);
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
	std::string formatted = addGrouping(formatNumber(val, decimals));
	drawString(static_cast<float>(x1 + pos), static_cast<float>(y1 + row * height), formatted.c_str(), r, g, b);
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
	std::string formatted = addGrouping(formatNumber(val, decimals));
	int width = getWidth(const_cast<char*>(formatted.c_str()));
	drawString(static_cast<float>(x1 + pos - width), static_cast<float>(y1 + row * height), formatted.c_str(), r, g, b);
}

void RenderText::printr(char *text, int pos)
{
	const char *safeText = text ? text : "";
	int width = getWidth(const_cast<char*>(safeText));
	drawString(static_cast<float>(x1 + pos - width), static_cast<float>(y1 + row * height), safeText, r, g, b);
}

void RenderText::render3(char *text, char *valText, int pos)
{
	const char *safeText = text ? text : "";
	std::string value = valText ? valText : "";
	value = addGrouping(value);
	std::string combined = std::string(safeText) + " " + value;
	drawString(static_cast<float>(x1 + pos), static_cast<float>(y1 + row * height), combined.c_str(), r, g, b);
}

void RenderText::renderR3(char *text, int pos)
{
	const char *safeText = text ? text : "";
	std::string grouped = addGrouping(safeText);
	int width = getWidth(const_cast<char*>(grouped.c_str()));
	drawString(static_cast<float>(x1 + pos - width), static_cast<float>(y1 + row * height), grouped.c_str(), r, g, b);
}

int  RenderText::getWidth(char *text)
{
	const char *safeText = text ? text : "";
	return stb_easy_font_width(const_cast<char*>(safeText));
}

int RenderText::measureWidth(const char *text)
{
	const char *safeText = text ? text : "";
	return stb_easy_font_width(const_cast<char*>(safeText));
}

int RenderText::measureHeight(const char *text)
{
	const char *safeText = text ? text : "";
	return stb_easy_font_height(const_cast<char*>(safeText));
}

void RenderText::lineFeed()
{
	row++;
}

void RenderText::halfLineFeed()
{
	row += 0.4;
}
