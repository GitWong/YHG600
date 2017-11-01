package com.galaxy.safe.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.ImageView;

import com.galaxy.safe.R;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Dell on 2016/3/25.
 */
public class CheckBitmapUtils {

    private int x1, y1;//利用算法 求得匹配度最高的坐标
    private int x2, y2;//利用算法 最不匹配的坐标

    private Canvas canvas;

    private Bitmap recBitmap;
    private Bitmap reBitmap;
    private Context context;
    private ImageView iv_cam;
    private int left_x;
    private int left_y;
    private int D;
    private int[] grays;


    /**
     * @param
     * @param context
     * @param iv_cam
     * @return
     */

    public CheckBitmapUtils(Bitmap recBitmap, Context context, ImageView iv_cam, String left_x, String left_y, int D, Bitmap reBitmap, int[] grays) {
        this.context = context;
        this.recBitmap = recBitmap;
        this.reBitmap = reBitmap;
        this.iv_cam = iv_cam;
        this.left_x = Integer.valueOf(left_x);
        this.left_y = Integer.valueOf(left_y);
        this.D = D;
        this.grays = grays;

    }

    public CheckBitmapUtils(Bitmap recBitmap, Context context, ImageView iv_cam, String left_x, String left_y, int D) {
        this.context = context;
        this.recBitmap = recBitmap;
        this.iv_cam = iv_cam;
        this.left_x = Integer.valueOf(left_x);
        this.left_y = Integer.valueOf(left_y);
        this.D = D;

    }

    public CheckBitmapUtils(Bitmap recBitmap, Context context, String left_x, String left_y, int D) {
        this.context = context;
        this.recBitmap = recBitmap;
        this.left_x = Integer.valueOf(left_x);
        this.left_y = Integer.valueOf(left_y);
        this.D = D;

    }


    public int[] check() {
      /*  Log.i("66", "高度" + recBitmap.getHeight());
        Log.i("66", "宽度" + recBitmap.getWidth());

        Log.i("66", "高度1" + reBitmap.getHeight());
        Log.i("66", "宽度1" + reBitmap.getWidth());*/

        Bitmap inbitmap = Bitmap.createBitmap(recBitmap, left_x, left_y, 35, 30);
        Mat img = new Mat();
        Utils.bitmapToMat(inbitmap, img);
        Bitmap testbitmap = Bitmap.createBitmap(10, 25, Bitmap.Config.ARGB_8888);
        ImageView test = new ImageView(context);
        test.setImageResource(R.drawable.test);
        test.draw(new Canvas(testbitmap));

        Mat templ = new Mat();
        Utils.bitmapToMat(testbitmap, templ);

        // / Create the result matrix
        int result_cols = img.cols() - templ.cols() + 1;
        int result_rows = img.rows() - templ.rows() + 1;
        Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);

