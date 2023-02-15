package com.example.pixelperfect.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.pixelperfect.R;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import org.wysaid.nativePort.CGENativeLibrary;

public class HSlFragment extends DialogFragment {
    private static final String TAG = "HSlFragment";
    private Bitmap bitmap;
    //public HSLSaveListener ratioSaveListener;
    public ImageView image_view_ratio;
    private IndicatorSeekBar seekbarIntensityHue;
    private IndicatorSeekBar seekbarIntensitySaturation;
    private IndicatorSeekBar seekbarIntensityLightness;
    private RadioGroup colorselection;
    private Bitmap tempbitmap,tempbitmap2;
    public OnFilterSavePhoto onFilterSavePhoto;

    public interface OnFilterSavePhoto {
        void onSaveFilter(Bitmap bitmap);
    }

    public void setOnFilterSavePhoto(OnFilterSavePhoto onFilterSavePhoto2) {
        this.onFilterSavePhoto = onFilterSavePhoto2;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public static HSlFragment show(@NonNull AppCompatActivity appCompatActivity, OnFilterSavePhoto onFilterSavePhoto2, Bitmap mBitmap) {
        HSlFragment ratioFragment = new HSlFragment();
        ratioFragment.setBitmap(mBitmap);
        ratioFragment.setOnFilterSavePhoto(onFilterSavePhoto2);
        ratioFragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return ratioFragment;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
    }

   /* public void setRatioSaveListener(HSLSaveListener iRatioSaveListener) {
        this.ratioSaveListener = iRatioSaveListener;
    }*/

    @SuppressLint("WrongConstant")
    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        getDialog().getWindow().requestFeature(1);
        getDialog().getWindow().setFlags(1024, 1024);
        View inflate = layoutInflater.inflate(R.layout.fragment_hsl, viewGroup, false);
        this.seekbarIntensityHue =inflate. findViewById(R.id.hue);
        this.seekbarIntensitySaturation =inflate. findViewById(R.id.sat);
        this.seekbarIntensityLightness =inflate. findViewById(R.id.light);
        colorselection =inflate.  findViewById(R.id.colorselection);

        this.image_view_ratio = inflate.findViewById(R.id.imageViewRatio);
        this.image_view_ratio.setImageBitmap(this.bitmap);

        inflate.findViewById(R.id.imageViewCloseRatio).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                HSlFragment.this.dismiss();
            }
        });
        inflate.findViewById(R.id.imageViewSaveRatio).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                HSlFragment.this.onFilterSavePhoto.onSaveFilter(((BitmapDrawable) HSlFragment.this.image_view_ratio.getDrawable()).getBitmap());
                HSlFragment.this.dismiss();
                //new SaveRatioView().execute(getBitmapFromView(HSlFragment.this.image_view_ratio));
            }
        });

        seekbarIntensityHue.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
//                Log.d(TAG, "onColorSelected: " + seekParams.progressFloat/100);
//                String sat="@adjust saturation 1";
//                int[] cmyk= rgbToCmyk(Color.red(i),Color.blue(i),Color.green(i));
//                 Color.HSVToColor()
//                Log.d(TAG, "blue: "+cmyk[0]+","+cmyk[1]+","+cmyk[2]+","+cmyk[3]);
//                inti =ColorUtils.HSLToColor(hsv)

                String ruleString="";
                if(colorselection.getCheckedRadioButtonId()==R.id.red){
                    ruleString = "@selcolor red("+seekbarIntensityHue.getProgressFloat()/100+","+seekbarIntensitySaturation.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+")" ;

                }else if(colorselection.getCheckedRadioButtonId()==R.id.green){
                    ruleString = "@selcolor green("+seekbarIntensityHue.getProgressFloat()/100+","+seekbarIntensitySaturation.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+")" ;

                }else if(colorselection.getCheckedRadioButtonId()==R.id.blue){
                    ruleString = "@selcolor blue("+seekbarIntensityHue.getProgressFloat()/100+","+seekbarIntensitySaturation.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+")" ;

                }else if(colorselection.getCheckedRadioButtonId()==R.id.mergenta){
                    ruleString = "@selcolor magenta("+seekbarIntensityHue.getProgressFloat()/100+","+seekbarIntensitySaturation.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+")" ;

                }else if(colorselection.getCheckedRadioButtonId()==R.id.yellow){
                    ruleString = "@selcolor yellow("+seekbarIntensityHue.getProgressFloat()/100+","+seekbarIntensitySaturation.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+")" ;

                }else if(colorselection.getCheckedRadioButtonId()==R.id.cyan){
                    ruleString = "@selcolor cyan("+seekbarIntensityHue.getProgressFloat()/100+","+seekbarIntensitySaturation.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+")" ;

                }
                else if(colorselection.getCheckedRadioButtonId()==R.id.white){
                    ruleString = "@selcolor white("+seekbarIntensityHue.getProgressFloat()/100+","+seekbarIntensitySaturation.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+")" ;

                }
