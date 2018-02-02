#include <stdlib.h>
#include <signal.h>
#include "udp_client.h"
#include "data.h"
#include "window.h"


static void usage(const char *proc)
{
	std::cout << "Usage:\n\t" << proc << " [server_ip] " << "[server_port]\r\n" << std::endl;
}

udp_client *cp = NULL;
window *wp = NULL;
std::string nick_name;
std::string school;
std::vector<std::string> flist;

static void addUser(std::string &f)
{
	std::vector<std::string>::iterator iter = flist.begin();
	for(; iter != flist.end(); iter++){
		if(*iter == f){
			return;
		}
	}

	flist.push_back(f);
}

static void delUser(const std::string &f)
{
	std::vector<std::string>::iterator iter = flist.begin();
	for(; iter != flist.end(); ){
		if(*iter == f){
			iter = flist.erase(iter);
			break;
		}else{
			iter++;
		}
	}
}

static void* run_header(void *arg)
{
	window *wp = (window*)arg;

	wp->drawHeader();
	wrefresh(wp->header);
	std::string tips = "Welcome To Chat System!";
	int i = 1;
	int y,x;
	while(1){
		getmaxyx(wp->header, y, x);
		wp->putStringToWin(wp->header, y/2, i++, tips);
		wrefresh(wp->header);

		usleep(300000);
		wp->clearLines(wp->header, y/2, 1);
		wp->drawHeader();
		wrefresh(wp->header);
		if(i > x - tips.size() - 1){
			i=1;
		}
	}
}

static void* run_output_flist(void *arg)
{
	window *wp = (window*)arg;

	wp->drawOutput();
	wp->drawFlist();
	wrefresh(wp->output);
	wrefresh(wp->flist);

	std::string outputString;
	std::string friendString;
	std::string jsonString;
	data d;
	int i = 1;
	int y, x;
	while(1){
		getmaxyx(wp->output, y, x);
		cp->recvData(jsonString);
		d.dataUnserialize(jsonString);

		outputString = d.nick_name;
		outputString += "-";
		outputString += d.school;

		friendString = outputString;

		outputString += "# ";
		outputString += d.msg;

		if(d.cmd != "QUIT"){
	    	if(i > y - 2){
	    		wp->clearLines(wp->output, 1, y-1);
	    		wp->drawOutput();
	    		i = 1;
	    	}
	        wp->putStringToWin(wp->output, i++,\
					1, outputString);
			addUser(friendString);
		}else{
			delUser(friendString);
		}
	    wrefresh(wp->output);


		getmaxyx(wp->flist, y, x);
	    wp->clearLines(wp->flist, 1, y-1);
		wp->drawFlist();
		int i = 0;
		for(; i < flist.size(); i++){
			wp->putStringToWin(wp->flist, i+1, 1, flist[i]);
		}
	    wrefresh(wp->flist);
	}
}

static void* run_input(void *arg)
{
	window *wp = (window*)arg;

	std::string tips = "Please Enter# ";
	wp->drawInput();
	wp->putStringToWin(wp->input, 1, 1, tips);
	wrefresh(wp->input);

	data d;
	std::string msg;
	std::string sendString;
	while(1){
	    wp->getStringFromWin(wp->input, msg);

	    wp->clearLines(wp->input, 1, 1);
	    wp->drawInput();
	    wp->putStringToWin(wp->input, 1, 1, tips);
	    wrefresh(wp->input);

		d.nick_name = nick_name;
		d.school = school;
		d.msg = msg;
		d.cmd = "";
		d.dataSerialize(sendString);
		cp->sendData(sendString);
	}
}

void sendQuit(int sig)
{
	std::string sendString;
	data d;
	d.nick_name = nick_name;
	d.school = school;
	d.msg = "";
	d.cmd = "QUIT";
	d.dataSerialize(sendString);
	cp->sendData(sendString);
	delete wp;
	exit(0);
}

int main(int argc, char *argv[])
{
	if( argc != 3 ){
		usage(argv[0]);
		return 1;
	}

	std::cout << "Please Enter Your Nick Name# ";
	std::cin >> nick_name;
	std::cout << "Please Enter Your School# ";
	std::cin >> school;

	udp_client client(argv[1], atoi(argv[2]));
	client.initClient();
	cp = &client;

	wp = new window;

	signal(2, sendQuit);

	pthread_t header, output_flist, input;
	pthread_create(&header, NULL, run_header, wp);
	pthread_create(&output_flist, NULL, run_output_flist, wp);
	pthread_create(&input, NULL, run_input, wp);

	pthread_join(header, NULL);
	pthread_join(output_flist, NULL);
	pthread_join(input, NULL);
//	std::string outString;
//	std::string inString;
//	while(1){
//	    data d;
//	    d.nick_name = "flypig";
//	    d.school = "beida";
//	    d.cmd = "";
//		std::cout << "Please Enter# ";
//		std::cin >> d.msg;
//		d.dataSerialize(outString);
//		client.sendData(outString);
//		client.recvData(inString);
//
//		std::cout << "server echo# " << inString << std::endl;
//	}
	return 0;
}









