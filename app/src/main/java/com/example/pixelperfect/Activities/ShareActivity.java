package com.example.pixelperfect.Activities;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import com.bumptech.glide.Glide;
import com.example.pixelperfect.R;
import com.example.pixelperfect.Utils.CaptureManager;
import com.example.pixelperfect.Utils.Constants;
import com.example.pixelperfect.Utils.PreferenceUtil;
import com.example.pixelperfect.Utils.RateDialog;

import java.io.File;

public class ShareActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "PhotoShareActivity";
    private File files;
    ImageView imageViewPreview;
    CaptureManager createImageFile1;

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        Constants.fullScreen(this);
        setContentView((int) R.layout.activity_share);
        createImageFile1=new CaptureManager(this);

        imageViewPreview = (ImageView) findViewById(R.id.image_view_preview);

        String string = getIntent().getExtras().getString("path");
        this.files = new File(string);

        Glide.with(getApplicationContext()).load(files).into((ImageView) findViewById(R.id.image_view_preview));
        findViewById(R.id.image_view_preview).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                ShareActivity.this.onClick(view);
            }
        });

        findViewById(R.id.imageViewBack).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                onBackPressed();
            }
        });

        findViewById(R.id.imageViewHome).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            public final void onClick(View view) {
                Intent intent = new Intent(ShareActivity.this, HomeActivity.class);
                intent.setFlags(67108864);
                startActivity(intent);
                finish();
            }
        });

        if (!PreferenceUtil.isRated(this)) {
            new RateDialog(this, false).show();
        }
    }

    public void onResume() {
        super.onResume();
    }

    @SuppressLint("WrongConstant")
    public void onClick(View view) {
        if (view != null) {
            int id = view.getId();
            switch (id) {
                case R.id.linear_layout_facebook:
                    shareToFacebook(files.getPath());
                    return;
                case R.id.linear_layout_instagram:
                    shareToInstagram(files.getPath());
                    return;
                case R.id.linear_layout_share_more:
                    shareImage(files.getPath());
                    return;
                case R.id.linear_layout_twitter:
                    shareToTwitter(files.getPath());
                    return;
                case R.id.linear_layout_whatsapp:
                    sendToWhatsaApp(files.getPath());
                    return;
                case R.id.linear_layout_messenger:
                    shareToMessanger(files.getPath());
                    return;
                case R.id.image_view_preview:
                    Intent intent4 = new Intent();
                    intent4.setAction("android.intent.action.VIEW");
                    intent4.setDataAndType(FileProvider.getUriForFile(getApplicationContext(), getResources().getString(R.string.file_provider), this.files), "image/*");
                    intent4.addFlags(3);
                    startActivity(intent4);
                    return;
                default:
            }
        }
    }

    public void sendToWhatsaApp(String str) {
        String str2 = Constants.WHATSAPP;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(getApplicationContext().getPackageName());
            sb.append(".provider");
            Uri uriForFile = FileProvider.getUriForFile(this, sb.toString(), new File(str));
            Intent intent = new Intent();
            intent.setAction("android.intent.action.SEND");
            intent.setType("image/*");
            intent.putExtra("android.intent.extra.STREAM", uriForFile);
            intent.setPackage(str2);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SuppressLint("WrongConstant")
    public void shareToFacebook(String str) {
        Intent intent = new Intent("android.intent.action.SEND");
        String str2 = Constants.FACEBOOK;
        intent.setPackage(str2);
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(getApplicationContext().getPackageName());
            sb.append(".provider");
            intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(this, sb.toString(), new File(str)));
            intent.setType("image/*");
            intent.addFlags(1);
            startActivity(Intent.createChooser(intent, "Share Photo."));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    public void shareToMessanger(String str) {
        String str2 = Constants.MESSEGER;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(getApplicationContext().getPackageName());
            sb.append(".provider");
            Uri uriForFile = FileProvider.getUriForFile(this, sb.toString(), new File(str));
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("image/*");
            intent.setPackage(str2);
            intent.putExtra("android.intent.extra.STREAM", uriForFile);
            intent.addFlags(524288);
            startActivity(Intent.createChooser(intent, "Test"));
        } catch (ActivityNotFoundException unused) {
            Toast.makeText(getApplicationContext(), "You don't seem to have twitter installed on this device", Toast.LENGTH_SHORT).show();
        }
    }

    public void shareToInstagram(String str) {
        String str2 = Constants.INSTAGRAM;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(getApplicationContext().getPackageName());
            sb.append(".provider");
            Uri uriForFile = FileProvider.getUriForFile(this, sb.toString(), new File(str));
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("image/*");
            intent.putExtra("android.intent.extra.STREAM", uriForFile);
            intent.setPackage(str2);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shareToTwitter(String str) {
        String str2 = Constants.TWITTER;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(getApplicationContext().getPackageName());
            sb.append(".provider");
            Uri uriForFile = FileProvider.getUriForFile(this, sb.toString(), new File(str));
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("image/*");
            intent.putExtra("android.intent.extra.STREAM", uriForFile);
            intent.setPackage(str2);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    public void shareImage(String str) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(getApplicationContext().getPackageName());
            sb.append(".provider");
            Uri uriForFile = FileProvider.getUriForFile(this, sb.toString(), new File(str));
            Intent intent = new Intent("android.intent.action.SEND");
            intent.addFlags(524288);
            intent.setType("image/*");
            StringBuilder sb2 = new StringBuilder();
            sb2.append(getResources().getString(R.string.share_text));
            sb2.append("\nhttps://play.google.com/store/apps/details?id=");
            sb2.append(getPackageName());
            intent.putExtra("android.intent.extra.TEXT", sb2.toString());
            intent.putExtra("android.intent.extra.STREAM", uriForFile);
            startActivity(Intent.createChooser(intent, "Share via"));
        } catch (Exception e) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("shareImage: ");
            sb3.append(e);
            Log.e(TAG, sb3.toString());
        }
    }

}
