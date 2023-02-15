package com.example.pixelperfect.Vector;

import android.graphics.PointF;

public class DHANVINE_Vector2D extends PointF {
    public DHANVINE_Vector2D(float f, float f2) {
        super(f, f2);
    }

    public static float getAngle(DHANVINE_Vector2D dHANVINE_Vector2D, DHANVINE_Vector2D dHANVINE_Vector2D2) {
        dHANVINE_Vector2D.normalize();
        dHANVINE_Vector2D2.normalize();
        return (float) ((Math.atan2((double) dHANVINE_Vector2D2.y, (double) dHANVINE_Vector2D2.x) - Math.atan2((double) dHANVINE_Vector2D.y, (double) dHANVINE_Vector2D.x)) * 57.29577951308232d);
    }

    public void normalize() {
        float sqrt = (float) Math.sqrt((double) ((this.x * this.x) + (this.y * this.y)));
        this.x /= sqrt;
        this.y /= sqrt;
    }
}
