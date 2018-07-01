//
// Created by zhilaizhang on 17/3/16.
//

#include <jni.h>
#ifndef IDSSERIALPORT_SERIALPORT_H
#define IDSSERIALPORT_SERIALPORT_H

#endif //IDSSERIALPORT_SERIALPORT_H


JNIEXPORT jobject JNICALL Java_com_ids_serialport_SerialPort_open(JNIEnv *env, jclass type, jstring path_, jint baudrate,
                                        jint flags);

JNIEXPORT void JNICALL Java_com_ids_serialport_SerialPort_close(JNIEnv *env, jobject instance);