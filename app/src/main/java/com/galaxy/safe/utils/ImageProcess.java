package com.galaxy.safe.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;

/**
 * Created by qiang on 2016/3/9.
 */
public class ImageProcess {
    public static String TAG = "图像校正";
    /*
    输入白板图片和原始检测图片，返回校正后的图片
     */
    public static int maxpointR = 0, maxpointG = 0, maxpointB = 0;

    public static Bitmap GetGrayCorrectionImage(Bitmap bitmap, Bitmap src) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[][] bitmapcolor = new int[width][height];
        int[][] srccolor = new int[width][height];
        int Maxcolor = 0;
        //  int maxpointR = 0, maxpointG = 0, maxpointB = 0;
        try {
            Maxcolor = Color.rgb(maxpointR, maxpointG, maxpointB);
            Log.i(TAG, "获取最大分量成功" + maxpointR + " " + maxpointG + " " + maxpointB + " 颜色值：" + String.valueOf(Maxcolor));
            Mat mat1 = new Mat();
            Mat mat2 = new Mat();
            Mat mat1Sub = new Mat();

            // 加载图片
            Bitmap srctemp = null;

            // 转换数据
            Utils.bitmapToMat(bitmap, mat1);
            Utils.bitmapToMat(src, mat2);

            /** 方法一加权 高级方式 可实现水印效果*********/
            //mat1Sub=mat1.submat(0, mat2.rows(), 0, mat2.cols());
            Core.addWeighted(mat2, 0.7, mat1, 0.3, 0., mat1Sub);
            Log.i(TAG, "叠加成功");
            srctemp = Bitmap.createBitmap(mat1Sub.cols(), mat1Sub.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(mat1Sub, srctemp);
            Log.i(TAG, "获取叠加图片成功");
            maxpointR = 255;
            maxpointG = 255;
            maxpointB = 255;
            for (int w1 = 0; w1 < width; w1++)   //X
            {
                for (int h1 = 0; h1 < height; h1++)   //Y
                {
                    bitmapcolor[w1][h1] = bitmap.getPixel(w1, h1);
                    srccolor[w1][h1] = src.getPixel(w1, h1);
                    int br = Color.red(bitmapcolor[w1][h1]);
                    int bg = Color.green(bitmapcolor[w1][h1]);
                    int bb = Color.blue(bitmapcolor[w1][h1]);
                    int sr = Color.red(srccolor[w1][h1]);
                    int sg = Color.green(srccolor[w1][h1]);
                    int sb = Color.blue(srccolor[w1][h1]);
                    float kr = ((float) sr * (float) maxpointR) / (float) br;
                    float kg = ((float) sg * (float) maxpointG) / (float) bg;
                    float kb = ((float) sb * (float) maxpointB) / (float) bb;
                    if (kr > 255) {
                        kr = 255;
                    }
                    if (kg > 255) {
                        kg = 255;
                    }
                    if (kb > 255) {
                        kb = 255;
                    }
                    Maxcolor = Color.rgb((int) kr, (int) kg, (int) kb);
                    Log.i(TAG, "maxcolor " + Maxcolor + " " + (int) kr + " " + (int) kg + " " + (int) kb);
                    //Log.i(TAG,"位置："+w1+" "+h1+" 颜色值"+br+" "+bg+" "+bb+" "+sr+" "+sg+" "+sb+" 比值 "+kr+" "+kg+" "+kb);
                    //   int color=Color.rgb(maxpointR*Color.red(srccolor[w1][h1])/Color.red(bitmapcolor[w1][h1]),maxpointG*Color.green(srccolor[w1][h1]) / Color.green(bitmapcolor[w1][h1]), maxpointB * Color.blue(srccolor[w1][h1])/Color.blue(bitmapcolor[w1][h1]));
                    srctemp.setPixel(w1, h1, Maxcolor);
                    // Log.i(TAG,"颜色："+String.valueOf(color));
                }
            }
            Log.i(TAG, "设置像素成功");
            return srctemp;
        } catch (Exception e) {
            Log.i(TAG, "错误" + e.getMessage());
            return null;
        }
    }

}