        Imgproc.matchTemplate(img, templ, result, Imgproc.TM_SQDIFF);
        Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());
        // / Localizing the best match with minMaxLoc
        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
        Point matchLoc;

        if (Imgproc.TM_SQDIFF == Imgproc.TM_SQDIFF || Imgproc.TM_SQDIFF == Imgproc.TM_SQDIFF_NORMED) {
            matchLoc = mmr.minLoc;
            Log.i("66", "matchLoc" + matchLoc);
            x1 = (int) matchLoc.x + left_x;
            y1 = (int) matchLoc.y + left_y + 7;

        } else {
            matchLoc = mmr.maxLoc;
            x2 = (int) matchLoc.x;
            y2 = (int) matchLoc.y;

        }
        matchLoc = mmr.maxLoc;
        x2 = (int) matchLoc.x + left_x;
        y2 = (int) matchLoc.y + left_y + 7;
        Log.i("66", "left_x" + left_x);
        Log.i("66", "left_y" + left_y);
        Log.i("66", "x1" + x1);

        Bitmap rBitmap = Bitmap.createBitmap(recBitmap, 0, 0, recBitmap.getWidth(), recBitmap.getHeight()).copy(Bitmap.Config.ARGB_8888, true);
        canvas = new Canvas(reBitmap);
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(2);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawBitmap(reBitmap, new android.graphics.Matrix(), paint);
        canvas.drawRect(x1, y1, x1 + 10, y1 + 25, paint);// c框


        paint.setColor(Color.BLUE);
        canvas.drawRect(x2, y2, x2 + 10, y2 + 25, paint);//左


        canvas.drawRect(x1 + 12, y1, x1 + 12 + D - 4 - 10, y1 + 25, paint);//右边


        Bitmap rightBitmap = Bitmap.createBitmap(recBitmap, x1 + 12, y1, D - 4 - 10, 25);
        int rGray = GrayUtils.getGray(rightBitmap);
        grays[0] = rGray;
        Log.i("66", "右边背景灰度值" + rGray);
        //
        Bitmap lBitmap = Bitmap.createBitmap(recBitmap, x2, y2, 10, 25);
        int lGray = GrayUtils.getGray(lBitmap);
        Log.i("66", "左背景灰度值" + lGray);
        grays[1] = lGray;

        Bitmap sgrayBitmap = Bitmap.createBitmap(recBitmap, x1, y1, 10, 25);
        int gray = GrayUtils.getGray(sgrayBitmap);
        Log.i("66", "c灰度值" + gray);
        grays[2] = gray;

        paint.setColor(Color.RED);
        canvas.drawRect(x1 + D, y1, x1 + D + 10, y1 + 25, paint);

        Bitmap tBitmap = Bitmap.createBitmap(recBitmap, x1 + D, y1, 10, 25);

        int tGray = GrayUtils.getGray(tBitmap);
        grays[3] = tGray;
        Log.i("66", "t灰度值" + tGray);

        paint.setColor(Color.BLUE);
        int dx = x1 - x2 - 10;
        canvas.drawRect(x2 + D + 20 + dx + 2, y2, x2 + 10 + D + 20 + dx, y2 + 25, paint); //t线右边背景
        iv_cam.setImageBitmap(reBitmap);
        Bitmap tRightBitmap = Bitmap.createBitmap(recBitmap, x2 + D + 22 + dx, y2, 10, 25);
        int tRightGray = GrayUtils.getGray(tRightBitmap);
        Log.i("66", "t右边背景灰度值" + tRightGray);
        grays[4] = tRightGray;

        paint.setColor(Color.RED);
        canvas.drawRect(left_x, left_y + 7, 34 + D + dx + x2, left_y + 35 + 7, paint);
