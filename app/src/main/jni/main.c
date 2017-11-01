#include <termios.h>
#include <unistd.h>
#include <stdint.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <string.h>
#include <jni.h>
#include <string.h>
#include "serial.h"
#include <stdlib.h>

#


int strleng(const char *str) {
    int len = 0;
    //使用断言排除str为空的情况
    while (*str++ != '\0') {
        len++;
    }
    return len;
}


char *Jbytearray2Char(JNIEnv *env, jbyteArray jbytearray) {
    jsize alen = (*env)->GetArrayLength(env, jbytearray); //获取长度
    jbyte *ba = (*env)->GetByteArrayElements(env, jbytearray, JNI_FALSE); //jbyteArray转为jbyte*
    uint8_t *rtn;
    if (alen > 0) {
        rtn = (uint8_t *) malloc(alen);         //"\0"
        memcpy(rtn, ba, alen);
    }
    (*env)->ReleaseByteArrayElements(env, jbytearray, ba, 0);  //释放掉
    return rtn;
}
size_t length(char *s) {
    char *p = s;
    int l = 0;
    while (*p != NULL) {
        p++;
        l++;
    }
    return l;
}

void Java_com_galaxy_safe_utils_JniUtils_print(JNIEnv *env, jobject thiz, jbyteArray s,
                                                  jbyteArray it,
                                                  jbyteArray t, jbyteArray co, jbyteArray p,jbyteArray dn) {
    uint8_t buf[128];
    uint8_t c;
    int fd;
    int i;

//      int length = (env)->GetStringLength(s);
//     const char *str = (*env)->GetStringUTFChars(env, s,0);
//    const char *sc = jstringToWindows(env, co);
//    const char *sc = jstringTostr(env, co);

    const char *str = Jbytearray2Char(env, s);
    const char *si = Jbytearray2Char(env, it);
    const char *st = Jbytearray2Char(env, t);
    const char *sc = Jbytearray2Char(env, co);
    const char *sp = Jbytearray2Char(env, p);
    const char *dno = Jbytearray2Char(env, dn);


    int len = (*env)->GetArrayLength(env, s);
    int leni = (*env)->GetArrayLength(env, it);
    int lent = (*env)->GetArrayLength(env, t);
    int lenc = (*env)->GetArrayLength(env, co);
    int lenp = (*env)->GetArrayLength(env, p);
    int lendn = (*env)->GetArrayLength(env, dn);



    printf("Serial printer test start ...\r\n");
    fd = serial_open("/dev/ttyAMA1", 9600);
    if (fd < 0) {
        printf("Can not open /dev/ttyAMA1.\r\n");
        return -1;
    }

    //打印开始
    //////////////
    buf[0] = 0x01;
    for (i = 0; i < 10; i++)
        serial_write(fd, buf, 1);
    buf[0] = 0x1b;
    buf[1] = 0x40;
    buf[2] = 0x00;
    serial_write(fd, buf, 2);
    uint8_t buff[10];
    usleep(1000);
    buf[0] = 0x1c;
    buf[1] = 0x26;
    serial_write(fd, buf, 2);
    usleep(1000);




    //检测人员
    buff[0] = 0xbc;
    buff[1] = 0xec;
    buff[2] = 0xb2;
    buff[3] = 0xe2;
    buff[4] = 0xc8;
    buff[5] = 0xcb;
    buff[6] = 0xd4;
    buff[7] = 0xb1;
    serial_write(fd, buff, 8);
    serial_write(fd, ":", 1);
    serial_write(fd, sp, lenp);
    serial_write(fd, "\n", 1);
    usleep(1000);

    //检测结论
    buff[0] = 0xbc;
    buff[1] = 0xec;
    buff[2] = 0xb2;
    buff[3] = 0xe2;
    buff[4] = 0xbd;
    buff[5] = 0xe1;
    buff[6] = 0xc2;
    buff[7] = 0xdb;
    serial_write(fd, buff, 8);
    serial_write(fd, ":", 1);
    serial_write(fd, sc, lenc);
    serial_write(fd, "\n", 1);
    usleep(1000);


//检测时间
    buff[0] = 0xbc;
    buff[1] = 0xec;
    buff[2] = 0xb2;
    buff[3] = 0xe2;
    buff[4] = 0xca;
    buff[5] = 0xb1;
    buff[6] = 0xbc;
    buff[7] = 0xe4;
    serial_write(fd, buff, 8);
    serial_write(fd, ":", 1);
    serial_write(fd, st, lent);
    serial_write(fd, "\n", 1);
    usleep(1000);
    //检测项目
    buff[0] = 0xbc;
    buff[1] = 0xec;
    buff[2] = 0xb2;
    buff[3] = 0xe2;
    buff[4] = 0xcf;
    buff[5] = 0xee;
    buff[6] = 0xc4;
    buff[7] = 0xbf;
    serial_write(fd, buff, 8);
    serial_write(fd, ":", 1);

    serial_write(fd, si, leni);
    serial_write(fd, "\n", 1);
    usleep(1000);
    //检测样品
    buff[0] = 0xd1;
    buff[1] = 0xf9;
    buff[2] = 0xc6;
    buff[3] = 0xb7;
    buff[4] = 0xc3;
    buff[5] = 0xfb;
    buff[6] = 0xb3;
    buff[7] = 0xc6;
    serial_write(fd, buff, 8);
    serial_write(fd, ":", 1);
    usleep(1000);
    serial_write(fd, str, len);
    serial_write(fd, "\n", 1);
    usleep(1000);
    //检测人员
    buff[0] = 0xbc;
    buff[1] = 0xec;
    buff[2] = 0xb2;
    buff[3] = 0xe2;

    serial_write(fd, buff, 4);
    serial_write(fd, ":", 1);
    serial_write(fd, dno, lendn);
    serial_write(fd, "\n", 1);
    usleep(1000);

    serial_write(fd, "\n\n\n\n\n\n\n\n\n\n\n\n\n\n", 14);
    usleep(1000);
    serial_close(fd);

//    return (*env)->NewStringUTF(env, sc);
}

