//
// Created by Woo on 2017-8-11.
//
#include "com_gdu_library_UdpSocket1.h"
#ifdef __cplusplus
extern "C" {
#endif

char hadGetFirstFrame2;

typedef unsigned char  byte;

/*
 * Class:     com_gdu_library_UdpSocket
 * Method:    start
 * Signature: (Lcom/gdu/library/CBUdpSocket;)V
 */
JNIEXPORT void JNICALL Java_com_gdu_library_UdpSocket1_start
        (JNIEnv *env, jobject job, jobject jcb,jint port)
{
   isStop2 = 0;
   connSocket2(env,jcb,port);
}

JNIEXPORT void JNICALL Java_com_gdu_library_UdpSocket1_showLog
        (JNIEnv * env, jobject job, jbyte isShowLog)
{
//    showLog = isShowLog;
//    LOGE("setShowLog===========");
    LOGE("********************************");
}


/*xx
 * Class:     com_gdu_library_UdpSocket
 * Method:    stop
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_gdu_library_UdpSocket1_stop
        (JNIEnv * env, jobject jobj)
{
  isStop2 = 1;
    /*if(sock != NULL )
        shutdown(sock,SHUT_WR);*/
}

  int byte2Int2(char * source,int begin)
  {
    int iOutcome = 0;
      LOGE("=====%d,%d,%d,%d",*(source +0),*(source +1),*(source +2),*(source +3));
    iOutcome |= ( (  *(source + begin ) & 0xFF) << 24);
      begin++;
    iOutcome |= ( ( *(source + begin ) & 0xFF) << 16);
      begin++;
    iOutcome |= ( ( *(source + begin ) & 0xFF) << 8);
      begin++;
    iOutcome |= ( *(source + begin ) & 0xFF);
    return iOutcome;
}

/*********设置当前飞机的类型********/
JNIEXPORT void JNICALL Java_com_gdu_library_UdpSocket1_droneType
        (JNIEnv * env, jobject job, jbyte droneType)
{
    LOGE("Java_com_gdu_library_UdpSocket_droneType:%d",droneType);
        if(droneType == 3)
        {
            isGeekVersion2 = 1;
        }
}

/*************************
 * 连接socket ----ron
 ************************/
void connSocket2(JNIEnv * env, jobject gObj ,int port)
{
         LOGI("go to connSocket22222222222222");
        struct sockaddr_in addr2;

        if ( (sock2=socket(AF_INET, SOCK_DGRAM, 0)) <0)
        {
            LOGE("create socket2222222222");
            exit(1);
        }
    struct timeval tv_out2;
    tv_out2.tv_sec = 2;//等待3秒
    tv_out2.tv_usec = 0;
    setsockopt(sock2,SOL_SOCKET,SO_RCVTIMEO,&tv_out2, sizeof(tv_out2));
        memset(&addr2,0,sizeof(addr2));
        LOGI("create socket success222200000002");
        addr2.sin_family = AF_INET;
        LOGI("use add 0001--2222222");
        addr2.sin_port = htons(port);
        LOGI("use add 0002--2222222");
        addr2.sin_addr.s_addr = htonl(INADDR_ANY);//inet_addr("127.0.0.1");//
        LOGI("use add 0003--2222222");
        if (addr2.sin_addr.s_addr == INADDR_NONE)
        {
            LOGE("Incorrect ip address!--2222222");
            close(sock2);
            return;
        }
        LOGI("begin bind ip --2222222");
        int len = sizeof(addr2);
        //绑定地址---必须要绑定
        n2 =  bind(sock2,(struct sockaddr*)&addr2,len);
        if(n2 < 0)
        {
            LOGE("bind fail %d,%s--2222222",errno,strerror(errno));
            close(sock2);
            return;
        }
    //存放数据的buffer
    char buff2[204800];
    int buffPosition2 = 0;//尾部的索引号
    int begin2 = 0; //头部起始的索引号
    int mark2 = 0;
    //从哪里接收到数据
    cacheBuff2 = (char*)malloc(BUFFLENGTH);
    isPause2 = 0;
    createFile2();
    while (1)
    {
//        LOGE("udp==begin ready Receive");
        n2 = recvfrom(sock2, buff2 + buffPosition2 , 4096, 0, (struct sockaddr *)&addr2, &len);
//        LOGE("udp====recvfrom:%d,%d--2222222",n2,port);
        if (n2>0)
        {
            //不需要自己组包的情况
            if (!isGeekVersion2)
            {
                receiverAllPckNum2 ++;
                disposeData2(buff2,0,n2,env,gObj);
            }
            //==========================需要组包的
            else
            {
                buffPosition2 += n2;
                for (int i = 0; i < 15; i++) {
                    mark2 = 0;
                    for (int i = begin2 + 2; i < buffPosition2-4; ++i)
                    {
                        if (*(buff2 + i + 0) == 0 &&
                            *(buff2 + i + 1) == 0 &&
                            *(buff2 + i + 2) == 0 &&
                            *(buff2 + i + 3) == 2)
                        {
                            mark2 = i;
                            break;
                        }
                    }
                    if (mark2 > 5) {
                        receiverAllPckNum2++;
//                    writeData(lastRtpBuff,mark);
//                    writeData("\n\n",2);
                        mark2 += 4;
//                        LOGE("disposeData:begin:%d,mark:%d,buffPosition:%d", begin, mark,
//                             buffPosition);
//                        writeData(buff + begin,mark-begin);
//                        writeData("\n\n",2);
                        disposeData2(buff2 + begin2, 0, mark2 - begin2 -4, env, gObj);
                        begin2 = mark2;
                        if (buffPosition2 + 4096 > 204800)
                        {
                            LOGE("=============================");
                            for (int i = 0; i < buffPosition2 - begin2; i++)
                            {
                                buff2[i] = buff2[i + begin2];
                            }
                            buffPosition2 -= begin2;
                            begin2 = 0;
                        }
                    } else
                    {
                        break;
                    }
                }
                //防止出现超出界限的bug
                if((buffPosition2 + 4096) > 204800 )
                {
//                    writeStr("buff positon > 204800:",buffPosition);
                    LOGE("buff positon > 204800:",buffPosition2);
                    buffPosition2 = 0;
                }
            }
        }
        else if (n2==0)
        {
            LOGE("server closed\n");
        }
        else if (n2 == -1)
        {
            LOGE("recvfrom length = -1");
        }
        if(isStop2)
        {
            LOGE("is Stop:%d",isStop2);
            break;
        }
    }
    if(sock2 != NULL )
    {
        close(sock2);
        sock2 = NULL;
    }
    destroyData2(env);
}

