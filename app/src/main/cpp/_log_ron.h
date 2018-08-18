//
// Created by Woo on 2017-11-24.
//

#ifndef TESTC_LOG_RON_H
#define TESTC_LOG_RON_H
#ifdef ANDROID
#include <android/log.h>
#include <stdio.h>
#define LOGE(format, ...)  __android_log_print(ANDROID_LOG_ERROR, "***GDU****", format, ##__VA_ARGS__)
#define LOGI(format, ...)  __android_log_print(ANDROID_LOG_INFO,  "====GDU===", format, ##__VA_ARGS__)
#else
#define LOGE(format, ...)  printf("---GDU---" format "\n", ##__VA_ARGS__)
#define LOGI(format, ...)  printf("***GDU****" format "\n", ##__VA_ARGS__)
#endif
#endif //TESTC_LOG_RON_H
