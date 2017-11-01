package com.galaxy.safe.utils;

import android.util.Log;

/**
 * Created by Dell on 2016/10/13.
 */
public class ValueUtils {

    public Float min;
    public Float max;
    public int n;
    public Float gay;

    public ValueUtils(Float min, Float max, int n, Float gay) {

        this.min = min;
        this.max = max;
        this.n = n;
        this.gay = gay;
    }

    public Float math() {

        Float value = null;
        Float piece = (max - min )/ n;


        int k = (int) (gay / piece);

        if (Math.abs(k * piece - gay) >= Math.abs((k + 1) * piece - gay)) {
            value = (k + 1) * piece;
        } else {
            value = k * piece;
        }

        return value;
    }
}
