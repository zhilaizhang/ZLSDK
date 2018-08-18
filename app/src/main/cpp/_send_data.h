//
// Created by Woo on 2018-4-24.
//

#ifndef CRTP_SEND_DATA_H
#define CRTP_SEND_DATA_H


#include<stdio.h>
#include<unistd.h>
#include<sys/types.h>
#include<sys/socket.h>
#include<arpa/inet.h>
#include "_log_ron.h"

#ifdef __cplusplus
extern "C" {
#endif


void initSendSocket(const char * ipAdd,int port);

void sendDataBySocket(char* buff,int off,int length);

void onDestroySendSocket();


#ifdef __cplusplus
}
#endif

#endif //CRTP_SEND_DATA_H


