
ROOT=$(shell pwd)
LIB=$(ROOT)/../lib
INCLUDE=-I$(LIB)/include
LIB_PATH=-L$(LIB)/lib

test:base_json.cpp data.cpp
	g++ -o $@ $^ $(INCLUDE) $(LIB_PATH) -ljsoncpp -D_STYLE_
.PHONY:clean
clean:
	rm -f  test
