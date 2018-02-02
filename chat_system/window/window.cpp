#include "window.h"


window::window()
{
	initscr();
	curs_set(0);
}

void window::drawHeader()
{
	int _h = LINES/5; 
	int _w = COLS;
	int _y = 0;
	int _x = 0;
	header = newwin(_h, _w, _y, _x);
	box(header, 0, 0);
}

void window::drawOutput()
{
	int _h = (LINES*3)/5; 
	int _w = (COLS*3)/4;
	int _y = LINES/5;
	int _x = 0;
	output = newwin(_h, _w, _y, _x);
	box(output, 0, 0);
}
void window::drawFlist()
{
	int _h = (LINES*3)/5; 
	int _w = COLS/4;
	int _y = LINES/5;
	int _x = (COLS*3)/4;
	flist = newwin(_h, _w, _y, _x);
	box(flist, 0, 0);
}
void window::drawInput()
{
	int _h = LINES/5; 
	int _w = COLS;
	int _y = (LINES*4)/5;
	int _x = 0;
	input = newwin(_h, _w, _y, _x);
	box(input, 0, 0);
}

void window::putStringToWin(WINDOW *w, int _y, int _x, std::string &msg)
{
	mvwaddstr(w, _y, _x, msg.c_str());
}

void window::getStringFromWin(WINDOW *w, std::string &msg)
{
	char buf[1024];
	wgetnstr(w, buf, sizeof(buf));
	msg = buf;
}

void window::clearLines(WINDOW *w, int begin, int nums)
{
	while(nums-- > 0){
		wmove(w, begin++, 0);
		wclrtoeol(w);
	}
}

window::~window()
{
	delwin(header);
	delwin(output);
	delwin(flist);
	delwin(input);
	endwin();
}


//int main()
//{
//	window w;

//
//	sleep(30);
//	return 0;
//}