//        Bitmap saveBitmap = Bitmap.createBitmap(recBitmap, left_x, left_y, 34 + D + dx + x2 - left_x, 35);
        recBitmap = null;
        inbitmap.recycle();
        img.release();
        testbitmap.recycle();
        templ.release();
        result.release();
        rBitmap.recycle();
        sgrayBitmap.recycle();
        tRightBitmap.recycle();
        canvas = null;

        return grays;
    }


    public void draw() {

        Bitmap inbitmap = Bitmap.createBitmap(recBitmap, left_x, left_y, 35, 30);
        Mat img = new Mat();
        Utils.bitmapToMat(inbitmap, img);
        Bitmap testbitmap = Bitmap.createBitmap(10, 25, Bitmap.Config.ARGB_8888);
        ImageView test = new ImageView(context);
        test.setImageResource(R.drawable.test);
        test.draw(new Canvas(testbitmap));

        Mat templ = new Mat();
        Utils.bitmapToMat(testbitmap, templ);

        // / Create the result matrix
        int result_cols = img.cols() - templ.cols() + 1;
        int result_rows = img.rows() - templ.rows() + 1;
        Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);

        Imgproc.matchTemplate(img, templ, result, Imgproc.TM_SQDIFF);
        Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());
        // / Localizing the best match with minMaxLoc
        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
        Point matchLoc;

        if (Imgproc.TM_SQDIFF == Imgproc.TM_SQDIFF || Imgproc.TM_SQDIFF == Imgproc.TM_SQDIFF_NORMED) {
            matchLoc = mmr.minLoc;
            Log.i("66", "matchLoc" + matchLoc);
            x1 = (int) matchLoc.x + left_x;
            y1 = (int) matchLoc.y + left_y + 7;

        } else {
            matchLoc = mmr.maxLoc;
            x2 = (int) matchLoc.x;
            y2 = (int) matchLoc.y;

        }
        matchLoc = mmr.maxLoc;
        x2 = (int) matchLoc.x + left_x;
        y2 = (int) matchLoc.y + left_y + 7;
        Log.i("66", "left_x" + left_x);
        Log.i("66", "left_y" + left_y);
        Log.i("66", "x1" + x1);

        Bitmap rBitmap = Bitmap.createBitmap(recBitmap, 0, 0, recBitmap.getWidth(), recBitmap.getHeight()).copy(Bitmap.Config.ARGB_8888, true);

        canvas = new Canvas();
        Bitmap copyBitmap = Bitmap.createBitmap(recBitmap.getWidth(), recBitmap.getHeight(), recBitmap.getConfig());
        canvas.setBitmap(copyBitmap);

        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(2);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawBitmap(recBitmap, new android.graphics.Matrix(), paint);
        canvas.drawRect(x1, y1, x1 + 10, y1 + 25, paint);// c框


        paint.setColor(Color.BLUE);
        canvas.drawRect(x2, y2, x2 + 10, y2 + 25, paint);//左

        canvas.drawRect(x1 + 12, y1, x1 + 12 + D - 4 - 10, y1 + 25, paint);//右边

        Bitmap rightBitmap = Bitmap.createBitmap(recBitmap, x1 + 12, y1, D - 4 - 10, 25);
        int rGray = GrayUtils.getGray(rightBitmap);
        Log.i("66", "右边背景灰度值" + rGray);
        //
        Bitmap lBitmap = Bitmap.createBitmap(recBitmap, x2, y2, 10, 25);
        int lGray = GrayUtils.getGray(lBitmap);
        Log.i("66", "左背景灰度值" + lGray);

        Bitmap sgrayBitmap = Bitmap.createBitmap(recBitmap, x1, y1, 10, 25);
        int gray = GrayUtils.getGray(sgrayBitmap);
        Log.i("66", "c灰度值" + gray);

        paint.setColor(Color.RED);
        canvas.drawRect(x1 + D, y1, x1 + D + 10, y1 + 25, paint);

        Bitmap tBitmap = Bitmap.createBitmap(recBitmap, x1 + D, y1, 10, 25);

        int tGray = GrayUtils.getGray(tBitmap);

        Log.i("66", "t灰度值" + tGray);
        paint.setColor(Color.BLUE);
        int dx = x1 - x2 - 10;
        canvas.drawRect(x2 + D + 20 + dx + 2, y2, x2 + 10 + D + 20 + dx, y2 + 25, paint); //t线右边背景
        iv_cam.setImageBitmap(copyBitmap);
        Bitmap tRightBitmap = Bitmap.createBitmap(recBitmap, x2 + D + 22 + dx, y2, 10, 25);
        int tRightGray = GrayUtils.getGray(tRightBitmap);
        Log.i("66", "t右边背景灰度值" + tRightGray);
        paint.setColor(Color.RED);
        canvas.drawRect(left_x, left_y + 7, 34 + D + dx + x2, left_y + 35 + 7, paint);
