package com.example.pixelperfect.Editor;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class FilterImageView extends AppCompatImageView {
    private OnImageChangedListener mOnImageChangedListener;

    interface OnImageChangedListener {
        void onBitmapLoaded(@Nullable Bitmap bitmap);
    }

    public FilterImageView(Context context) {
        super(context);
    }

    public FilterImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public FilterImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }


    public void setImageBitmap(Bitmap bitmap) {
        super.setImageBitmap(bitmap);
        if (this.mOnImageChangedListener != null) {
            this.mOnImageChangedListener.onBitmapLoaded(getBitmap());
        }
    }

    public void setImageIcon(@Nullable Icon icon) {
        super.setImageIcon(icon);
        if (this.mOnImageChangedListener != null) {
            this.mOnImageChangedListener.onBitmapLoaded(getBitmap());
        }
    }

    public void setImageMatrix(Matrix matrix) {
        super.setImageMatrix(matrix);
        if (this.mOnImageChangedListener != null) {
            this.mOnImageChangedListener.onBitmapLoaded(getBitmap());
        }
    }

    public void setImageState(int[] iArr, boolean z) {
        super.setImageState(iArr, z);
        if (this.mOnImageChangedListener != null) {
            this.mOnImageChangedListener.onBitmapLoaded(getBitmap());
        }
    }

    public void setImageTintList(@Nullable ColorStateList colorStateList) {
        super.setImageTintList(colorStateList);
        if (this.mOnImageChangedListener != null) {
            this.mOnImageChangedListener.onBitmapLoaded(getBitmap());
        }
    }

    public void setImageTintMode(@Nullable PorterDuff.Mode mode) {
        super.setImageTintMode(mode);
        if (this.mOnImageChangedListener != null) {
            this.mOnImageChangedListener.onBitmapLoaded(getBitmap());
        }
    }

    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        if (this.mOnImageChangedListener != null) {
            this.mOnImageChangedListener.onBitmapLoaded(getBitmap());
        }
    }

    public void setImageResource(int i) {
        super.setImageResource(i);
        if (this.mOnImageChangedListener != null) {
            this.mOnImageChangedListener.onBitmapLoaded(getBitmap());
        }
    }

    public void setImageURI(@Nullable Uri uri) {
        super.setImageURI(uri);
        if (this.mOnImageChangedListener != null) {
            this.mOnImageChangedListener.onBitmapLoaded(getBitmap());
        }
    }

    public void setImageLevel(int i) {
        super.setImageLevel(i);
        if (this.mOnImageChangedListener != null) {
            this.mOnImageChangedListener.onBitmapLoaded(getBitmap());
        }
    }

    @Nullable
    public Bitmap getBitmap() {
        if (getDrawable() != null) {
            return ((BitmapDrawable) getDrawable()).getBitmap();
        }
        return null;
    }
}
