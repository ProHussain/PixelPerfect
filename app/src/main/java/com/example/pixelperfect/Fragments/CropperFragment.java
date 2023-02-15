package com.example.pixelperfect.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pixelperfect.Adapters.AspectAdapter;
import com.example.pixelperfect.R;
import com.github.flipzeus.FlipDirection;
import com.github.flipzeus.ImageFlipper;
import com.isseiaoki.simplecropview.CropImageView;
import com.steelkiwi.cropiwa.AspectRatio;

public class CropperFragment extends DialogFragment implements AspectAdapter.OnNewSelectedListener {
    private static final String TAG = "CropFragment";
    private Bitmap bitmap;
    public CropImageView crop_image_view;
    public OnCropPhoto onCropPhoto;
    private RelativeLayout relative_layout_loading;
    public interface OnCropPhoto {
        void finishCrop(Bitmap bitmap);
    }

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }

    public static CropperFragment show(@NonNull AppCompatActivity appCompatActivity, OnCropPhoto onCropPhoto2, Bitmap bitmap2) {
        CropperFragment cropDialogFragment = new CropperFragment();
        cropDialogFragment.setBitmap(bitmap2);
        cropDialogFragment.setOnCropPhoto(onCropPhoto2);
        cropDialogFragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return cropDialogFragment;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    public void setOnCropPhoto(OnCropPhoto onCropPhoto2) {
        this.onCropPhoto = onCropPhoto2;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(-16777216));
        }
    }

    @SuppressLint("WrongConstant")
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        getDialog().getWindow().requestFeature(1);
        getDialog().getWindow().setFlags(1024, 1024);
        View inflate = layoutInflater.inflate(R.layout.fragment_cropper, viewGroup, false);
        AspectAdapter aspectRatioPreviewAdapter = new AspectAdapter();
        aspectRatioPreviewAdapter.setListener(this);
        RecyclerView recycler_view_ratio = inflate.findViewById(R.id.recycler_view_ratio);
        recycler_view_ratio.setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
        recycler_view_ratio.setAdapter(aspectRatioPreviewAdapter);
        this.crop_image_view = inflate.findViewById(R.id.crop_image_view);
        this.crop_image_view.setCropMode(CropImageView.CropMode.FREE);
        inflate.findViewById(R.id.imageViewRotateLeft).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CropperFragment.this.crop_image_view.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D);
            }
        });
        inflate.findViewById(R.id.imageViewRotateRight).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CropperFragment.this.crop_image_view.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
            }
        });
        inflate.findViewById(R.id.imageViewFlipV).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ImageFlipper.flip(crop_image_view, FlipDirection.VERTICAL);
            }
        });

        inflate.findViewById(R.id.imageViewFlipH).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ImageFlipper.flip(crop_image_view, FlipDirection.HORIZONTAL);
            }
        });
        inflate.findViewById(R.id.imageViewSaveCrop).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new OnSaveCrop().execute(new Void[0]);
            }
        });
        this.relative_layout_loading = inflate.findViewById(R.id.relative_layout_loading);
        this.relative_layout_loading.setVisibility(View.GONE);
        inflate.findViewById(R.id.imageViewCloseCrop).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CropperFragment.this.dismiss();
            }
        });
        return inflate;
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.crop_image_view = view.findViewById(R.id.crop_image_view);
        this.crop_image_view.setImageBitmap(this.bitmap);
    }

    public void onNewAspectRatioSelected(AspectRatio aspectRatio) {
        if (aspectRatio.getWidth() == 10 && aspectRatio.getHeight() == 10) {
            this.crop_image_view.setCropMode(CropImageView.CropMode.FREE);
        } else {
            this.crop_image_view.setCustomRatio(aspectRatio.getWidth(), aspectRatio.getHeight());
        }
    }

    class OnSaveCrop extends AsyncTask<Void, Bitmap, Bitmap> {
        OnSaveCrop() {
        }

        public void onPreExecute() {
            CropperFragment.this.mLoading(true);
        }

        public Bitmap doInBackground(Void... voidArr) {
            return CropperFragment.this.crop_image_view.getCroppedBitmap();
        }

        public void onPostExecute(Bitmap bitmap) {
            CropperFragment.this.mLoading(false);
            CropperFragment.this.onCropPhoto.finishCrop(bitmap);
            CropperFragment.this.dismiss();
        }
    }

    public void mLoading(boolean z) {
        if (z) {
            getActivity().getWindow().setFlags(16, 16);
            this.relative_layout_loading.setVisibility(View.VISIBLE);
            return;
        }
        getActivity().getWindow().clearFlags(16);
        this.relative_layout_loading.setVisibility(View.GONE);
    }
}