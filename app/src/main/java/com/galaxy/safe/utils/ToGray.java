package com.galaxy.safe.utils;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Dell on 2016/3/25.
 */
public class ToGray {
    public static Bitmap toGray(Bitmap bitmap) {

        Mat rgbMat = new Mat();
        Mat grayMat = new Mat();
        Bitmap grayBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
        Utils.bitmapToMat(bitmap, rgbMat);//convert original bitmap to Mat, R G B.
        Imgproc.cvtColor(rgbMat, grayMat, Imgproc.COLOR_RGB2GRAY);//rgbMat to gray grayMat
        Utils.matToBitmap(grayMat, grayBitmap); //convert mat to bitmap
        return grayBitmap;

    }
}
