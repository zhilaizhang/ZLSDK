/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>

#include <stdio.h>
#include <time.h>

#include <sys/types.h>
#include <sys/socket.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include <netinet/in.h>
#include <unistd.h>
#include <sys/time.h>
#include "_log_ron.h"
/* Header for class com_gdu_library_UdpSocket */

#ifndef _Included_com_gdu_library_UdpSocket
#define _Included_com_gdu_library_UdpSocket
#ifdef __cplusplus
extern "C" {
#endif
char isStop2 = 0;
#define BUFFLENGTH 307200
//序列号---ron
int serialNum2 = 0;

char * cacheBuff2;

/************当前是否需要暂停ron***********/
char isPause2 = 0;

/**********数据回调的方法*******/
jmethodID callData2;
jclass cbClazz2;

//当前收到的长度----ron
int position2 = 0;

//接收一帧数据的分片包
int receiveCutNum2;

//丢掉的一帧数据的分片包
int lostCutNum2;

/***************************
 *  总共收到的包数---ron
 */
int receiverAllPckNum2 = 0;

/******************************************
 * 换成的数据，用来标识上次计算码流的数据
 */
int cachePckNum2 = 0;

/***************************
 *  总共丢包数---ron
 */
int lostAllPckNum2 = 0;

/************************
 * 上一次的丢包总数---ron
 */
int lastLoastAllPckNum2= 0;

/***************************
 *  上一次收到的包数---ron
 */
int lastReceiverAllPckNum2 = 0;

//建立sock的句柄 ---ron
int sock2 = 0;
size_t n2 = 0;

//是否显示日志信息
char showLog2 = 1;

/*******一帧数据的java 数组******/
jbyteArray oneFrameDataJ2;

/*************java数组的长度***************/
int  jArrayLength2;

int byte2Int2(char * source,int begin);

/*
 * Class:     com_gdu_library_UdpSocket
 * Method:    start
 * Signature: (Lcom/gdu/library/CBUdpSocket;)V
 */
JNIEXPORT void JNICALL Java_com_gdu_library_UdpSocket1_start
        (JNIEnv *, jobject, jobject,jint);

JNIEXPORT void JNICALL Java_com_gdu_library_UdpSocket1_showLog
        (JNIEnv * env, jobject job, jbyte showLog);


JNIEXPORT void JNICALL Java_com_gdu_library_UdpSocket1_droneType
        (JNIEnv * env, jobject job, jbyte droneType);

JNIEXPORT jint JNICALL Java_com_gdu_library_UdpSocket1_getReceiverData
        (JNIEnv *, jobject);

/*
 * Class:     com_gdu_library_UdpSocket
 * Method:    stop
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_gdu_library_UdpSocket1_stop
        (JNIEnv *, jobject);

JNIEXPORT void JNICALL Java_com_gdu_library_UdpSocket1_onResume
        (JNIEnv *, jobject);

JNIEXPORT void JNICALL Java_com_gdu_library_UdpSocket1_onPause
        (JNIEnv *, jobject);

void connSocket2(JNIEnv * env, jobject gObj ,int port);

/******************************
 * 处理每次收到的数据---ron
 */
void disposeData2(char* data,int offset,int length,JNIEnv * env, jobject gObj );

void sendData2Java2(JNIEnv * env, jobject gObj );

void destroyData2(JNIEnv * env);

FILE *pFile2;
FILE *pStrFile2;

void createFile2();

void writeData_(char *str,int length);

void writeData2_(char *str ,int data);

void writeStr_(char *str,int length);

/*******极客版本********/
char isGeekVersion2 = 0;

#ifdef __cplusplus
}
#endif
#endif
