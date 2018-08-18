/*
 * ThreadQueue.h
 *      Author: george
 */

#include <pthread.h>
#include "ScopeFrame.h"

const int ScopeSIZE = 60;

class ScopeQueue {
public:
	ScopeQueue();
	~ScopeQueue();

	bool Enter(ScopeFrame* obj);
	ScopeFrame* Out();
	ScopeFrame* getExistFrame();
	bool IsEmpty();
	bool IsFull();
	int Size();
	void Abort();
	bool isAborted();
private:
	int front;
	int rear;
	int queueSize;
	bool isAbort;
	ScopeFrame* list[ScopeSIZE];
	pthread_mutex_t queueMutex;
};
