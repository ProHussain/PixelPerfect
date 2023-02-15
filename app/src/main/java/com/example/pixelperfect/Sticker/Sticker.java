package com.example.pixelperfect.Sticker;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class Sticker {
    private final float[] boundPoints = new float[8];

    private boolean isFlippedHorizontally;

    private boolean isFlippedVertically;

    private boolean isShow = true;

    private final float[] mappedBounds = new float[8];

    private final Matrix matrix = new Matrix();

    private final float[] matrixValues = new float[9];

    private final RectF trappedRect = new RectF();

    private final float[] unrotatedPoint = new float[2];

    private final float[] unrotatedWrapperCorner = new float[8];

    public boolean contains(float paramFloat1, float paramFloat2) {
        return contains(new float[]{paramFloat1, paramFloat2});
    }

    public boolean contains(@NonNull float[] paramArrayOffloat) {
        Matrix matrix = new Matrix();
        matrix.setRotate(-getCurrentAngle());
        getBoundPoints(this.boundPoints);
        getMappedPoints(this.mappedBounds, this.boundPoints);
        matrix.mapPoints(this.unrotatedWrapperCorner, this.mappedBounds);
        matrix.mapPoints(this.unrotatedPoint, paramArrayOffloat);
        StickerUtils.trapToRect(this.trappedRect, this.unrotatedWrapperCorner);
        return this.trappedRect.contains(this.unrotatedPoint[0], this.unrotatedPoint[1]);
    }

    public abstract void draw(@NonNull Canvas paramCanvas);

    public abstract int getAlpha();

    @NonNull
    public RectF getBound() {
        RectF rectF = new RectF();
        getBound(rectF);
        return rectF;
    }

    public void getBound(@NonNull RectF paramRectF) {
        paramRectF.set(0.0F, 0.0F, getWidth(), getHeight());
    }

    public void getBoundPoints(@NonNull float[] paramArrayOffloat) {
        if (!this.isFlippedHorizontally) {
            if (!this.isFlippedVertically) {
                paramArrayOffloat[0] = 0.0F;
                paramArrayOffloat[1] = 0.0F;
                paramArrayOffloat[2] = getWidth();
                paramArrayOffloat[3] = 0.0F;
                paramArrayOffloat[4] = 0.0F;
                paramArrayOffloat[5] = getHeight();
                paramArrayOffloat[6] = getWidth();
                paramArrayOffloat[7] = getHeight();
                return;
            }
            paramArrayOffloat[0] = 0.0F;
            paramArrayOffloat[1] = getHeight();
            paramArrayOffloat[2] = getWidth();
            paramArrayOffloat[3] = getHeight();
            paramArrayOffloat[4] = 0.0F;
            paramArrayOffloat[5] = 0.0F;
            paramArrayOffloat[6] = getWidth();
            paramArrayOffloat[7] = 0.0F;
            return;
        }
        if (!this.isFlippedVertically) {
            paramArrayOffloat[0] = getWidth();
            paramArrayOffloat[1] = 0.0F;
            paramArrayOffloat[2] = 0.0F;
            paramArrayOffloat[3] = 0.0F;
            paramArrayOffloat[4] = getWidth();
            paramArrayOffloat[5] = getHeight();
            paramArrayOffloat[6] = 0.0F;
            paramArrayOffloat[7] = getHeight();
            return;
        }
        paramArrayOffloat[0] = getWidth();
        paramArrayOffloat[1] = getHeight();
        paramArrayOffloat[2] = 0.0F;
        paramArrayOffloat[3] = getHeight();
        paramArrayOffloat[4] = getWidth();
        paramArrayOffloat[5] = 0.0F;
        paramArrayOffloat[6] = 0.0F;
        paramArrayOffloat[7] = 0.0F;
    }

    public float[] getBoundPoints() {
        float[] arrayOfFloat = new float[8];
        getBoundPoints(arrayOfFloat);
        return arrayOfFloat;
    }

    @NonNull
    public PointF getCenterPoint() {
        PointF pointF = new PointF();
        getCenterPoint(pointF);
        return pointF;
    }

    public void getCenterPoint(@NonNull PointF paramPointF) {
        paramPointF.set(getWidth() * 1.0F / 2.0F, getHeight() * 1.0F / 2.0F);
    }

    public float getCurrentAngle() {
        return getMatrixAngle(this.matrix);
    }


    @NonNull
    public abstract Drawable getDrawable();

    public abstract int getHeight();

    @NonNull
    public RectF getMappedBound() {
        RectF rectF = new RectF();
        getMappedBound(rectF, getBound());
        return rectF;
    }

    public void getMappedBound(@NonNull RectF paramRectF1, @NonNull RectF paramRectF2) {
        this.matrix.mapRect(paramRectF1, paramRectF2);
    }


    @NonNull
    public PointF getMappedCenterPoint() {
        PointF pointF = getCenterPoint();
        getMappedCenterPoint(pointF, new float[2], new float[2]);
        return pointF;
    }

    public void getMappedCenterPoint(@NonNull PointF paramPointF, @NonNull float[] paramArrayOffloat1, @NonNull float[] paramArrayOffloat2) {
        getCenterPoint(paramPointF);
        paramArrayOffloat2[0] = paramPointF.x;
        paramArrayOffloat2[1] = paramPointF.y;
        getMappedPoints(paramArrayOffloat1, paramArrayOffloat2);
        paramPointF.set(paramArrayOffloat1[0], paramArrayOffloat1[1]);
    }

    public void getMappedPoints(@NonNull float[] paramArrayOffloat1, @NonNull float[] paramArrayOffloat2) {
        this.matrix.mapPoints(paramArrayOffloat1, paramArrayOffloat2);
    }


    @NonNull
    public Matrix getMatrix() {
        return this.matrix;
    }

    public float getMatrixAngle(@NonNull Matrix paramMatrix) {
        return (float) Math.toDegrees(-Math.atan2(getMatrixValue(paramMatrix, 1), getMatrixValue(paramMatrix, 0)));
    }


    public float getMatrixValue(@NonNull Matrix paramMatrix, @IntRange(from = 0L, to = 9L) int paramInt) {
        paramMatrix.getValues(this.matrixValues);
        return this.matrixValues[paramInt];
    }

    public abstract int getWidth();

    public boolean isFlippedHorizontally() {
        return this.isFlippedHorizontally;
    }

    public boolean isFlippedVertically() {
        return this.isFlippedVertically;
    }

    public boolean isShow() {
        return this.isShow;
    }

    public void release() {
    }

    @NonNull
    public abstract Sticker setAlpha(@IntRange(from = 0L, to = 255L) int paramInt);

    public abstract Sticker setDrawable(@NonNull Drawable paramDrawable);

    @NonNull
    public Sticker setFlippedHorizontally(boolean paramBoolean) {
        this.isFlippedHorizontally = paramBoolean;
        return this;
    }

    @NonNull
    public Sticker setFlippedVertically(boolean paramBoolean) {
        this.isFlippedVertically = paramBoolean;
        return this;
    }

    public Sticker setMatrix(@Nullable Matrix paramMatrix) {
        this.matrix.set(paramMatrix);
        return this;
    }

    public void setShow(boolean paramBoolean) {
        this.isShow = paramBoolean;
    }

}
