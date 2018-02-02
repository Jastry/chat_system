
#include "base_json.h"

void serialize(Json::Value &val, std::string &outString)
{
#ifdef _STYLE_
	Json::StyledWriter w;
#else
	Json::FastWriter w;
#endif
	outString = w.write(val);
}

void unserialize(std::string &inString, Json::Value &val)
{
	Json::Reader r;
	r.parse(inString, val, false);
}














