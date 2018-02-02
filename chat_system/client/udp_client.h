#ifndef _UDP_CLIENT_H_
#define _UDP_CLIENT_H_

#include <iostream>
#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <string.h>
#include <map>
#include <string>

class udp_client{
	public:
		udp_client(const std::string &_ip, const int &_port);
		int initClient();
		int recvData(std::string &outString);
		int sendData(const std::string &inString);
		~udp_client();

	private:
		udp_client(const udp_client& );
	private:
		int sock;
		struct sockaddr_in server;
};

#endif
