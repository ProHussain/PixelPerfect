package com.example.pixelperfect.Editor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.pixelperfect.Listener.OnSaveBitmap;
import com.example.pixelperfect.R;
import com.example.pixelperfect.Sticker.StickerView;

import org.wysaid.view.ImageGLSurfaceView;

import java.util.ArrayList;
import java.util.List;

public class PhotoEditorView extends StickerView {

    private Bitmap currentBitmap;
    private BrushDrawingView brushDrawingView;
    public ImageGLSurfaceView imageGLSurfaceView;
    private FilterImageView filterImageView;
    private List<Bitmap> bitmaplist=new ArrayList<>();
    private int index=-1;

    public PhotoEditorView(Context context) {
        super(context);
        init(null);
    }

    public PhotoEditorView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(attributeSet);
    }

    public PhotoEditorView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(attributeSet);
    }


    @SuppressLint({"Recycle", "ResourceType"})
    private void init(@Nullable AttributeSet attributeSet) {
        this.filterImageView = new FilterImageView(getContext());
        this.filterImageView.setId(1);
        this.filterImageView.setAdjustViewBounds(true);
        this.filterImageView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams.addRule(13, -1);
        this.brushDrawingView = new BrushDrawingView(getContext());
        this.brushDrawingView.setVisibility(View.GONE);
        this.brushDrawingView.setId(2);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams2.addRule(13, -1);
        layoutParams2.addRule(6, 1);
        layoutParams2.addRule(8, 1);
        this.imageGLSurfaceView = new ImageGLSurfaceView(getContext(), attributeSet);
        this.imageGLSurfaceView.setId(3);
        this.imageGLSurfaceView.setVisibility(View.VISIBLE);
        this.imageGLSurfaceView.setAlpha(1.0f);
        this.imageGLSurfaceView.setDisplayMode(ImageGLSurfaceView.DisplayMode.DISPLAY_ASPECT_FIT);
        RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams3.addRule(13, -1);
        layoutParams3.addRule(6, 1);
        layoutParams3.addRule(8, 1);
        addView(this.filterImageView, layoutParams);
        addView(this.imageGLSurfaceView, layoutParams3);
        addView(this.brushDrawingView, layoutParams2);
    }


    public void setImageSource(final Bitmap bitmap) {
        this.filterImageView.setImageBitmap(bitmap);
        if (this.imageGLSurfaceView.getImageHandler() != null) {
            this.imageGLSurfaceView.setImageBitmap(bitmap);
        } else {
            this.imageGLSurfaceView.setSurfaceCreatedCallback(new ImageGLSurfaceView.OnSurfaceCreatedCallback() {
                public void surfaceCreated() {
                    PhotoEditorView.this.imageGLSurfaceView.setImageBitmap(bitmap);
                }
            });
        }
        this.currentBitmap = bitmap;
        bitmaplist.add(bitmap);
        index++;
    }

    public void setImageSourceUndoRedo(final Bitmap bitmap) {
        this.filterImageView.setImageBitmap(bitmap);
        if (this.imageGLSurfaceView.getImageHandler() != null) {
            this.imageGLSurfaceView.setImageBitmap(bitmap);
        } else {
            this.imageGLSurfaceView.setSurfaceCreatedCallback(new ImageGLSurfaceView.OnSurfaceCreatedCallback() {
                public void surfaceCreated() {
                    PhotoEditorView.this.imageGLSurfaceView.setImageBitmap(bitmap);
                }
            });
        }
        this.currentBitmap = bitmap;
    }

    public void setImageSource(Bitmap bitmap, ImageGLSurfaceView.OnSurfaceCreatedCallback onSurfaceCreatedCallback) {
        this.filterImageView.setImageBitmap(bitmap);
        if (this.imageGLSurfaceView.getImageHandler() != null) {
            this.imageGLSurfaceView.setImageBitmap(bitmap);
        } else {
            this.imageGLSurfaceView.setSurfaceCreatedCallback(onSurfaceCreatedCallback);
        }
        this.currentBitmap = bitmap;
    }

    public boolean undo() {
        Log.d("TAG", "undo: "+index);

        if (index > 0) {
            setImageSourceUndoRedo(bitmaplist.get(--index));
            return true;

        }

        return false;
    }

    public boolean redo() {
        Log.d("TAG", "redo: "+index);

        if (index+1<bitmaplist.size()) {

            setImageSourceUndoRedo(bitmaplist.get(++index));
            return true;


        }

        return false;
    }

    public Bitmap getCurrentBitmap() {
        return this.currentBitmap;
    }


    public BrushDrawingView getBrushDrawingView() {
        return this.brushDrawingView;
    }

    public ImageGLSurfaceView getGLSurfaceView() {
        return this.imageGLSurfaceView;
    }

    public void saveGLSurfaceViewAsBitmap(@NonNull final OnSaveBitmap onSaveBitmap) {
        if (this.imageGLSurfaceView.getVisibility() == VISIBLE) {
            this.imageGLSurfaceView.getResultBitmap(new ImageGLSurfaceView.QueryResultBitmapCallback() {
                public void get(Bitmap bitmap) {
                    onSaveBitmap.onBitmapReady(bitmap);
                }
            });
        }
    }

    public void setFilterEffect(String str) {
        this.imageGLSurfaceView.setFilterWithConfig(str);
    }

    public void setFilterIntensity(float f) {
        this.imageGLSurfaceView.setFilterIntensity(f);
    }
}