void destroyData2(JNIEnv * env)
{
  if(oneFrameDataJ2!= NULL )
  {
      LOGE("destory onframeData");
    (*env)->DeleteLocalRef(env, oneFrameDataJ2);
    oneFrameDataJ2 = NULL;
      LOGE("destory callData");
//    (*env)->DeleteLocalRef(env, callData);
//    callData = NULL;
//    (*env)->DeleteLocalRef(env, cbClazz);
//    cbClazz = NULL;
  }

   if(cacheBuff2 != NULL)
   {
     free(cacheBuff2);
       cacheBuff2 = NULL;
   }

}

void sendData2Java2(JNIEnv * env, jobject gObj )
{
    if(isPause2)
    {
        if(showLog2)
         LOGI("progress is Pause");
        return;
    }
   if(callData2 == NULL)
   {
       cbClazz2 = (*env)->GetObjectClass(env,gObj);
       callData2 = (*env)->GetMethodID(env,cbClazz2,"dataCB","([BI)V");
       LOGI("find java method");
   }

   //如果 oneFrameDataJ 为null的情况
   if(oneFrameDataJ2 == NULL)
   {
      oneFrameDataJ2 = (*env)->NewByteArray(env,position2);
      jArrayLength2 = position2;
      LOGI("oneFrameDataJ == NULL");
   }else if( jArrayLength2 < position2)//oneFrameDataJ的长度小于当前需要发送的长度
   {
        (*env)->DeleteLocalRef(env, oneFrameDataJ2);
        oneFrameDataJ2= NULL;
        oneFrameDataJ2 = (*env)->NewByteArray(env,position2);
        jArrayLength2 = position2;
        LOGI(" oneFrameDataJ ReCreate");
   }
   if(oneFrameDataJ2 == NULL)
   {
       LOGE("oneFrameDataJ is null,err====");
   }
    if(showLog2)
        LOGI("beigin set oneFrameDataJ positon:%d",position2);
  (*env)->SetByteArrayRegion(env,oneFrameDataJ2,0,position2,cacheBuff2);
  (*env)->CallVoidMethod(env,gObj,callData2,oneFrameDataJ2,position2);
}

/******************************
 * 处理每次收到的数据---ron
 */
