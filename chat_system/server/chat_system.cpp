
#include <pthread.h>
#include <stdlib.h>
#include <unistd.h>
#include "udp_server.h"

static void usage(const char *proc)
{
	std::cout << "Usage:\n\t" << proc << " [local_ip] " << "[local_port]\r\n" << std::endl;
}

static void *runConsume(void *arg)
{
	udp_server *sp = (udp_server*)arg;
	while(1){
		sp->broadcast();
	}
}
static void *runProduct(void *arg)
{
	udp_server *sp = (udp_server*)arg;
	std::string recvString;
	while(1){
		sp->recvData(recvString);
		std::cout << "client# " << recvString << std::endl;
	}
}

int main(int argc, char *argv[])
{
	if(argc != 3){
		usage(argv[0]);
		return 1;
	}
	udp_server server(argv[1], atoi(argv[2]));
	server.initServer();

	daemon(1, 0);
	pthread_t c, p;
	pthread_create(&c, NULL, runConsume, &server);
	pthread_create(&p, NULL, runProduct, &server);

	pthread_join(c, NULL);
	pthread_join(p, NULL);

	return 0;
}
