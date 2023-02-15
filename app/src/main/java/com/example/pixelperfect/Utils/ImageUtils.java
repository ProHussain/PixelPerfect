package com.example.pixelperfect.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.util.Log;

import java.io.IOException;

public class ImageUtils {



    public static Options getResampling(int i, int i2, int i3) {
        float f;
        float f2;
        Options options = new Options();
        if (i <= i2 && i2 > i) {
            f = (float) i3;
            f2 = (float) i2;
        } else {
            f = (float) i3;
            f2 = (float) i;
        }
        float f3 = f / f2;
        options.outWidth = (int) ((((float) i) * f3) + 0.5f);
        options.outHeight = (int) ((((float) i2) * f3) + 0.5f);
        return options;
    }

    public static int getClosestResampleSize(int i, int i2, int i3) {
        int max = Math.max(i, i2);
        int i4 = 1;
        while (true) {
            if (i4 >= Integer.MAX_VALUE) {
                break;
            } else if (i4 * i3 > max) {
                i4--;
                break;
            } else {
                i4++;
            }
        }
        if (i4 > 0) {
            return i4;
        }
        return 1;
    }

    public static Bitmap getBitmapFromAsset(Context context, String str) {
        try {
            return BitmapFactory.decodeStream(context.getAssets().open(str));
        } catch (IOException unused) {
            String str2 = "";
            Log.e(str2, str2);
            return null;
        }
    }

    public static Bitmap getMask(Context context, Bitmap bitmap, Bitmap bitmap2, int i, int i2) {
        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, i, i2, true);
        Bitmap createScaledBitmap2 = Bitmap.createScaledBitmap(bitmap2, i, i2, true);
        Bitmap createBitmap = Bitmap.createBitmap(createScaledBitmap2.getWidth(), createScaledBitmap2.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint(1);
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        canvas.drawBitmap(createScaledBitmap, 0.0f, 0.0f, null);
        canvas.drawBitmap(createScaledBitmap2, 0.0f, 0.0f, paint);
        paint.setXfermode(null);
        return createBitmap;
    }

    public static Bitmap getBitmapResize(Context context, Bitmap bitmap, int width, int height) {
        int bmpWidth = bitmap.getWidth();
        int bmpHeight = bitmap.getHeight();
        if (bmpWidth >= bmpHeight) {
            int heightRatio = (bmpHeight * width) / bmpWidth;
            if (heightRatio > height) {
                width = (width * height) / heightRatio;
            } else {
                height = heightRatio;
            }
        } else {
            int widthRatio = (bmpWidth * height) / bmpHeight;
            if (widthRatio > width) {
                height = (height * width) / widthRatio;
            } else {
                width = widthRatio;
            }
        }
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

}
