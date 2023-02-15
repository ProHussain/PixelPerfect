package com.example.pixelperfect.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pixelperfect.Adapters.AspectAdapter;
import com.example.pixelperfect.Adapters.BorderRatioAdapter;
import com.example.pixelperfect.Adapters.ColorRatioAdapter;
import com.example.pixelperfect.Adapters.GradientRatioAdapter;
import com.example.pixelperfect.Assets.FilterFileAsset;
import com.example.pixelperfect.R;
import com.example.pixelperfect.Utils.SystemUtil;
import com.steelkiwi.cropiwa.AspectRatio;

public class RatioFragment extends DialogFragment implements AspectAdapter.OnNewSelectedListener,
        GradientRatioAdapter.BackgroundInstaListener, ColorRatioAdapter.BackgroundColorListener, BorderRatioAdapter.BackgroundBorderListener {
    private static final String TAG = "RatioFragment";
    private RelativeLayout relative_layout_loading;
    private Bitmap bitmap;
    private Bitmap blurBitmap;
    private ImageView image_view_blur;
    private AspectRatio aspectRatio;
    public RecyclerView recycler_view_ratio;
    public RecyclerView recycler_view_color;
    public RatioSaveListener ratioSaveListener;
    public RecyclerView constraint_layout_padding;
    public ImageView image_view_ratio;
    private ConstraintLayout constraint_layout_ratio;
    public RecyclerView recycler_view_background;
    public FrameLayout frame_layout_wrapper;

    TextView textViewValue;
    ImageView imageViewCrop;
    ImageView imageViewGradient;
    ImageView imageViewBorder;
    ImageView imageViewColor;

    public interface RatioSaveListener {
        void ratioSavedBitmap(Bitmap bitmap);
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public static RatioFragment show(@NonNull AppCompatActivity appCompatActivity, RatioSaveListener ratioSaveListener, Bitmap mBitmap, Bitmap iBitmap) {
        RatioFragment ratioFragment = new RatioFragment();
        ratioFragment.setBitmap(mBitmap);
        ratioFragment.setBlurBitmap(iBitmap);
        ratioFragment.setRatioSaveListener(ratioSaveListener);
        ratioFragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return ratioFragment;
    }

    public void setBlurBitmap(Bitmap bitmap2) {
        this.blurBitmap = bitmap2;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
    }

    public void setRatioSaveListener(RatioSaveListener iRatioSaveListener) {
        this.ratioSaveListener = iRatioSaveListener;
    }

    @SuppressLint("WrongConstant")
    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        getDialog().getWindow().requestFeature(1);
        getDialog().getWindow().setFlags(1024, 1024);
        View inflate = layoutInflater.inflate(R.layout.fragment_ratio, viewGroup, false);
        AspectAdapter aspectRatioPreviewAdapter = new AspectAdapter(true);
        aspectRatioPreviewAdapter.setListener(this);
        this.relative_layout_loading = inflate.findViewById(R.id.relative_layout_loading);
        this.relative_layout_loading.setVisibility(View.GONE);
        this.recycler_view_ratio = inflate.findViewById(R.id.recycler_view_ratio);
        this.recycler_view_ratio.setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
        this.recycler_view_ratio.setAdapter(aspectRatioPreviewAdapter);
        this.aspectRatio = new AspectRatio(1, 1);
        this.recycler_view_background = inflate.findViewById(R.id.recycler_view_background);
        this.recycler_view_background.setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
        this.recycler_view_background.setAdapter(new GradientRatioAdapter(getContext(), this));

        this.recycler_view_color = inflate.findViewById(R.id.recycler_view_color);
        this.recycler_view_color.setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
        this.recycler_view_color.setAdapter(new ColorRatioAdapter(getContext(), this));

        this.textViewValue = inflate.findViewById(R.id.textViewValue);
        this.imageViewCrop = inflate.findViewById(R.id.imageViewCrop);
        this.imageViewGradient = inflate.findViewById(R.id.imageViewGradient);
        this.constraint_layout_padding = inflate.findViewById(R.id.recycler_vew_border);
        this.imageViewBorder = inflate.findViewById(R.id.imageViewBorder);
        this.imageViewColor = inflate.findViewById(R.id.imageViewColor);

        this.imageViewCrop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                recycler_view_ratio.setVisibility(View.VISIBLE);
                recycler_view_background.setVisibility(View.GONE);
                constraint_layout_padding.setVisibility(View.GONE);
                recycler_view_color.setVisibility(View.GONE);
                imageViewCrop.setBackgroundResource(R.drawable.background_selected_color);
                imageViewCrop.setColorFilter(getResources().getColor(R.color.mainColor));
                imageViewGradient.setBackgroundResource(R.drawable.background_unslelected);
                imageViewGradient.setColorFilter(getResources().getColor(R.color.white));
                imageViewBorder.setBackgroundResource(R.drawable.background_unslelected);
                imageViewBorder.setColorFilter(getResources().getColor(R.color.white));
                imageViewColor.setBackgroundResource(R.drawable.background_unslelected);
                imageViewColor.setColorFilter(getResources().getColor(R.color.white));
            }
        });

        this.imageViewGradient.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                recycler_view_ratio.setVisibility(View.GONE);
                recycler_view_background.setVisibility(View.VISIBLE);
                constraint_layout_padding.setVisibility(View.GONE);
                recycler_view_color.setVisibility(View.GONE);
                imageViewCrop.setBackgroundResource(R.drawable.background_unslelected);
                imageViewCrop.setColorFilter(getResources().getColor(R.color.white));
                imageViewGradient.setBackgroundResource(R.drawable.background_selected_color);
                imageViewGradient.setColorFilter(getResources().getColor(R.color.mainColor));
                imageViewBorder.setBackgroundResource(R.drawable.background_unslelected);
                imageViewBorder.setColorFilter(getResources().getColor(R.color.white));
                imageViewColor.setBackgroundResource(R.drawable.background_unslelected);
                imageViewColor.setColorFilter(getResources().getColor(R.color.white));
            }
        });

        this.imageViewBorder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                constraint_layout_padding.setVisibility(View.VISIBLE);
                recycler_view_ratio.setVisibility(View.GONE);
                recycler_view_background.setVisibility(View.GONE);
                recycler_view_color.setVisibility(View.GONE);
                imageViewCrop.setBackgroundResource(R.drawable.background_unslelected);
                imageViewCrop.setColorFilter(getResources().getColor(R.color.white));
                imageViewGradient.setBackgroundResource(R.drawable.background_unslelected);
                imageViewGradient.setColorFilter(getResources().getColor(R.color.white));
                imageViewBorder.setBackgroundResource(R.drawable.background_selected_color);
                imageViewBorder.setColorFilter(getResources().getColor(R.color.mainColor));
                imageViewColor.setBackgroundResource(R.drawable.background_unslelected);
                imageViewColor.setColorFilter(getResources().getColor(R.color.white));
            }
        });

        this.imageViewColor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                recycler_view_color.setVisibility(View.VISIBLE);
                constraint_layout_padding.setVisibility(View.GONE);
                recycler_view_ratio.setVisibility(View.GONE);
                recycler_view_background.setVisibility(View.GONE);
                imageViewCrop.setBackgroundResource(R.drawable.background_unslelected);
                imageViewCrop.setColorFilter(getResources().getColor(R.color.white));
                imageViewGradient.setBackgroundResource(R.drawable.background_unslelected);
                imageViewGradient.setColorFilter(getResources().getColor(R.color.white));
                imageViewBorder.setBackgroundResource(R.drawable.background_unslelected);
                imageViewBorder.setColorFilter(getResources().getColor(R.color.white));
                imageViewColor.setBackgroundResource(R.drawable.background_selected_color);
                imageViewColor.setColorFilter(getResources().getColor(R.color.mainColor));
            }
        });


        constraint_layout_padding.setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
        constraint_layout_padding.setHasFixedSize(true);
        constraint_layout_padding.setAdapter(new BorderRatioAdapter(getContext(), this));
        ((SeekBar) inflate.findViewById(R.id.seekbarPadding)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                int dpToPx = SystemUtil.dpToPx(RatioFragment.this.getContext(), i);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) RatioFragment.this.image_view_ratio.getLayoutParams();
                layoutParams.setMargins(dpToPx, dpToPx, dpToPx, dpToPx);
                RatioFragment.this.image_view_ratio.setLayoutParams(layoutParams);
                String value = String.valueOf(i);
                textViewValue.setText(value);
            }
        });
        this.image_view_ratio = inflate.findViewById(R.id.image_view_ratio);
        this.image_view_ratio.setImageBitmap(this.bitmap);
        this.image_view_ratio.setAdjustViewBounds(true);
        Display defaultDisplay = getActivity().getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        this.constraint_layout_ratio = inflate.findViewById(R.id.constraint_layout_ratio);
        this.image_view_blur = inflate.findViewById(R.id.image_view_blur);
        this.image_view_blur.setImageBitmap(this.blurBitmap);
        this.frame_layout_wrapper = inflate.findViewById(R.id.frame_layout_wrapper);
        this.frame_layout_wrapper.setLayoutParams(new ConstraintLayout.LayoutParams(point.x, point.x));
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.constraint_layout_ratio);
        ConstraintSet constraintSet2 = constraintSet;
        constraintSet2.connect(this.frame_layout_wrapper.getId(), 3, this.constraint_layout_ratio.getId(), 3, 0);
        constraintSet2.connect(this.frame_layout_wrapper.getId(), 1, this.constraint_layout_ratio.getId(), 1, 0);
        constraintSet2.connect(this.frame_layout_wrapper.getId(), 4, this.constraint_layout_ratio.getId(), 4, 0);
        constraintSet2.connect(this.frame_layout_wrapper.getId(), 2, this.constraint_layout_ratio.getId(), 2, 0);
        constraintSet.applyTo(this.constraint_layout_ratio);
        inflate.findViewById(R.id.image_view_close).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                RatioFragment.this.dismiss();
            }
        });
        inflate.findViewById(R.id.image_view_save).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new SaveRatioView().execute(getBitmapFromView(RatioFragment.this.frame_layout_wrapper));
            }
        });
        return inflate;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(-16777216));
        }
    }

    public void onStop() {
        super.onStop();
    }

    private int[] calculateWidthAndHeight(AspectRatio aspectRatio, Point point) {
        int height = this.constraint_layout_ratio.getHeight();
        if (aspectRatio.getHeight() > aspectRatio.getWidth()) {
            int mRatio = (int) (aspectRatio.getRatio() * ((float) height));
            if (mRatio < point.x) {
                return new int[]{mRatio, height};
            }
            return new int[]{point.x, (int) (((float) point.x) / aspectRatio.getRatio())};
        }
        int iRatio = (int) (((float) point.x) / aspectRatio.getRatio());
        if (iRatio > height) {
            return new int[]{(int) (((float) height) * aspectRatio.getRatio()), height};
        }
        return new int[]{point.x, iRatio};
    }


    public void onNewAspectRatioSelected(AspectRatio aspectRatio) {
        Display defaultDisplay = getActivity().getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        this.aspectRatio = aspectRatio;
        int[] calculateWidthAndHeight = calculateWidthAndHeight(aspectRatio, point);
        this.frame_layout_wrapper.setLayoutParams(new ConstraintLayout.LayoutParams(calculateWidthAndHeight[0], calculateWidthAndHeight[1]));
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.constraint_layout_ratio);
        ConstraintSet constraintSet2 = constraintSet;
        constraintSet2.connect(this.frame_layout_wrapper.getId(), 3, this.constraint_layout_ratio.getId(), 3, 0);
        constraintSet2.connect(this.frame_layout_wrapper.getId(), 1, this.constraint_layout_ratio.getId(), 1, 0);
        constraintSet2.connect(this.frame_layout_wrapper.getId(), 4, this.constraint_layout_ratio.getId(), 4, 0);
        constraintSet2.connect(this.frame_layout_wrapper.getId(), 2, this.constraint_layout_ratio.getId(), 2, 0);
        constraintSet.applyTo(this.constraint_layout_ratio);
    }

    class SaveRatioView extends AsyncTask<Bitmap, Bitmap, Bitmap> {
        SaveRatioView() {
        }

        public void onPreExecute() {
            RatioFragment.this.mLoading(true);
        }

        public Bitmap doInBackground(Bitmap... bitmapArr) {
            Bitmap cloneBitmap = FilterFileAsset.cloneBitmap(bitmapArr[0]);
            bitmapArr[0].recycle();
            bitmapArr[0] = null;
            return cloneBitmap;
        }

        public void onPostExecute(Bitmap bitmap) {
            RatioFragment.this.mLoading(false);
            RatioFragment.this.ratioSaveListener.ratioSavedBitmap(bitmap);
            RatioFragment.this.dismiss();
        }
    }

    @Override
    public void onBackgroundColorSelected(int item, ColorRatioAdapter.SquareView squareView) {
        if (squareView.isColor) {
            this.frame_layout_wrapper.setBackgroundColor(squareView.drawableId);
            this.image_view_blur.setVisibility(View.GONE);
        } else if (squareView.text.equals("Blur")) {
            this.image_view_blur.setVisibility(View.VISIBLE);
        } else {
            this.frame_layout_wrapper.setBackgroundResource(squareView.drawableId);
            this.image_view_blur.setVisibility(View.GONE);
        }
        this.frame_layout_wrapper.invalidate();
    }

    public void onBackgroundSelected(int item, GradientRatioAdapter.SquareView squareView) {
         if (squareView.isGradient) {
            this.frame_layout_wrapper.setBackgroundColor(squareView.drawableId);

        } else if (squareView.text.equals("Blur")) {
            this.image_view_blur.setVisibility(View.VISIBLE);
        } else {
            this.frame_layout_wrapper.setBackgroundResource(squareView.drawableId);
            this.image_view_blur.setVisibility(View.GONE);
        }
        this.frame_layout_wrapper.invalidate();

    }

    public void onDestroyView() {
        super.onDestroyView();
        if (this.blurBitmap != null) {
            this.blurBitmap.recycle();
            this.blurBitmap = null;
        }
        this.bitmap = null;
    }

    @Override
    public void onBackgroundBorderSelected(int item, BorderRatioAdapter.SquareView squareView) {
        if (squareView.isColor) {
            this.image_view_ratio.setBackgroundColor(squareView.drawableId);
            int dpToPx = SystemUtil.dpToPx(getContext(), 3);
            this.image_view_ratio.setPadding(dpToPx, dpToPx, dpToPx, dpToPx);
            return;
        } else if (squareView.text.equals("Blur")) {
            this.image_view_ratio.setPadding(0, 0, 0, 0);
        } else {
            int dpToPx = SystemUtil.dpToPx(getContext(), 3);
            this.image_view_ratio.setPadding(dpToPx, dpToPx, dpToPx, dpToPx);
            this.image_view_ratio.setBackgroundColor(squareView.drawableId);
            return;
        }
        this.frame_layout_wrapper.invalidate();

    }

    public Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
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
