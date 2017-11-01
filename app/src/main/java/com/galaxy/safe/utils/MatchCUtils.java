package com.galaxy.safe.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.widget.ImageView;

import com.galaxy.safe.R;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 2016/10/18.
 */
public class MatchCUtils {
    private Bitmap bitmap;
    private Context context;
    private List list;
    private int x1, y1;//利用算法 求得匹配度最高的坐标
    private int x2, y2;//利用算法 最不匹配的坐标


    public MatchCUtils(Context context, Bitmap bitmap) {

        this.bitmap = bitmap;
        this.context = context;
    }

    public List match() {
        list = new ArrayList();

        Bitmap inbitmap = Bitmap.createBitmap(bitmap, 40, 0, 152, 152);
        Mat img = new Mat();
        Utils.bitmapToMat(inbitmap, img);
        Bitmap testbitmap = Bitmap.createBitmap(10, 65, Bitmap.Config.ARGB_8888);
        ImageView test = new ImageView(context);
        test.setImageResource(R.drawable.test2);
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
            Log.i("22", "matchLoc" + matchLoc);
            x1 = (int) matchLoc.x;
            y1 = (int) matchLoc.y;

        } else {
            matchLoc = mmr.maxLoc;
            x2 = (int) matchLoc.x;
            y2 = (int) matchLoc.y;

        }
        matchLoc = mmr.maxLoc;
        x2 = (int) matchLoc.x;
        y2 = (int) matchLoc.y;
        list.add(x1 - 16+40);
        list.add(y1 + 10);


        Log.i("66", x1 + "oo" + y1);


        return list;
    }

    public List matchB() {
        list = new ArrayList();

        Bitmap inbitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());
        Mat img = new Mat();
        Utils.bitmapToMat(inbitmap, img);
        Bitmap testbitmap = Bitmap.createBitmap(10, 65, Bitmap.Config.ARGB_8888);
        ImageView test = new ImageView(context);
        test.setImageResource(R.drawable.test2);
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
            Log.i("22", "matchLoc" + matchLoc);
            x1 = (int) matchLoc.x;
            y1 = (int) matchLoc.y;

        } else {
            matchLoc = mmr.maxLoc;
            x2 = (int) matchLoc.x;
            y2 = (int) matchLoc.y;

        }
        matchLoc = mmr.maxLoc;
        x2 = (int) matchLoc.x;
        y2 = (int) matchLoc.y;
        list.add(x1 - 16);
        list.add(y1 + 10);


        Log.i("66", x1 + "oo" + y1);


        return list;
    }

}
