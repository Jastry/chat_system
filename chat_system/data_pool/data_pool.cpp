
#include "data_pool.h"


data_pool::data_pool(int _cap):
	pool(_cap), cap(_cap)
{
	c_step = 0;
	p_step = 0;
	sem_init(&c_sem, 0, 0);
	sem_init(&p_sem, 0, cap);
}

int data_pool::get_data(std::string &outString)
{
	sem_wait(&c_sem);
	outString = pool[c_step++];
	c_step %= cap;
	sem_post(&p_sem);
}
int data_pool::put_data(const std::string &inString)
{
	sem_wait(&p_sem);
	pool[p_step++] = inString;
	p_step %= cap;
	sem_post(&c_sem);
}

data_pool::~data_pool()
{
	sem_destroy(&c_sem);
	sem_destroy(&p_sem);
}













