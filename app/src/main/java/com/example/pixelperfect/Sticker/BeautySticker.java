package com.example.pixelperfect.Sticker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.example.pixelperfect.Utils.SystemUtil;


public class BeautySticker extends Sticker {
    private Drawable drawable;
    private int drawableSizeBoobs;
    private int drawableSizeFace_Height;
    private int drawableSizeFace_Width;
    private int drawableSizeHip1_Height;
    private int drawableSizeHip1_Width;
    private int height_Width;
    private PointF mappedCenterPoint;
    private int radius;
    private Rect realBounds;
    private int type;

    public BeautySticker(Context paramContext, int paramInt, Drawable paramDrawable) {
        this.drawableSizeBoobs = SystemUtil.dpToPx(paramContext, 50);
        this.drawableSizeHip1_Width = SystemUtil.dpToPx(paramContext, 150);
        this.drawableSizeHip1_Height = SystemUtil.dpToPx(paramContext, 75);
        this.drawableSizeFace_Height = SystemUtil.dpToPx(paramContext, 50);
        this.drawableSizeFace_Width = SystemUtil.dpToPx(paramContext, 80);
        this.type = paramInt;
        this.drawable = paramDrawable;
        this.realBounds = new Rect(0, 0, getWidth(), getHeight());
    }


    public void draw(@NonNull Canvas paramCanvas) {
        paramCanvas.save();
        paramCanvas.concat(getMatrix());
        this.drawable.setBounds(this.realBounds);
        this.drawable.draw(paramCanvas);
        paramCanvas.restore();
    }

    public int getAlpha() {
        return this.drawable.getAlpha();
    }

    @NonNull
    public Drawable getDrawable() {
        return null;
    }

    public int getHeight() {
        return (this.type == 1 || this.type == 0) ? this.drawableSizeBoobs : ((this.type == 2) ? this.drawableSizeHip1_Height : ((this.type == 4) ? this.drawableSizeFace_Height : ((this.type == 10 || this.type == 11) ? this.drawable.getIntrinsicHeight() : 0)));
    }

    @NonNull
    public PointF getMappedCenterPoint2() {
        return this.mappedCenterPoint;
    }

    public int getRadius() {
        return this.radius;
    }

    public int getType() {
        return this.type;
    }

    public int getWidth() {
        return (this.type == 1 || this.type == 0) ? this.drawableSizeBoobs : ((this.type == 2) ? this.drawableSizeHip1_Width : ((this.type == 4) ? this.drawableSizeFace_Width : ((this.type == 10 || this.type == 11) ? this.height_Width : 0)));
    }

    public void release() {
        super.release();
        if (this.drawable != null)
            this.drawable = null;
    }

    @NonNull
    public BeautySticker setAlpha(@IntRange(from = 0L, to = 255L) int paramInt) {
        this.drawable.setAlpha(paramInt);
        return this;
    }

    public BeautySticker setDrawable(@NonNull Drawable paramDrawable) {
        return this;
    }

    public void setRadius(int paramInt) {
        this.radius = paramInt;
    }

    public void updateRadius() {
        RectF rectF = getBound();
        if (this.type == 0 || this.type == 1 || this.type == 4) {
            this.radius = (int) (rectF.left + rectF.right);
        } else if (this.type == 2) {
            this.radius = (int) (rectF.top + rectF.bottom);
        }
        this.mappedCenterPoint = getMappedCenterPoint();
    }
}
