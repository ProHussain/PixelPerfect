package com.example.pixelperfect.Sticker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

class StickerUtils {

    public static void notifySystemGallery(@NonNull Context paramContext, @NonNull File paramFile) {
        if (paramFile != null && paramFile.exists())
            try {
                MediaStore.Images.Media.insertImage(paramContext.getContentResolver(), paramFile.getAbsolutePath(), paramFile.getName(), null);
                paramContext.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(paramFile)));
                return;
            } catch (FileNotFoundException fileNotFoundException) {
                throw new IllegalStateException("File couldn't be found");
            }
        throw new IllegalArgumentException("bmp should not be null");
    }

    public static File saveImageToGallery(@NonNull File paramFile, @NonNull Bitmap paramBitmap) {
        if (paramBitmap != null) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(paramFile);
                paramBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException iOException) {
                iOException.printStackTrace();
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("saveImageToGallery: the path of bmp is ");
            stringBuilder.append(paramFile.getAbsolutePath());
            Log.e("StickerView", stringBuilder.toString());
            return paramFile;
        }
        throw new IllegalArgumentException("bmp should not be null");
    }


    public static void trapToRect(@NonNull RectF paramRectF, @NonNull float[] paramArrayOffloat) {
        paramRectF.set(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
        int i;
        for (i = 1; i < paramArrayOffloat.length; i += 2) {
            float f3;
            float f2 = Math.round(paramArrayOffloat[i - 1] * 10.0F) / 10.0F;
            float f1 = Math.round(paramArrayOffloat[i] * 10.0F) / 10.0F;
            if (f2 < paramRectF.left) {
                f3 = f2;
            } else {
                f3 = paramRectF.left;
            }
            paramRectF.left = f3;
            if (f1 < paramRectF.top) {
                f3 = f1;
            } else {
                f3 = paramRectF.top;
            }
            paramRectF.top = f3;
            if (f2 <= paramRectF.right)
                f2 = paramRectF.right;
            paramRectF.right = f2;
            if (f1 <= paramRectF.bottom)
                f1 = paramRectF.bottom;
            paramRectF.bottom = f1;
        }
        paramRectF.sort();
    }
}