//        Bitmap saveBitmap = Bitmap.createBitmap(recBitmap, left_x, left_y, 34 + D + dx + x2 - left_x, 35);
        recBitmap = null;
        copyBitmap=null;
        inbitmap.recycle();
        img.release();
        testbitmap.recycle();
        templ.release();
        result.release();
        rBitmap.recycle();
        sgrayBitmap.recycle();
        tRightBitmap.recycle();
        canvas = null;
    }

    public int math() {

        Bitmap inbitmap = Bitmap.createBitmap(recBitmap, left_x, left_y, 35, 30);
        Mat img = new Mat();
        Utils.bitmapToMat(inbitmap, img);
        Bitmap testbitmap = Bitmap.createBitmap(10, 25, Bitmap.Config.ARGB_8888);
        ImageView test = new ImageView(context);
        test.setImageResource(R.drawable.test);
        test.draw(new Canvas(testbitmap));

        Mat templ = new Mat();
        Utils.bitmapToMat(testbitmap, templ);

        // / Create the result matrix
        int result_cols = img.cols() - templ.cols() + 1;
        int result_rows = img.rows() - templ.rows() + 1;
        Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);

        Imgproc.matchTemplate(img, templ, result, Imgproc.TM_SQDIFF);
        Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());
        // / Localizing the best match with minMaxLoc
        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
        Point matchLoc;

        if (Imgproc.TM_SQDIFF == Imgproc.TM_SQDIFF || Imgproc.TM_SQDIFF == Imgproc.TM_SQDIFF_NORMED) {
            matchLoc = mmr.minLoc;
            Log.i("66", "matchLoc" + matchLoc);
            x1 = (int) matchLoc.x + left_x;
            y1 = (int) matchLoc.y + left_y + 7;

        } else {
            matchLoc = mmr.maxLoc;
            x2 = (int) matchLoc.x;
            y2 = (int) matchLoc.y;

        }
        matchLoc = mmr.maxLoc;
        x2 = (int) matchLoc.x + left_x;
        y2 = (int) matchLoc.y + left_y + 7;
        Bitmap rBitmap = Bitmap.createBitmap(recBitmap, 0, 0, recBitmap.getWidth(), recBitmap.getHeight()).copy(Bitmap.Config.ARGB_8888, true);

        canvas = new Canvas(rBitmap);
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(2);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawBitmap(rBitmap, new android.graphics.Matrix(), paint);
        canvas.drawRect(x1, y1, x1 + 10, y1 + 25, paint);// c框
        paint.setColor(Color.BLUE);

        canvas.drawRect(x2, y2, x2 + 10, y2 + 25, paint);//左

        canvas.drawRect(x1 + 12, y1, x1 + 12 + D - 4 - 10, y1 + 25, paint);//右边

        Bitmap rightBitmap = Bitmap.createBitmap(recBitmap, x1 + 12, y1, D - 4 - 10, 25);
        int rGray = GrayUtils.getGray(rightBitmap);
        Log.i("66", "右边背景灰度值" + rGray);
        //
        Bitmap lBitmap = Bitmap.createBitmap(recBitmap, x2, y2, 10, 25);
        int lGray = GrayUtils.getGray(lBitmap);
        Log.i("66", "左背景灰度值" + lGray);

        Bitmap sgrayBitmap = Bitmap.createBitmap(recBitmap, x1, y1, 10, 25);

        int gray = GrayUtils.getGray(sgrayBitmap);
        Log.i("22", "c灰度值" + gray);
        sgrayBitmap.recycle();
        paint.setColor(Color.RED);
        canvas.drawRect(x1 + D, y1, x1 + D + 10, y1 + 25, paint);
        Bitmap tBitmap = Bitmap.createBitmap(recBitmap, x1 + D, y1, 10, 25);

        int tGray = GrayUtils.getGray(tBitmap);
        Log.i("66", "t灰度值" + tGray);
        paint.setColor(Color.BLUE);
        int dx = x1 - x2 - 10;
        canvas.drawRect(x2 + D + 20 + dx + 2, y2, x2 + 10 + D + 20 + dx, y2 + 25, paint); //t线右边背景

        Bitmap tRightBitmap = Bitmap.createBitmap(recBitmap, x2 + D + 22 + dx, y2, 10, 25);
        int tRightGray = GrayUtils.getGray(tRightBitmap);
        Log.i("66", "t右边背景灰度值" + tRightGray);
        paint.setColor(Color.RED);
        canvas.drawRect(left_x, left_y + 7, 34 + D + dx + x2, left_y + 35 + 7, paint);

//        Bitmap saveBitmap = Bitmap.createBitmap(recBitmap, left_x, left_y, 34 + D + dx + x2 - left_x, 35);

        recBitmap = null;
        inbitmap.recycle();
        img.release();
        testbitmap.recycle();
        templ.release();
        result.release();
        rBitmap.recycle();
        sgrayBitmap.recycle();
        tRightBitmap.recycle();
        canvas = null;

        return tGray;
    }
}
