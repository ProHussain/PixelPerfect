package com.example.pixelperfect.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.pixelperfect.BuildConfig;
import com.example.pixelperfect.R;
import com.example.pixelperfect.Utils.CaptureManager;
import com.example.pixelperfect.Utils.Constants;
import com.example.pixelperfect.databinding.ActivityHomeBinding;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final int IMAGE_GALLERY_REQUEST = 20;
    private CaptureManager captureManager;
    ImageView imgSetting;
    LinearLayout editorLayout, cameraLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.fullScreen(this);
        setContentView(R.layout.activity_home);
        init();
    }

    private void init() {
        editorLayout = findViewById(R.id.relativeLayoutEditor);
        cameraLayout = findViewById(R.id.relativeLayoutCamera);
        imgSetting = findViewById(R.id.imageViewSettings);
        captureManager = new CaptureManager(this);
        imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettings();
            }
        });

        editorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPhoto();
            }
        });

        cameraLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
    }

    private void openCamera() {
        String[] cameraPermisscions = {"android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
        if (Build.VERSION.SDK_INT >= 29)cameraPermisscions=new String[]{"android.permission.CAMERA"};
        Dexter.withContext(HomeActivity.this).withPermissions(cameraPermisscions)
                .withListener(new MultiplePermissionsListener() {
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            takePhotoWithCamera();
                        }
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            permissionDeniedDialog();
                        }
                    }

                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).withErrorListener(dexterError -> Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show()).onSameThread().check();
    }

    private void editPhoto() {
        String[] editPermissions = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
        if (Build.VERSION.SDK_INT >= 29) {
            editPermissions=new String[]{"android.permission.READ_EXTERNAL_STORAGE"};
        }
        Dexter.withContext(HomeActivity.this).withPermissions(editPermissions).withListener(new MultiplePermissionsListener() {
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                if (multiplePermissionsReport.areAllPermissionsGranted()) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    String[] mimetypes = {"image/*"};
                    intent.putExtra(Intent.EXTRA_MIME_TYPES,  mimetypes);
                    startActivityForResult(intent, IMAGE_GALLERY_REQUEST);
                }

                if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                    permissionDeniedDialog();
                }
            }

            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }

        }).withErrorListener(dexterError -> Toast.makeText(HomeActivity.this, "Error occurred! ", Toast.LENGTH_SHORT).show()).onSameThread().check();
    }

    private void permissionDeniedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GO SETTINGS", (dialogInterface, i) -> {
            dialogInterface.cancel();
            openDetails();
        });
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel());
        builder.show();
    }

    private void openDetails() {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", BuildConfig.APPLICATION_ID,  null));
        startActivityForResult(intent, 101);
    }

    public void takePhotoWithCamera() {
        try {
            startActivityForResult(this.captureManager.dispatchTakePictureIntent(), 1);
        } catch (IOException | ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != -1) {
            super.onActivityResult(requestCode, resultCode, data);
        } else if (data != null) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                Intent intent2 = new Intent(getApplicationContext(), PhotoEditorActivity.class);
                intent2.putExtra(Constants.KEY_SELECTED_PHOTOS, getRealPathFromURI(imageUri));
                startActivity(intent2);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else if (requestCode == 1){
            if (this.captureManager == null) {
                this.captureManager = new CaptureManager(this);
            }
            Intent intent2 = new Intent(getApplicationContext(), PhotoEditorActivity.class);
            intent2.putExtra(Constants.KEY_SELECTED_PHOTOS, this.captureManager.getCurrentPhotoPath());
            startActivity(intent2);
        }
    }

    private void openSettings() {
        startActivity(new Intent(HomeActivity.this,SettingsActivity.class));
    }
}