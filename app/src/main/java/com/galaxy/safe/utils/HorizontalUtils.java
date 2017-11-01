package com.galaxy.safe.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Dell on 2016/10/17.
 */
public class HorizontalUtils {


    private Bitmap bitmap;
    private Context context;
    double roat;

    public HorizontalUtils(Context context, Bitmap bitmap) {
        this.bitmap = bitmap;
        this.context = context;
    }

    public double getRoat() {

        Mat lines = new Mat();
//        Bitmap bp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight()).copy(Bitmap.Config.RGB_565, true);

        double scale = 0.1;
        Mat img = new Mat();
        Utils.bitmapToMat(bitmap, img);
        Size dsize = new Size(img.width() * scale, img.height() * scale);
        Mat img2 = new Mat(dsize, CvType.CV_8SC1);
        Mat img3 = new Mat();
        img.convertTo(img2, CvType.CV_8SC1);
        Imgproc.Canny(img, img3, 50, 200);//图像边缘化
        Imgproc.HoughLinesP(img3, lines, 1, Math.PI / 180, 50, 50.0, 10.0);//提取直线

        int[] a = new int[(int) lines.total() * lines.channels()]; //数组a存储检测出的直线端点坐标
        lines.get(0, 0, a);

        int temp = 0;
        int temp1 = 0;
        int temp2 = 0;
        int temp3 = 0;

        for (int i = 0; i < a.length; i += 4)

        {
            Core.line(img3, new Point(a[i], a[i + 1]), new Point(a[i + 2], a[i + 3]), new Scalar(255, 0, 255), 4);
//            Log.i("66", new Point(a[0], a[1]) + "ff" + new Point(a[2], a[3]));
            if (a[i + 3] - a[i + 1] < a[i + 7] - a[i + 5]) {//对所有线段排序，线段最长的排前面
                temp = a[i + 3];
                temp1 = a[i + 1];
                temp2 = a[i + 2];
                temp3 = a[i + 0];
                a[i + 3] = a[i + 7];
                a[i + 1] = a[i + 5];
                a[i + 2] = a[i + 6];
                a[i + 0] = a[i + 4];
                a[i + 7] = temp;
                a[i + 5] = temp1;
                a[i + 6] = temp2;
                a[i + 4] = temp3;
            }
            if (roat == 0) {//线段最长的求角度
                roat = CalculateLineAngle(new Point(a[0], a[1]), new Point(a[2], a[3]));
                Log.i("66", roat + "oo");
                return roat;
            }
        }
        img = null;
        img3 = null;
        img2 = null;
        a = null;
        bitmap = null;
        return roat;
    }

    double CalculateLineAngle(Point p1, Point p2) {
        double xDis, yDis;
        xDis = p2.x - p1.x;
        yDis = p2.y - p1.y;
        double angle = Math.atan2(yDis, xDis);
        angle = angle / Math.PI * 180;
        return angle;
    }
}
