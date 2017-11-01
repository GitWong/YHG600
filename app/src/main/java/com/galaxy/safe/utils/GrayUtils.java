package com.galaxy.safe.utils;

import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.imgproc.Imgproc;

/**
 * Created by Dell on 2016/3/8.
 */
public class GrayUtils {


    public static int getGray(Bitmap bt) {


        int sgray[] = new int[256];
        for (int i = 0; i < 256; i++) {
            sgray[i] = 0;
        }
        int width = bt.getWidth();

        int height = bt.getHeight();

        /*图像的类型，看看它是多少位的.如果是32位
        的要考虑aphal值通道,通过Raster对象来读取/写入像素，
        它自动帮你处理成为32位的. */

        byte ss[] = new byte[45 * 50];
        int grays = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = bt.getPixel(i, j);
                /*应为使用getRGB(i,j)获取的该点的颜色值是ARGB，
                而在实际应用中使用的是RGB，所以需要将ARGB转化成RGB，
                即bufImg.getRGB(i, j) & 0xFFFFFF。*/
                int r = (rgb & 0xff0000) >> 16;
                int g = (rgb & 0xff00) >> 8;
                int b = (rgb & 0xff);
                int gray = (int) (r * 0.3 + g * 0.59 + b * 0.11);    //计算灰度值
                grays = grays + gray;
              /*  sgray[gray]++;
                return gray;*/
            }
        }
        return grays;

    }

    public static int geGray(Bitmap bt) {
        int width = bt.getWidth();

        int height = bt.getHeight();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {


                
            }
        }
        return 0;
    }
}





