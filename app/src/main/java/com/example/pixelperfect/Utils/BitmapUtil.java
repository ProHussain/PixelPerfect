package com.example.pixelperfect.Utils;

import android.graphics.Bitmap;

public class BitmapUtil {
    BitmapUtil() {
    }

    public static Bitmap removeTransparency(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] iArr = new int[(bitmap.getWidth() * bitmap.getHeight())];
        bitmap.getPixels(iArr, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        int i = 0;
        int i2 = 0;
        loop0:
        while (true) {
            if (i2 >= bitmap.getWidth()) {
                i2 = 0;
                break;
            }
            for (int i3 = 0; i3 < bitmap.getHeight(); i3++) {
                if (iArr[(bitmap.getWidth() * i3) + i2] != 0) {
                    break loop0;
                }
            }
            i2++;
        }
        int i4 = 0;
        loop2:
        while (true) {
            if (i4 >= bitmap.getHeight()) {
                break;
            }
            for (int i5 = i2; i5 < bitmap.getHeight(); i5++) {
                if (iArr[(bitmap.getWidth() * i4) + i5] != 0) {
                    i = i4;
                    break loop2;
                }
            }
            i4++;
        }
        int width2 = bitmap.getWidth() - 1;
        loop4:
        while (true) {
            if (width2 < i2) {
                break;
            }
            for (int height2 = bitmap.getHeight() - 1; height2 >= i; height2--) {
                if (iArr[(bitmap.getWidth() * height2) + width2] != 0) {
                    width = width2;
                    break loop4;
                }
            }
            width2--;
        }
        int height3 = bitmap.getHeight() - 1;
        loop6:
        while (true) {
            if (height3 < i) {
                break;
            }
            for (int width3 = bitmap.getWidth() - 1; width3 >= i2; width3--) {
                if (iArr[(bitmap.getWidth() * height3) + width3] != 0) {
                    height = height3;
                    break loop6;
                }
            }
            height3--;
        }
        return Bitmap.createBitmap(bitmap, i2, i, width - i2, height - i);
    }


}
