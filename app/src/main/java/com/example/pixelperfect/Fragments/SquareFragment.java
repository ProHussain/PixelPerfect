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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pixelperfect.Adapters.SquareAdapter;
import com.example.pixelperfect.Assets.FilterFileAsset;
import com.example.pixelperfect.Assets.StickerFileAsset;
import com.example.pixelperfect.R;
import com.example.pixelperfect.Editor.SquareView;
import com.example.pixelperfect.Sticker.SplashSticker;

public class SquareFragment extends DialogFragment implements SquareAdapter.SplashChangeListener {
    private static final String TAG = "SplashFragment";
    private ImageView imageViewBackground;
    public Bitmap bitmap;
    private Bitmap BlurBitmap;
    private FrameLayout frameLayout;
    public boolean isSplashView;
    public RecyclerView recyclerViewBlur;
    public TextView textVewTitle;
    public SplashDialogListener blurSquareBgListener;
    private SplashSticker polishSplashSticker;
    public SquareView polishSplashView;
    private ViewGroup viewGroup;
    TextView textViewValue;
    private SeekBar seekbar_brush;
    public interface SplashDialogListener {
        void onSaveBlurBackground(Bitmap bitmap);
    }

    public void setPolishSplashView(boolean z) {
        this.isSplashView = z;
    }

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }

    public static SquareFragment show(@NonNull AppCompatActivity appCompatActivity, Bitmap bitmap2, Bitmap bitmap3, Bitmap bitmap4, SplashDialogListener blurSquareBgListener, boolean z) {
        SquareFragment splashDialog = new SquareFragment();
        splashDialog.setBitmap(bitmap2);
        splashDialog.setBlurBitmap(bitmap4);
        splashDialog.setBlurSquareBgListener(blurSquareBgListener);
        splashDialog.setPolishSplashView(z);
        splashDialog.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return splashDialog;
    }

    public void setBlurBitmap(Bitmap bitmap2) {
        this.BlurBitmap = bitmap2;
    }


    public void setBlurSquareBgListener(SplashDialogListener blurSquareBgListener) {
        this.blurSquareBgListener = blurSquareBgListener;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
    }

    @SuppressLint("WrongConstant")
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup2, @Nullable Bundle bundle) {
        getDialog().getWindow().requestFeature(1);
        getDialog().getWindow().setFlags(1024, 1024);
        View inflate = layoutInflater.inflate(R.layout.fragment_square, viewGroup2, false);
        this.viewGroup = viewGroup2;
        this.imageViewBackground = inflate.findViewById(R.id.image_view_background);
        this.polishSplashView = inflate.findViewById(R.id.splash_view);
        this.frameLayout = inflate.findViewById(R.id.frame_layout);
        this.textViewValue = inflate.findViewById(R.id.textViewValue);
        this.imageViewBackground.setImageBitmap(this.bitmap);
        this.textVewTitle = inflate.findViewById(R.id.textViewTitle);
        if (this.isSplashView) {
            this.polishSplashView.setImageBitmap(this.BlurBitmap);
        }
        this.recyclerViewBlur = inflate.findViewById(R.id.recycler_view_splash);
        this.recyclerViewBlur.setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
        this.recyclerViewBlur.setHasFixedSize(true);
        this.recyclerViewBlur.setAdapter(new SquareAdapter(getContext(), this, this.isSplashView));
        if (this.isSplashView) {
            this.polishSplashSticker = new SplashSticker(StickerFileAsset.loadBitmapFromAssets(getContext(), "blur/image_mask_1.webp"), StickerFileAsset.loadBitmapFromAssets(getContext(), "blur/image_frame_1.webp"));
            this.polishSplashView.addSticker(this.polishSplashSticker);
        }

        this.seekbar_brush = inflate.findViewById(R.id.seekbar_brush);
        this.seekbar_brush.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                new LoadBlurBitmap((float) i).execute(new Void[0]);
                String value = String.valueOf(i);
                textViewValue.setText(value);
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        this.polishSplashView.refreshDrawableState();
        this.polishSplashView.setLayerType(2, null);
        this.textVewTitle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                polishSplashView.setCurrentSplashMode(0);
                recyclerViewBlur.setVisibility(View.VISIBLE);
                polishSplashView.refreshDrawableState();
                polishSplashView.invalidate();
            }
        });
        inflate.findViewById(R.id.image_view_save).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                blurSquareBgListener.onSaveBlurBackground(polishSplashView.getBitmap(bitmap));
                dismiss();
            }
        });
        inflate.findViewById(R.id.image_view_close).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dismiss();
            }
        });
        return inflate;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
    }

    class LoadBlurBitmap extends AsyncTask<Void, Bitmap, Bitmap> {
        private float intensity;

        public LoadBlurBitmap(float f) {
            this.intensity = f;
        }

        public void onPreExecute() {
        }

        public Bitmap doInBackground(Void... voidArr) {
            return FilterFileAsset.getBlurImageFromBitmap(SquareFragment.this.bitmap, this.intensity);
        }

        public void onPostExecute(Bitmap bitmap) {
            polishSplashView.setImageBitmap(bitmap);
        }
    }


    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(-16777216));
        }
    }

    public void onDestroy() {
        super.onDestroy();
        this.polishSplashView.getSticker().release();
        if (this.BlurBitmap != null) {
            this.BlurBitmap.recycle();
        }
        this.BlurBitmap = null;
        this.bitmap = null;
    }

    public void onSelected(SplashSticker splashSticker2) {
        this.polishSplashView.addSticker(splashSticker2);
    }

}
