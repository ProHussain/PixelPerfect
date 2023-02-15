package com.example.pixelperfect.Sticker;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

public class DrawableSticker extends Sticker {
    private Drawable drawable;

    private Rect realBounds;

    public DrawableSticker(Drawable paramDrawable) {
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
        return this.drawable;
    }

    public int getHeight() {
        return this.drawable.getIntrinsicHeight();
    }

    public int getWidth() {
        return this.drawable.getIntrinsicWidth();
    }

    public void release() {
        super.release();
        if (this.drawable != null)
            this.drawable = null;
    }

    @NonNull
    public DrawableSticker setAlpha(@IntRange(from = 0L, to = 255L) int paramInt) {
        this.drawable.setAlpha(paramInt);
        return this;
    }

    public DrawableSticker setDrawable(@NonNull Drawable paramDrawable) {
        this.drawable = paramDrawable;
        return this;
    }
}
