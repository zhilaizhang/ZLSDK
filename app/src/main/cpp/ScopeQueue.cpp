/*
 * ScopeQueue.cpp
 *
 *      Author: george
 */
#ifndef SCOPEQUEUE_H_
#define SCOPEQUEUE_H_

#include "ScopeQueue.h"
#include <android/log.h>

#define TAG "ScopeQueue"

//------------------------------------------------------
ScopeQueue::ScopeQueue() {
	pthread_mutex_init(&queueMutex, NULL);
	front = rear = 0;
	queueSize = 0;
	isAbort = false;

	for (int i = 0; i < ScopeSIZE; i++) {
		list[i] = NULL;
	}
}
//------------------------------------------------------
ScopeQueue::~ScopeQueue() {
	pthread_mutex_destroy(&queueMutex);

	for (int i = 0; i < ScopeSIZE; i++) {
		if (list[i] != NULL) {
			delete list[i];
			list[i] = NULL;
		}
	}

	queueSize = 0;
}


//------------------------------------------------------
bool ScopeQueue::Enter(ScopeFrame* obj) {
	pthread_mutex_lock(&queueMutex);
	if (IsFull()) {
		pthread_mutex_unlock(&queueMutex);

		return false;
	}

	list[rear] = obj;
	rear = (rear + 1) % ScopeSIZE;

	queueSize++;

	pthread_mutex_unlock(&queueMutex);
	return true;
}
//------------------------------------------------------
ScopeFrame* ScopeQueue::getExistFrame() {
	pthread_mutex_lock(&queueMutex);
	ScopeFrame* existFrame = list[rear];
	pthread_mutex_unlock(&queueMutex);

	return existFrame;
}
//------------------------------------------------------
ScopeFrame* ScopeQueue::Out() {
	ScopeFrame* temp;
	pthread_mutex_lock(&queueMutex);
	if (IsEmpty()) {
		pthread_mutex_unlock(&queueMutex);

		return NULL;
	}
	temp = list[front];
	front = (front + 1) % ScopeSIZE;

	queueSize--;

	pthread_mutex_unlock(&queueMutex);

	return temp;
}
//------------------------------------------------------
bool ScopeQueue::IsEmpty() {
	if (rear == front)
		return true;
	else
		return false;
}
//------------------------------------------------------
bool ScopeQueue::IsFull() {
	if ((rear + 1) % ScopeSIZE == front)
		return true;
	else
		return false;
}
//------------------------------------------------------
int ScopeQueue::Size() {
	return queueSize;
}

bool ScopeQueue::isAborted() {
	return isAbort;
}

void ScopeQueue::Abort() {
	isAbort = true;
}
//------------------------------------------------------
#endif /* SCOPEQUEUE_H_ */
