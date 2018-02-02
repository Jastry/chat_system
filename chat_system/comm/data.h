#ifndef _DATA_H_
#define _DATA_H_

#include <iostream>
#include <string>
#include "base_json.h"

class data{
	public:
		data();
		void dataSerialize(std::string &outString);
		void dataUnserialize(std::string &inString);
		~data();
	public:
		std::string nick_name;
		std::string school;
		std::string msg;
		std::string cmd;
};

#endif