void disposeData2(char* data,int offset,int length,JNIEnv * env, jobject gObj )
{
    if(length < 14) return;
    //先拿序列号
    int currentSerial = ( *(data+ 2)<<8) + *(data + 3);
    LOGI( "recvframe====length:%d,dataOfLast:%d",length,*(data + length -1));
    if(*(data + length -1) == 0)
    {
        LOGE( "recvframe====length:%d,dataOfLast:%d,%d,%d",length,*(data + length -3),*(data + length -2),*(data + length -1));
    }
    if(currentSerial -1 != serialNum2  && position2 > 0 )
    {
        LOGE("lost one package:%d,%d",currentSerial,serialNum2);
        int num = currentSerial - 1 - serialNum2;
        if( num > 0 )
        {
            lostCutNum2  += num;
            //计算丢包率的问题
            lostAllPckNum2 += num;
        }
        serialNum2 = currentSerial;
        return;
    }
    serialNum2 = currentSerial;
    char step = ( *(data+ 13)&0xc0 ) >> 6;
    char type = (*(data+ 12)&0x1f );
    if(showLog2 && length != 1400)
        LOGI("type:%d,step:%d,dataLength:%d,serialNum:%d,lostNum:%d",
             type,step,length,serialNum2,lostAllPckNum2);
    //没有丢序列号
    if(type == 28 )//分片的
    {
        if( step == 2 )//开始
        {
            position2 = 0;
            //开始收到第一包的数据，初始化状态
            receiveCutNum2 = 1;
            lostCutNum2  =  0;
            *(cacheBuff2+0) = 0;
            *(cacheBuff2+1) = 0;
            *(cacheBuff2+2) = 0;
            *(cacheBuff2+3) = 1;
            *(cacheBuff2+4) = (char)(*(data+ 12)& 224 | *(data+ 13) & 31);
            position2 += 5;
            memcpy(cacheBuff2 + position2 ,data + 14,length - 14);
            position2 += length - 14 ;
        }else if(step == 1)//一帧的结束
        {
            if(showLog2)
                LOGI("receive frame is end");
            if(position2 < 14)
            {
                LOGI("position < 14");
                return;
            }
            if(position2 + length -14 > BUFFLENGTH )
            {
                LOGE("receive data length too big");
                position2 = 0;
                return;
            }
            receiveCutNum2++;

            //计算丢包的多少
            if(!hadGetFirstFrame2)
            {
                if( lostCutNum2 > 0 )
                {
                    LOGE("lost data aaaaaaaaaaaa");
                    return;
                }
            } else
            if( lostCutNum2 * 2 > receiveCutNum2 )
            {
                LOGE("lost data > 25%");
//                return;
            }

            hadGetFirstFrame2 = 1;
            memcpy(cacheBuff2 + position2 ,data + 14,length - 14 );
            position2 += length - 14 ;
            int a = (receiveCutNum2-1)*1386+5+length-14;
            if(position2 != a )
            {
                LOGE("TYPE:%d,lostCutNum:%d,receiveCutNum:%d,length:%d,needLength:%d",
                     *(cacheBuff2+4),lostCutNum2,receiveCutNum2,position2,a);
            }
            if(showLog2)
                LOGI("TYPE:%d,lostCutNum:%d,receiveCutNum:%d,length:%d,needLength:%d",
                     *(cacheBuff2+4),lostCutNum2,receiveCutNum2,position2,a);
            sendData2Java2(env,gObj );
        }else
        {
            if(position2 < 14)
            {
                LOGI("position < 14");
                return;
            }
            if(position2 + length -14 > BUFFLENGTH )
            {
                LOGE("receive data length too big");
                position2 = 0;
                return;
            }
            memcpy(cacheBuff2 + position2 ,data + 14,length - 14 );
            position2 += length - 14 ;
            receiveCutNum2 ++;
        }
    }else
    {//不是分片的
        position2 = 0;
        *(cacheBuff2+0) = 0;
        *(cacheBuff2+1) = 0;
        *(cacheBuff2+2) = 0;
        *(cacheBuff2+3) = 1;
        position2 += 4;
        memcpy(cacheBuff2 + position2 ,data + 12,length - 12 );
        position2 += length - 12 ;
        sendData2Java2(env,gObj );
    }
}


/******************************
 * 处理每次收到的数据---ron
 */
