ROOT=$(shell pwd)
SERVER=$(ROOT)/server
CLIENT=$(ROOT)/client
DATA_POOL=$(ROOT)/data_pool
COMM=$(ROOT)/comm
WINDOW=$(ROOT)/window
LIB=$(ROOT)/lib

INCLUDE=-I$(DATA_POOL) -I$(LIB)/include -I$(COMM) -I$(WINDOW)

client_bin=chat_client
server_bin=chat_system
cc=g++

LIB_PATH=-L$(LIB)/lib
LDFLAGS=-lpthread  -ljsoncpp

server_src=$(shell ls $(SERVER)/*.cpp)
server_src+=$(shell ls $(DATA_POOL)/*.cpp)
server_src+=$(shell ls $(COMM)/*.cpp)
server_obj=$(notdir $(server_src:.cpp=.o))
client_src=$(shell ls $(CLIENT)/*.cpp)
client_src+=$(shell ls $(COMM)/*.cpp)
client_src+=$(shell ls $(WINDOW)/*.cpp)
client_obj=$(notdir $(client_src:.cpp=.o))

.PHONY:all
all:$(server_bin) $(client_bin)

$(server_bin):$(server_obj)
	@$(cc) -o $@ $^ $(LIB_PATH) $(LDFLAGS)
	@echo "Linking [$^] to [$@] ... done"
$(client_bin):$(client_obj)
	@$(cc) -o $@ $^ $(LIB_PATH) $(LDFLAGS) -lncurses -static
	@echo "Linking [$^] to [$@] ... done"
%.o:$(SERVER)/%.cpp
	@$(cc) -c $< $(INCLUDE)
	@echo "Compling [$<] to [$@] ... done"
%.o:$(CLIENT)/%.cpp
	@$(cc) -c $< $(INCLUDE)
	@echo "Compling [$<] to [$@] ... done"
%.o:$(DATA_POOL)/%.cpp
	@$(cc) -c $<
	@echo "Compling [$<] to [$@] ... done"
%.o:$(COMM)/%.cpp
	@$(cc) -c $< $(INCLUDE)
	@echo "Compling [$<] to [$@] ... done"
%.o:$(WINDOW)/%.cpp
	@$(cc) -c $< $(INCLUDE)
	@echo "Compling [$<] to [$@] ... done"


.PHONY:clean
clean:
	rm -rf $(server_bin) $(client_bin) *.o output

.PHONY:output
output:
	mkdir -p output/server/bin
	mkdir -p output/client
	cp conf output/server -a
	cp log output/server -a
	cp chat_system output/server/bin -a
	cp chat_client output/client -a
	cp plugin/server_ctl.sh output/server -a

.PHONY:debug
debug:
	@echo $(server_src)
	@echo $(server_obj)
	@echo $(client_src)
	@echo $(client_obj)











