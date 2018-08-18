//
// Created by Woo on 2018-4-24.
//
#include <malloc.h>
#include "_send_data.h"
#ifdef __cplusplus
extern "C" {
#endif


int sock_01 = 0;
struct sockaddr_in addr;
char * send_buff;
char  a;
void initSendSocket(const char * ipAdd,int port)
{
    a = 1;
    if ( (sock_01=socket(PF_INET, SOCK_STREAM, 0)) <0)
    {
        LOGE("create socket--send");
        return;
    }
    LOGE("initSendSocket ===begin");

    //创建网络通信对象
        addr.sin_family =AF_INET;
        addr.sin_port =htons(port);
        addr.sin_addr.s_addr=inet_addr(ipAdd);
    LOGE("initSendSocket ===begin11111");
    struct linger so_linger;
    so_linger.l_linger =1;
    so_linger.l_onoff = 0;
    setsockopt(sock_01,SOL_SOCKET,SO_LINGER,&so_linger,sizeof(so_linger));
    int ret = connect(sock_01, (struct sockaddr *) &addr, sizeof(addr));
    LOGE("create addr success:%d,%s",port,ipAdd);
    send_buff = (char *)malloc(1404);
}

void sendDataBySocket(char* buff,int off,int length)
{
    if(sock_01 != 0 )
    {
         *(send_buff + 0 )= 0xff;
         *(send_buff + 1 )= 0xff;
         *(send_buff + 2 )= ((short)(length))&0xff;
         *(send_buff + 3 )= ((short)(length)>>8);
         memcpy(send_buff+4,buff + off,length);
         send(sock_01,send_buff,1404,0);
    }
    LOGE("sendDataBySocket=====");
//    sendto(sock_01,buff+off,
//           length,0,(struct sockaddr*)&addr,sizeof(addr));
}

void onDestroySendSocket()
{
    free(send_buff);
    send_buff = NULL;
    if(sock_01 != 0)
    close(sock_01);
}


#ifdef __cplusplus
}
#endif
