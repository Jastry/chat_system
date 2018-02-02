#ifndef _UDP_SERVER_H_
#define _UDP_SERVER_H_

#include <iostream>
#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <string.h>
#include <map>
#include <string>
#include "data_pool.h"
#include "data.h"

class udp_server{
	public:
		udp_server(const std::string &_ip, const int &_port);
		int initServer();
		int recvData(std::string &outString);
		int broadcast();
		~udp_server();

	private:
		udp_server(const udp_server& );
		void addUser(struct sockaddr_in &peer, socklen_t &len);
		void delUser(struct sockaddr_in &peer, socklen_t &len);
		int sendData(std::string &inString,\
				     struct sockaddr_in &client, const socklen_t &len);
	private:
		int port;
		int sock;
		std::string ip;
		std::map<int, struct sockaddr_in> online_user;
		data_pool pool;
};

#endif