//                String ruleString = "@adjust hsl "+seekbarIntensityHue.getProgressFloat()+" "+seekbarIntensitySaturation.getProgressFloat()+" "+seekbarIntensityLightness.getProgressFloat();


                if(tempbitmap == null){
                    tempbitmap = getBitmapFromView(HSlFragment.this.image_view_ratio);

                }
//                if(tempbitmap2!=null)
//                    tempbitmap2.recycle();

                tempbitmap2= CGENativeLibrary.filterImage_MultipleEffects(tempbitmap, ruleString, seekbarIntensityHue.getProgressFloat()/200);
                image_view_ratio.setImageBitmap(tempbitmap2);

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

            }
        });
        seekbarIntensitySaturation.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
//                String ruleString = "@adjust saturation "+seekParams.progressFloat;
////                Log.d(TAG, "onColorSelected: " + ruleString);
//
//                if (seekbarIntensityHue.getProgressFloat()==hue){
//                    tempbitmap=quShotView.getCurrentBitmap();
//                    hue=seekbarIntensitySaturation.getProgressFloat();
//                }
//                if(tempbitmap2!=null)
//                    tempbitmap2=null;
//
//                tempbitmap2=CGENativeLibrary.filterImage_MultipleEffects(tempbitmap, ruleString, 1.0f);
//                quShotEditor.parentView.setImageSource(tempbitmap2);


                String ruleString="";
                if(colorselection.getCheckedRadioButtonId()==R.id.red){
                    ruleString = "@selcolor red("+seekbarIntensityHue.getProgressFloat()/100+","+seekbarIntensitySaturation.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+")" ;

                }else if(colorselection.getCheckedRadioButtonId()==R.id.green){
                    ruleString = "@selcolor green("+seekbarIntensityHue.getProgressFloat()/100+","+seekbarIntensitySaturation.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+")" ;

                }else if(colorselection.getCheckedRadioButtonId()==R.id.blue){
                    ruleString = "@selcolor blue("+seekbarIntensityHue.getProgressFloat()/100+","+seekbarIntensitySaturation.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+")" ;

                }else if(colorselection.getCheckedRadioButtonId()==R.id.mergenta){
                    ruleString = "@selcolor magenta("+seekbarIntensityHue.getProgressFloat()/100+","+seekbarIntensitySaturation.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+")" ;

                }else if(colorselection.getCheckedRadioButtonId()==R.id.yellow){
                    ruleString = "@selcolor yellow("+seekbarIntensityHue.getProgressFloat()/100+","+seekbarIntensitySaturation.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+")" ;

                }else if(colorselection.getCheckedRadioButtonId()==R.id.cyan){
                    ruleString = "@selcolor cyan("+seekbarIntensityHue.getProgressFloat()/100+","+seekbarIntensitySaturation.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+")" ;

                }
                else if(colorselection.getCheckedRadioButtonId()==R.id.white){
                    ruleString = "@selcolor white("+seekbarIntensityHue.getProgressFloat()/100+","+seekbarIntensitySaturation.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+")" ;

                }
