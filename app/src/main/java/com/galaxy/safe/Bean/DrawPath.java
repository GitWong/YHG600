package com.galaxy.safe.Bean;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by Dell on 2016/10/24.
 */
public class DrawPath {

    private Path path;
    private Paint paint;
    private int color;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public DrawPath(int color, Paint paint, Path path) {
        this.color = color;
        this.paint = paint;
        this.path = path;
    }
}
