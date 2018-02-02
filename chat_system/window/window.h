#ifndef _WINDOW_H_
#define _WINDOW_H_

#include <iostream>
#include <ncurses.h>

class window{
	public:
		window();
        void drawHeader();
        void drawOutput();
        void drawFlist();
        void drawInput();
		void putStringToWin(WINDOW *w, int _y, int _x, std::string &msg);
		void getStringFromWin(WINDOW *w, std::string &msg);
		void clearLines(WINDOW *w, int begin, int nums);
		~window();
	public:
		WINDOW *header;
        WINDOW *output;
        WINDOW *flist;
        WINDOW *input;
};

#endif
