package com.galaxy.safe.utils;

/**
 * Created by Dell on 2016/6/6.
 */
public class JniUtils {
    static {
        System.loadLibrary("print");
    }

    public native void print(byte[] ss, byte[] si, byte[] st, byte[] sc, byte[] sp, byte[] dn);
}
