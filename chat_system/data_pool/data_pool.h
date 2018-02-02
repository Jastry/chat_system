
#ifndef _DATA_POOL_H_
#define _DATA_POOL_H_

#include <iostream>
#include <vector>
#include <string>
#include <semaphore.h>

class data_pool{
	public:
		data_pool(int _cap);
		int get_data(std::string&);
		int put_data(const std::string &);
		~data_pool();
	private:
		std::vector<std::string> pool;
		int cap;
		int c_step;
		sem_t c_sem;

		int p_step;
		sem_t p_sem;
};

#endif

