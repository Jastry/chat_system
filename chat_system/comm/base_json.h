#ifndef _BASE_JSON_H_
#define _BASE_JSON_H_

#include <iostream>
#include <string.h>
#include <json/json.h>

void serialize(Json::Value &val, std::string &outString);
void unserialize(std::string &inString, Json::Value &val);


#endif