/*void disposeData2(char* data,int offset,int length,JNIEnv * env, jobject gObj )
{
if(length < 14) return;
 //先拿序列号
 int currentSerial = ( *(data+ 2)<<8) + *(data + 3);
//  LOGI("recvframe====length:%d,serail:%d,serialNum:%d",n,currentSerial,serialNum);
    if(currentSerial -1 != serialNum2  && position2 > 0 )
    {
        position2 = 0;
        LOGE("lost one package");
        return;
    }
    serialNum2 = currentSerial;
    char step = ( *(data+ 13)&0xc0 ) >> 6;
    char type = (*(data+ 12)&0x1f );
    if(showLog2)
    LOGI("type:%d,step:%d",type,step);
    //没有丢序列号
    if(type == 28 )//分片的
    {
        if( step == 2 )//开始
        {
            position2 = 0;
            *(cacheBuff2+0) = 0;
            *(cacheBuff2+1) = 0;
            *(cacheBuff2+2) = 0;
            *(cacheBuff2+3) = 1;
            *(cacheBuff2+4) = (char)(*(data+ 12)& 224 | *(data+ 13) & 31);
            position2 += 5;
            memcpy(cacheBuff2 + position2 ,data + 14,length - 14 );
            position2 += length - 14 ;
        }
        else if(step == 1)//一帧的结束
        {
            if(showLog2)
             LOGI("receive frame is end");
           if(position2< 14)
           {
             LOGI("position < 14");
             return;
           }
            if(position2 + length -14 > BUFFLENGTH )
            {
                LOGE("receive data length too big");
                position2 = 0;
                return;
            }
            memcpy(cacheBuff2 + position2 ,data + 14,length - 14 );
            position2 += length - 14 ;
            sendData2Java2( env,gObj );
        }else
        {
            if(position2 < 14)
           {
             LOGI("position < 14");
             return;
           }
            if(position2 + length -14 > BUFFLENGTH )
            {
                LOGE("receive data length too big");
                position2 = 0;
                return;
            }
            memcpy(cacheBuff2 + position2 ,data + 14,length - 14 );
                     position2 += length - 14 ;
        }
    }else
    {//不是分片的
       position2 = 0;
       *(cacheBuff2+0) = 0;
       *(cacheBuff2+1) = 0;
       *(cacheBuff2+2) = 0;
       *(cacheBuff2+3) = 1;
       position2 += 4;
       memcpy(cacheBuff2 + position2 ,data + 12,length - 12 );
       position2 += length - 12 ;
        sendData2Java2(env,gObj );
    }
}*/

JNIEXPORT jint JNICALL Java_com_gdu_library_UdpSocket1_getReceiverData
        (JNIEnv *env, jobject jobject1)
{
//    LOGE("lostAllPckNum:%d;receiverAllPckNum:%d;"
//                 "lastLoastAllPckNum:%d;lastReceiverAllPckNum:%d",lostAllPckNum,receiverAllPckNum,lastReceiverAllPckNum,lastLoastAllPckNum);
    int b = ((lostAllPckNum2 - lastLoastAllPckNum2) + (receiverAllPckNum2 - lastReceiverAllPckNum2));
    int a = 100;
      if(b > 0)
      {
          a = (lostAllPckNum2 - lastLoastAllPckNum2)*100/b;

      } else
      {
          a = 1000;
      }
    lastReceiverAllPckNum2 = receiverAllPckNum2;
    lastLoastAllPckNum2 = lostAllPckNum2;
    return a;
}


JNIEXPORT void JNICALL Java_com_gdu_library_UdpSocket1_onResume
        (JNIEnv * env, jobject job)
        {
            hadGetFirstFrame2 = 0;
           isPause2 = 0;
        }

JNIEXPORT void JNICALL Java_com_gdu_library_UdpSocket1_onPause
        (JNIEnv * env, jobject job)
        {
         isPause2 = 1;
        }


void createFile2()
{
    pFile2 = fopen("/sdcard/logData.h264","wb+");
}

void writeData_(char *str,int length)
{
    char lenData[2];
    lenData[0] = (char) (length & 0xff);
    lenData[1] = (char) (length>>8 & 0xff);
    fwrite(lenData,1,2,pFile2);
    fwrite(str,1,length,pFile2);
    if( length < 1400 )
    {
        char filter[ 1400 - length ];
        for(int i = 0 ; i < 1400 - length; i ++ )
        {
            filter[i] = 0;
        }
        fwrite(filter,1,1400 - length,pFile2);
    }
    fflush(pFile2);
}
u_char pps720_[] = {0x00, 0x00, 0x00, 0x01, (byte) 0x67, (byte) 0x42, (byte) 0x80,
                   (byte) 0x1f, (byte) 0xda, (byte) 0x01, (byte) 0x40, (byte) 0x16,
                   (byte) 0xe8, (byte) 0x06, (byte) 0xd0, (byte) 0xa1, (byte) 0x35,
                   (byte) 0x04,
                   0, 0, 0, 1, (byte) 0x68, (byte) 0xce, (byte) 0x06, (byte) 0xe2};
char hadsave_ = 0;
void writeData2_(char *str,int data)
{
//    int n = 100;
//    char msg[100];// = malloc(n);
//    n = sprintf(msg,"%s:%d \n",str,data);
    if(*(str + 4)  == 101 )
    {
        hadsave_ = 1;
        fwrite(pps720_,1,26,pFile2);
    } else{
        if(!hadsave_) return;
    }

    fwrite(str,1,data,pFile2);
    fflush(pStrFile2);
//    writeData(msg,n);
}

void writeStr_(char *str,int length)
{
    int n = 100;
    char msg[100];// = malloc(n);
    n = sprintf(msg,"%s:%d \n",str,length);
    fwrite(msg,1,n,pStrFile2);
    fflush(pStrFile2);
//    writeData(msg,n);
}

#ifdef __cplusplus
}
#endif