//                String ruleString = "@adjust hsl "+seekbarIntensityHue.getProgressFloat()+" "+seekbarIntensitySaturation.getProgressFloat()+" "+seekbarIntensityLightness.getProgressFloat();


                if(tempbitmap==null){
                    tempbitmap=getBitmapFromView(HSlFragment.this.image_view_ratio);

                }
//                if(tempbitmap2!=null)
//                    tempbitmap2.recycle();

                tempbitmap2=CGENativeLibrary.filterImage_MultipleEffects(tempbitmap, ruleString, seekbarIntensityHue.getProgressFloat()/200);
                image_view_ratio.setImageBitmap(tempbitmap2);

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

            }
        });
        colorselection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                tempbitmap=null;
            }
        });
        seekbarIntensityLightness.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
//                Log.d(TAG, "onColorSelected: " + seekParams.progress);
//                String ruleString = "@adjust hsl 0 0 "+seekParams.progressFloat;
////                Log.d(TAG, "onColorSelected: " + ruleString);
//
////                if(tempbitmap==null){
//                if (seekbarIntensitySaturation.getProgressFloat()==sat){
//                    tempbitmap=quShotView.getCurrentBitmap();
//                   sat=seekbarIntensitySaturation.getProgressFloat();
//                }
//
////                }
//                if(tempbitmap2!=null)
//                    tempbitmap2=null;
//
//                tempbitmap2=CGENativeLibrary.filterImage_MultipleEffects(tempbitmap, ruleString, seekbarIntensitySaturation.getProgressFloat()/100);
//                quShotEditor.parentView.setImageSource(tempbitmap2);
                String ruleString="";
                if(colorselection.getCheckedRadioButtonId()==R.id.red){
                    ruleString = "@selcolor red("+seekbarIntensityHue.getProgressFloat()/100+","+seekbarIntensitySaturation.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+")" ;

                }else if(colorselection.getCheckedRadioButtonId()==R.id.green){
                    ruleString = "@selcolor green("+seekbarIntensityHue.getProgressFloat()/100+","+seekbarIntensitySaturation.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+")" ;

                }else if(colorselection.getCheckedRadioButtonId()==R.id.blue){
                    ruleString = "@selcolor blue("+seekbarIntensityHue.getProgressFloat()/100+","+seekbarIntensitySaturation.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+")" ;

                }else if(colorselection.getCheckedRadioButtonId()==R.id.mergenta){
                    ruleString = "@selcolor magenta("+seekbarIntensityHue.getProgressFloat()/100+","+seekbarIntensitySaturation.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+")" ;

                }else if(colorselection.getCheckedRadioButtonId()==R.id.yellow){
                    ruleString = "@selcolor yellow("+seekbarIntensityHue.getProgressFloat()/100+","+seekbarIntensitySaturation.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+")" ;

                }else if(colorselection.getCheckedRadioButtonId()==R.id.cyan){
                    ruleString = "@selcolor cyan("+seekbarIntensityHue.getProgressFloat()/100+","+seekbarIntensitySaturation.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+")" ;

                }
                else if(colorselection.getCheckedRadioButtonId()==R.id.white){
                    ruleString = "@selcolor white("+seekbarIntensityHue.getProgressFloat()/100+","+seekbarIntensitySaturation.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+","+seekbarIntensityLightness.getProgressFloat()/100+")" ;

                }
//                String ruleString = "@adjust hsl "+seekbarIntensityHue.getProgressFloat()+" "+seekbarIntensitySaturation.getProgressFloat()+" "+seekbarIntensityLightness.getProgressFloat();


                if(tempbitmap==null){
                    tempbitmap=getBitmapFromView(HSlFragment.this.image_view_ratio);

                }
//                if(tempbitmap2!=null)
//                    tempbitmap2.recycle();

                tempbitmap2=CGENativeLibrary.filterImage_MultipleEffects(tempbitmap, ruleString, seekbarIntensityHue.getProgressFloat()/200);
                image_view_ratio.setImageBitmap(tempbitmap2);

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

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

    public void onDestroyView() {
        super.onDestroyView();
        this.bitmap = null;
    }


    public Bitmap getBitmapFromView(View view)
    {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
}
