package com.example.pixelperfect.Utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.example.pixelperfect.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SaveFileUtils {

    public static File FolderPathShow = new File(Environment.getExternalStorageDirectory() + "/Pictures");

    public static void createFolder() {
        if (!FolderPathShow.exists()) {
            FolderPathShow.mkdirs();
        }
    }


    public static void shareImage(Context context, String filePath) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, context.getResources().getString(R.string.share_txt));
            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), filePath, "", null);
            Uri screenshotUri = Uri.parse(path);
            intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
            intent.setType("image/*");
            context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string.share_image_via)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static boolean isExternalStorageWritable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static File saveBitmapFileEditor(Context context, Bitmap bitmap, String name, String dir) throws IOException {

        if (Build.VERSION.SDK_INT >= 29) {
            boolean isWritable=isExternalStorageWritable();
            String imagesRelPath=null;
            if(isWritable) {
                FileOutputStream fos = null;
                try {
                    String relativePath = dir != null ? Environment.DIRECTORY_PICTURES + File.separator + dir : Environment.DIRECTORY_PICTURES;
                    ContentResolver resolver = context.getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
                    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath);

                    Uri contUri= resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    imagesRelPath = FilePathUtil.getPath(context,contUri);
                    fos = (FileOutputStream) resolver.openOutputStream(contUri);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                } catch (Exception e) {
                    String err = e.getMessage();
                    Log.e("File save exception", err);
                } finally {
                    if (fos != null) fos.close();
                }
                return imagesRelPath!=null?new File(imagesRelPath):null;
            }
            return null;

        }
        String imagesDir2 = Environment.getExternalStorageDirectory().toString() + File.separator + Environment.DIRECTORY_PICTURES;
        File file2 = new File(imagesDir2);
        if (!file2.exists()) {
            file2.mkdir();
        }

        File image = new File(imagesDir2, name + ".jpg");
        OutputStream fos2 = new FileOutputStream(image);
        MediaScannerConnection.scanFile(context, new String[]{image.getAbsolutePath()}, (String[]) null, new MediaScannerConnection.MediaScannerConnectionClient() {
            public void onMediaScannerConnected() {
            }

            public void onScanCompleted(String path, Uri uri) {
            }
        });
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos2);
        fos2.flush();
        fos2.close();
        return image;
    }

}
