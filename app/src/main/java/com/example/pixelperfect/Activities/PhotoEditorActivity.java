package com.example.pixelperfect.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.content.ContextCompat;
import androidx.core.internal.view.SupportMenu;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.pixelperfect.Adapters.AdjustAdapter;
import com.example.pixelperfect.Adapters.ColorAdapter;
import com.example.pixelperfect.Adapters.FilterAdapter;
import com.example.pixelperfect.Adapters.OverlayAdapter;
import com.example.pixelperfect.Adapters.RecyclerTabLayout;
import com.example.pixelperfect.Adapters.StickerAdapter;
import com.example.pixelperfect.Adapters.StickerTabAdapter;
import com.example.pixelperfect.Adapters.ToolsAdapter;
import com.example.pixelperfect.Assets.FilterFileAsset;
import com.example.pixelperfect.Assets.OverlayFileAsset;
import com.example.pixelperfect.Assets.StickerFileAsset;
import com.example.pixelperfect.Editor.PTextView;
import com.example.pixelperfect.Editor.PhotoEditor;
import com.example.pixelperfect.Editor.PhotoEditorView;
import com.example.pixelperfect.Editor.Text;
import com.example.pixelperfect.Editor.ViewType;
import com.example.pixelperfect.Event.AlignHorizontallyEvent;
import com.example.pixelperfect.Event.DeleteIconEvent;
import com.example.pixelperfect.Event.EditTextIconEvent;
import com.example.pixelperfect.Event.FlipHorizontallyEvent;
import com.example.pixelperfect.Event.ZoomIconEvent;
import com.example.pixelperfect.Fragments.CropperFragment;
import com.example.pixelperfect.Fragments.HSlFragment;
import com.example.pixelperfect.Fragments.RatioFragment;
import com.example.pixelperfect.Fragments.SquareFragment;
import com.example.pixelperfect.Fragments.TextFragment;
import com.example.pixelperfect.Listener.AdjustListener;
import com.example.pixelperfect.Listener.BrushColorListener;
import com.example.pixelperfect.Listener.FilterListener;
import com.example.pixelperfect.Listener.OnPhotoEditorListener;
import com.example.pixelperfect.R;
import com.example.pixelperfect.Sticker.BitmapStickerIcon;
import com.example.pixelperfect.Sticker.DrawableSticker;
import com.example.pixelperfect.Sticker.Sticker;
import com.example.pixelperfect.Sticker.StickerView;
import com.example.pixelperfect.Utils.BitmapTransfer;
import com.example.pixelperfect.Utils.Constants;
import com.example.pixelperfect.Utils.PermissionsUtils;
import com.example.pixelperfect.Utils.PreferenceUtil;
import com.example.pixelperfect.Utils.SaveFileUtils;
import com.example.pixelperfect.Utils.StoreManager;
import com.example.pixelperfect.Utils.SystemUtil;
import com.example.pixelperfect.Utils.ToolEditor;
import com.hold1.keyboardheightprovider.KeyboardHeightProvider;
import org.jetbrains.annotations.NotNull;
import org.wysaid.myUtils.MsgUtil;
import org.wysaid.nativePort.CGENativeLibrary;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@SuppressLint("StaticFieldLeak")
public class PhotoEditorActivity extends BaseActivity implements OnPhotoEditorListener, View.OnClickListener,HSlFragment.OnFilterSavePhoto, StickerAdapter.OnClickStickerListener, CropperFragment.OnCropPhoto, BrushColorListener, RatioFragment.RatioSaveListener, SquareFragment.SplashDialogListener, ToolsAdapter.OnItemSelected, FilterListener, AdjustListener {

    private static final String TAG = "PhotoEditorActivity";

    public ImageView imageViewAddSticker;
    private TextView addNewText;
    private RelativeLayout relativeLayoutText;
    private RelativeLayout relativeLayoutSaveSticker;
    private ConstraintLayout constraint_layout_adjust;
    private SeekBar seekbar_adjust;
    private ImageView image_view_compare_adjust;
    public ImageView image_view_compare_filter;
    public ImageView image_view_compare_overlay;
    public ToolEditor currentMode = ToolEditor.NONE;
    public SeekBar seekbar_filter;
    public ConstraintLayout constraint_layout_filter;
    private KeyboardHeightProvider keyboardHeightProvider;
    public ArrayList lstBitmapWithFilter = new ArrayList<>();

    public List<Bitmap> lstBitmapWithOverlay = new ArrayList<>();
    private final ToolsAdapter mEditingToolsAdapter = new ToolsAdapter(this);
    public CGENativeLibrary.LoadImageCallback mLoadImageCallback = new CGENativeLibrary.LoadImageCallback() {
        public Bitmap loadImage(String str, Object obj) {
            try {
                return BitmapFactory.decodeStream(PhotoEditorActivity.this.getAssets().open(str));
            } catch (IOException io) {
                return null;
            }
        }

        public void loadImageOK(Bitmap bitmap, Object obj) {
            bitmap.recycle();
        }
    };
    public PhotoEditor photoEditor;
    public PhotoEditorView photo_editor_view;
    private ConstraintLayout constraint_layout_root_view;
    private RecyclerView recycler_view_adjust;
    public RecyclerView recycler_view_filter;

    public RecyclerView recycler_view_overlay;

    public RecyclerView recycler_view_tools;
    public AdjustAdapter mAdjustAdapter;


    @SuppressLint("ClickableViewAccessibility")
    View.OnTouchListener onCompareTouchListener = (view, motionEvent) -> {
        switch (motionEvent.getAction()) {
            case 0:
                PhotoEditorActivity.this.photo_editor_view.getGLSurfaceView().setAlpha(0.0f);
                return true;
            case 1:
                PhotoEditorActivity.this.photo_editor_view.getGLSurfaceView().setAlpha(1.0f);
                return false;
            default:
                return true;
        }
    };

    public SeekBar seekbar_overlay;
    public ConstraintLayout constraint_layout_overlay;
    private ConstraintLayout constraint_save_control;
    public SeekBar seekbarStickerAlpha;
    private ConstraintLayout constraint_layout_sticker;
    public TextFragment.TextEditor textEditor;
    public TextFragment textEditorDialogFragment;
    private RelativeLayout relativeLayoutSaveText;
    public RelativeLayout relative_layout_wrapper_photo;
    public LinearLayout linear_layout_wrapper_sticker_list;
    private RelativeLayout relative_layout_loading;
    private Guideline guidelinePaint;
    private Guideline guideline;
    private Animation slideUpAnimation, slideDownAnimation;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Constants.fullScreen(this);
        setContentView(R.layout.activity_photo_editor);
        initViews();
        slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        CGENativeLibrary.setLoadImageCallback(this.mLoadImageCallback, null);
        if (Build.VERSION.SDK_INT < 26) {
            getWindow().setSoftInputMode(48);
        }
        this.recycler_view_tools.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recycler_view_tools.setAdapter(this.mEditingToolsAdapter);
        this.recycler_view_tools.setHasFixedSize(true);
        this.recycler_view_filter.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recycler_view_filter.setHasFixedSize(true);
        this.recycler_view_overlay.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recycler_view_overlay.setHasFixedSize(true);
        new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        this.photoEditor = new PhotoEditor.Builder(this, this.photo_editor_view).setPinchTextScalable(true).build();
        this.photoEditor.setOnPhotoEditorListener(this);
        PreferenceUtil.setHeightOfKeyboard(getApplicationContext(), 0);
        this.keyboardHeightProvider = new KeyboardHeightProvider(this);
        this.keyboardHeightProvider.addKeyboardListener(i -> {
            if (i <= 0) {
                PreferenceUtil.setHeightOfNotch(getApplicationContext(), -i);
            } else if (textEditorDialogFragment != null) {
                textEditorDialogFragment.updateAddTextBottomToolbarHeight(PreferenceUtil.getHeightOfNotch(getApplicationContext()) + i);
                PreferenceUtil.setHeightOfKeyboard(getApplicationContext(), i + PreferenceUtil.getHeightOfNotch(getApplicationContext()));
            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            new OnLoadBitmapFromUri().execute(extras.getString(Constants.KEY_SELECTED_PHOTOS));
        }
    }

    private void initViews() {
        this.relative_layout_loading = findViewById(R.id.relative_layout_loading);
        this.relative_layout_loading.setVisibility(View.VISIBLE);
        this.linear_layout_wrapper_sticker_list = findViewById(R.id.linear_layout_wrapper_sticker_list);
        this.photo_editor_view = findViewById(R.id.photo_editor_view);
        this.photo_editor_view.setVisibility(View.INVISIBLE);
        this.recycler_view_tools = findViewById(R.id.recycler_view_tools);

        this.guidelinePaint = findViewById(R.id.guidelinePaint);
        this.guideline = findViewById(R.id.guideline);
        this.recycler_view_filter = findViewById(R.id.recycler_view_filter);
        this.recycler_view_overlay = findViewById(R.id.recycler_view_overlay);
        this.recycler_view_adjust = findViewById(R.id.recycler_view_adjust);
        this.constraint_layout_root_view = findViewById(R.id.constraint_layout_root_view);
        this.constraint_layout_filter = findViewById(R.id.constraint_layout_filter);
        this.constraint_layout_overlay = findViewById(R.id.constraint_layout_overlay);
        this.constraint_layout_adjust = findViewById(R.id.constraint_layout_adjust);
        this.constraint_layout_sticker = findViewById(R.id.constraint_layout_sticker);
        this.relativeLayoutSaveSticker = findViewById(R.id.relativeLayoutSaveSticker);
        this.relativeLayoutSaveText = findViewById(R.id.relativeLayoutSaveText);
        ViewPager sticker_viewpaper = findViewById(R.id.sticker_viewpaper);
        this.seekbar_filter = findViewById(R.id.seekbar_filter);
        this.seekbar_overlay = findViewById(R.id.seekbar_overlay);
        this.seekbarStickerAlpha = findViewById(R.id.seekbarStickerAlpha);
        this.seekbarStickerAlpha.setVisibility(View.GONE);
        this.relative_layout_wrapper_photo = findViewById(R.id.relative_layout_wrapper_photo);

        ImageView text_view_save = findViewById(R.id.imageViewSaveFinal);
        this.constraint_save_control = findViewById(R.id.constraint_save_control);
        text_view_save.setOnClickListener(view -> {
            if (PermissionsUtils.checkWriteStoragePermission(PhotoEditorActivity.this)) {
                new SaveBitmap().execute();
            }
        });
        this.image_view_compare_adjust = findViewById(R.id.image_view_compare_adjust);
        this.image_view_compare_adjust.setOnTouchListener(this.onCompareTouchListener);
        this.image_view_compare_adjust.setVisibility(View.GONE);

        this.image_view_compare_filter = findViewById(R.id.image_view_compare_filter);
        this.image_view_compare_filter.setOnTouchListener(this.onCompareTouchListener);
        this.image_view_compare_filter.setVisibility(View.GONE);

        this.image_view_compare_overlay = findViewById(R.id.image_view_compare_overlay);
        this.image_view_compare_overlay.setOnTouchListener(this.onCompareTouchListener);
        this.image_view_compare_overlay.setVisibility(View.GONE);
        findViewById(R.id.image_view_exit).setOnClickListener(view -> PhotoEditorActivity.this.onBackPressed());
        this.seekbarStickerAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                Sticker currentSticker = PhotoEditorActivity.this.photo_editor_view.getCurrentSticker();
                if (currentSticker != null) {
                    currentSticker.setAlpha(i);
                }
            }
        });
        this.imageViewAddSticker = findViewById(R.id.imageViewAddSticker);
        this.imageViewAddSticker.setVisibility(View.GONE);
        this.imageViewAddSticker.setOnClickListener(view -> {
            PhotoEditorActivity.this.imageViewAddSticker.setVisibility(View.GONE);
            PhotoEditorActivity.this.slideUp(PhotoEditorActivity.this.linear_layout_wrapper_sticker_list);
        });
        this.addNewText = findViewById(R.id.addNewText);
        this.relativeLayoutText = findViewById(R.id.relativeLayoutText);
        this.addNewText.setOnClickListener(view -> {
            PhotoEditorActivity.this.photo_editor_view.setHandlingSticker(null);
            PhotoEditorActivity.this.openTextFragment();
        });
        this.seekbar_adjust = findViewById(R.id.seekbar_adjust);
        this.seekbar_adjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                PhotoEditorActivity.this.mAdjustAdapter.getCurrentAdjustModel().setSeekBarIntensity(PhotoEditorActivity.this.photoEditor, ((float) i) / ((float) seekBar.getMax()), true);
            }
        });
        BitmapStickerIcon bitmapStickerIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this, R.drawable.ic_outline_close), 0, BitmapStickerIcon.REMOVE);
        bitmapStickerIcon.setIconEvent(new DeleteIconEvent());
        BitmapStickerIcon bitmapStickerIcon2 = new BitmapStickerIcon(ContextCompat.getDrawable(this, R.drawable.ic_outline_scale), 3, BitmapStickerIcon.ZOOM);
        bitmapStickerIcon2.setIconEvent(new ZoomIconEvent());
        BitmapStickerIcon bitmapStickerIcon3 = new BitmapStickerIcon(ContextCompat.getDrawable(this, R.drawable.ic_outline_flip), 1, BitmapStickerIcon.FLIP);
        bitmapStickerIcon3.setIconEvent(new FlipHorizontallyEvent());
        BitmapStickerIcon bitmapStickerIcon4 = new BitmapStickerIcon(ContextCompat.getDrawable(this, R.drawable.ic_outline_rotate), 3, BitmapStickerIcon.ROTATE);
        bitmapStickerIcon4.setIconEvent(new ZoomIconEvent());
        BitmapStickerIcon bitmapStickerIcon5 = new BitmapStickerIcon(ContextCompat.getDrawable(this, R.drawable.ic_outline_edit), 1, BitmapStickerIcon.EDIT);
        bitmapStickerIcon5.setIconEvent(new EditTextIconEvent());
        BitmapStickerIcon bitmapStickerIcon6 = new BitmapStickerIcon(ContextCompat.getDrawable(this, R.drawable.ic_outline_center), 2, BitmapStickerIcon.ALIGN_HORIZONTALLY);
        bitmapStickerIcon6.setIconEvent(new AlignHorizontallyEvent());
        this.photo_editor_view.setIcons(Arrays.asList(bitmapStickerIcon, bitmapStickerIcon2, bitmapStickerIcon3, bitmapStickerIcon5, bitmapStickerIcon4, bitmapStickerIcon6));
        this.photo_editor_view.setBackgroundColor(-16777216);
        this.photo_editor_view.setLocked(false);
        this.photo_editor_view.setConstrained(true);
        this.photo_editor_view.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            public void onStickerDragFinished(@NonNull Sticker sticker) {
            }

            public void onStickerFlipped(@NonNull Sticker sticker) {
            }

            public void onStickerTouchedDown(@NonNull Sticker sticker) {
            }

            public void onStickerZoomFinished(@NonNull Sticker sticker) {
            }

            public void onTouchDownForBeauty(float f, float f2) {
            }

            public void onTouchDragForBeauty(float f, float f2) {
            }

            public void onTouchUpForBeauty(float f, float f2) {
            }

            public void onStickerAdded(@NonNull Sticker sticker) {
                PhotoEditorActivity.this.seekbarStickerAlpha.setVisibility(View.VISIBLE);
                PhotoEditorActivity.this.seekbarStickerAlpha.setProgress(sticker.getAlpha());
            }

            @SuppressLint("RestrictedApi")
            public void onStickerClicked(@NonNull Sticker sticker) {
                if (sticker instanceof PTextView) {
                    ((PTextView) sticker).setTextColor(SupportMenu.CATEGORY_MASK);
                    PhotoEditorActivity.this.photo_editor_view.replace(sticker);
                    PhotoEditorActivity.this.photo_editor_view.invalidate();
                }
                PhotoEditorActivity.this.seekbarStickerAlpha.setVisibility(View.VISIBLE);
                PhotoEditorActivity.this.seekbarStickerAlpha.setProgress(sticker.getAlpha());
            }

            public void onStickerDeleted(@NonNull Sticker sticker) {
                PhotoEditorActivity.this.seekbarStickerAlpha.setVisibility(View.GONE);
            }

            public void onStickerTouchOutside() {
                PhotoEditorActivity.this.seekbarStickerAlpha.setVisibility(View.GONE);
            }

            public void onStickerDoubleTapped(@NonNull Sticker sticker) {
                if (sticker instanceof PTextView) {
                    sticker.setShow(false);
                    photo_editor_view.setHandlingSticker((Sticker) null);
                    textEditorDialogFragment = TextFragment.show(PhotoEditorActivity.this, ((PTextView) sticker).getPolishText());
                    textEditor = new TextFragment.TextEditor() {
                        public void onDone(Text polishText) {
                            photo_editor_view.getStickers().remove(photo_editor_view.getLastHandlingSticker());
                            photo_editor_view.addSticker(new PTextView(PhotoEditorActivity.this, polishText));
                        }

                        public void onBackButton() {
                            photo_editor_view.showLastHandlingSticker();
                        }
                    };
                    textEditorDialogFragment.setOnTextEditorListener(textEditor);
                }
            }
        });
        this.seekbar_filter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                PhotoEditorActivity.this.photo_editor_view.setFilterIntensity(((float) i) / 100.0f);
            }
        });
        this.seekbar_overlay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                PhotoEditorActivity.this.photo_editor_view.setFilterIntensity(((float) i) / 100.0f);
            }
        });

        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        sticker_viewpaper.setAdapter(new PagerAdapter() {
            public int getCount() {
                return 15;
            }

            public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
                return view.equals(obj);
            }


            @Override
            public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
                (container).removeView((View) object);
            }

            @NonNull
            public Object instantiateItem(@NonNull ViewGroup viewGroup, int i) {
                View inflate = LayoutInflater.from(PhotoEditorActivity.this.getBaseContext()).inflate(R.layout.sticker_list, null, false);
                RecyclerView recycler_view_sticker = inflate.findViewById(R.id.recycler_view_sticker);
                recycler_view_sticker.setHasFixedSize(true);
                recycler_view_sticker.setLayoutManager(new GridLayoutManager(PhotoEditorActivity.this.getApplicationContext(), 6));
                switch (i) {
                    case 0:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PhotoEditorActivity.this.getApplicationContext(), StickerFileAsset.amojiList(), i, PhotoEditorActivity.this));
                        break;
                    case 1:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PhotoEditorActivity.this.getApplicationContext(), StickerFileAsset.chickenList(), i, PhotoEditorActivity.this));
                        break;
                    case 2:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PhotoEditorActivity.this.getApplicationContext(), StickerFileAsset.childList(), i, PhotoEditorActivity.this));
                        break;
                    case 3:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PhotoEditorActivity.this.getApplicationContext(), StickerFileAsset.christmasList(), i, PhotoEditorActivity.this));
                        break;
                    case 4:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PhotoEditorActivity.this.getApplicationContext(), StickerFileAsset.cuteList(), i, PhotoEditorActivity.this));
                        break;
                    case 5:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PhotoEditorActivity.this.getApplicationContext(), StickerFileAsset.emojList(), i, PhotoEditorActivity.this));
                        break;
                    case 6:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PhotoEditorActivity.this.getApplicationContext(), StickerFileAsset.emojiList(), i, PhotoEditorActivity.this));
                        break;
                    case 7:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PhotoEditorActivity.this.getApplicationContext(), StickerFileAsset.fruitList(), i, PhotoEditorActivity.this));
                        break;
                    case 8:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PhotoEditorActivity.this.getApplicationContext(), StickerFileAsset.heartList(), i, PhotoEditorActivity.this));
                        break;
                    case 9:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PhotoEditorActivity.this.getApplicationContext(), StickerFileAsset.lovedayList(), i, PhotoEditorActivity.this));
                        break;
                    case 10:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PhotoEditorActivity.this.getApplicationContext(), StickerFileAsset.plantList(), i, PhotoEditorActivity.this));
                        break;
                    case 11:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PhotoEditorActivity.this.getApplicationContext(), StickerFileAsset.stickerList(), i, PhotoEditorActivity.this));
                        break;
                    case 12:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PhotoEditorActivity.this.getApplicationContext(), StickerFileAsset.sweetList(), i, PhotoEditorActivity.this));
                        break;
                    case 13:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PhotoEditorActivity.this.getApplicationContext(), StickerFileAsset.textcolorList(), i, PhotoEditorActivity.this));
                        break;
                    case 14:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PhotoEditorActivity.this.getApplicationContext(), StickerFileAsset.textneonList(), i, PhotoEditorActivity.this));
                        break;
                }

                viewGroup.addView(inflate);
                return inflate;
            }
        });
        RecyclerTabLayout recycler_tab_layout = findViewById(R.id.recycler_tab_layout);
        recycler_tab_layout.setUpWithAdapter(new StickerTabAdapter(sticker_viewpaper, getApplicationContext()));
        recycler_tab_layout.setPositionThreshold(0.5f);
        recycler_tab_layout.setBackgroundColor(ContextCompat.getColor(this, R.color.BackgroundColor));
    }

    public void slideUp(final View showLayout) {
        showLayout.setVisibility(View.VISIBLE);
        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        showLayout.startAnimation(slideUpAnimation);
        slideUpAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    public void slideDown(final View hideLayout) {
        hideLayout.setVisibility(View.GONE);
        hideLayout.startAnimation(slideDownAnimation);
        slideDownAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
    }

    public void onAddViewListener(ViewType viewType, int i) {
        Log.d(TAG, "onAddViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + i + "]");
    }

    public void onRemoveViewListener(int i) {
        Log.d(TAG, "onRemoveViewListener() called with: numberOfAddedViews = [" + i + "]");
    }

    public void onRemoveViewListener(ViewType viewType, int i) {
        Log.d(TAG, "onRemoveViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + i + "]");
    }

    public void onStartViewChangeListener(ViewType viewType) {
        Log.d(TAG, "onStartViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    public void onStopViewChangeListener(ViewType viewType) {
        Log.d(TAG, "onStopViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_view_close_adjust:
            case R.id.image_view_close_filter:
            case R.id.image_view_close_overlay:
            case R.id.closeSticker:
            case R.id.image_view_close_text:
                slideDownSaveView();
                onBackPressed();
                return;
            case R.id.image_view_save_adjust:
                new SaveFilterAsBitmap().execute();
                this.image_view_compare_adjust.setVisibility(View.GONE);
                slideDown(this.constraint_layout_adjust);
                slideUp(this.recycler_view_tools);
                slideDownSaveView();
                setGuideLine();
                updateLayout();
                this.currentMode = ToolEditor.NONE;
                return;
            case R.id.image_view_save_filter:
                new SaveFilterAsBitmap().execute();
                this.image_view_compare_filter.setVisibility(View.GONE);
                slideDown(this.constraint_layout_filter);
                slideUp(this.recycler_view_tools);
                slideDownSaveView();
                setGuideLine();
                updateLayout();
                this.currentMode = ToolEditor.NONE;
                return;
            case R.id.image_view_save_overlay:
                new SaveFilterAsBitmap().execute();
                slideDown(this.constraint_layout_overlay);
                slideUp(this.recycler_view_tools);
                this.image_view_compare_overlay.setVisibility(View.GONE);
                slideDownSaveView();
                setGuideLine();
                updateLayout();
                this.currentMode = ToolEditor.NONE;
                return;
            case R.id.saveSticker:
                this.photo_editor_view.setHandlingSticker(null);
                this.photo_editor_view.setLocked(true);
                this.relativeLayoutSaveSticker.setVisibility(View.GONE);
                this.imageViewAddSticker.setVisibility(View.GONE);
                if (!this.photo_editor_view.getStickers().isEmpty()) {
                    new SaveStickerAsBitmap().execute();
                }
                updateLayout();
                setGuideLine();
                slideDown(this.linear_layout_wrapper_sticker_list);
                slideDown(this.constraint_layout_sticker);
                slideDown(this.relativeLayoutSaveSticker);
                slideUp(this.recycler_view_tools);
                slideDownSaveView();
                this.currentMode = ToolEditor.NONE;
                return;
            case R.id.image_view_save_text:
                this.photo_editor_view.setHandlingSticker(null);
                this.photo_editor_view.setLocked(true);
                this.relativeLayoutSaveText.setVisibility(View.GONE);
                if (!this.photo_editor_view.getStickers().isEmpty()) {
                    new SaveStickerAsBitmap().execute();
                }
                setGuideLine();
                slideDown(this.relativeLayoutText);
                slideUp(this.recycler_view_tools);
                slideDownSaveView();
                updateLayout();
                this.currentMode = ToolEditor.NONE;
                return;
            case R.id.imageViewRedo:
                setRedo();
                return;
            case R.id.imageViewUndo:
                setUndo();
                return;
            default:
        }
    }

    private void setUndo() {
        photo_editor_view.undo();
    }

    private void setRedo() {
        photo_editor_view.redo();
    }

    public void onPause() {
        super.onPause();
        this.keyboardHeightProvider.onPause();
    }

    public void onResume() {
        super.onResume();
        this.keyboardHeightProvider.onResume();
    }

    public void openTextFragment() {
        this.textEditorDialogFragment = TextFragment.show(this);
        this.textEditor = new TextFragment.TextEditor() {
            public void onDone(Text polishText) {
                photo_editor_view.addSticker(new PTextView(getApplicationContext(), polishText));
            }

            public void onBackButton() {
                if (photo_editor_view.getStickers().isEmpty()) {
                    onBackPressed();
                }
            }
        };
        this.textEditorDialogFragment.setOnTextEditorListener(this.textEditor);
    }

    public void onToolSelected(ToolEditor toolType) {
        this.currentMode = toolType;
        switch (toolType) {
            case TEXT:
                slideUpSaveView();
                this.photo_editor_view.setLocked(false);
                openTextFragment();
                slideDown(this.recycler_view_tools);
                slideUp(this.relativeLayoutSaveText);
                this.relativeLayoutText.setVisibility(View.VISIBLE);
                setGuideLine();
                break;
            case ADJUST:
                slideUpSaveView();
                updateLayout();
                this.image_view_compare_adjust.setVisibility(View.VISIBLE);
                this.mAdjustAdapter = new AdjustAdapter(getApplicationContext(), this);
                this.recycler_view_adjust.setAdapter(this.mAdjustAdapter);
                this.mAdjustAdapter.setSelectedAdjust(0);
                this.photoEditor.setAdjustFilter(this.mAdjustAdapter.getFilterConfig());
                ConstraintSet constraintSetAdjust = new ConstraintSet();
                constraintSetAdjust.clone(this.constraint_layout_root_view);
                constraintSetAdjust.connect(this.relative_layout_wrapper_photo.getId(), 1, this.constraint_layout_root_view.getId(), 1, 0);
                constraintSetAdjust.connect(this.relative_layout_wrapper_photo.getId(), 4, this.constraint_layout_adjust.getId(), 3, 0);
                constraintSetAdjust.connect(this.relative_layout_wrapper_photo.getId(), 2, this.constraint_layout_root_view.getId(), 2, 0);
                constraintSetAdjust.applyTo(this.constraint_layout_root_view);
                slideUp(this.constraint_layout_adjust);
                slideDown(this.recycler_view_tools);
                break;
            case FILTER:
                slideUpSaveView();
                new LoadFilterBitmap().execute();
                break;
            case EFFECT:
                slideUpSaveView();
                new LoadOverlayBitmap().execute();
                break;
            case STICKER:
                this.constraint_layout_sticker.setVisibility(View.VISIBLE);
                this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
                updateLayout();
                slideUpSaveView();
                this.photo_editor_view.setLocked(false);
                slideDown(this.recycler_view_tools);
                slideUp(this.constraint_layout_sticker);
                slideUp(this.relativeLayoutSaveSticker);
                setGuideLine();
                break;
            case RATIO:
                new ShowRatioFragment().execute();
                break;
            case SQUARE:
                new ShowSplashFragment(true).execute();
                break;
            case CROP:
                CropperFragment.show(this, this, this.photo_editor_view.getCurrentBitmap());
                break;
            case HSL:
                HSlFragment.show(this, this, this.photo_editor_view.getCurrentBitmap());
                break;

        }
        this.photo_editor_view.setHandlingSticker(null);
    }

    public void setGuideLine() {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.constraint_layout_root_view);
        constraintSet.connect(this.relative_layout_wrapper_photo.getId(), 1, this.constraint_layout_root_view.getId(), 1, 0);
        constraintSet.connect(this.relative_layout_wrapper_photo.getId(), 4, this.guideline.getId(), 3, 0);
        constraintSet.connect(this.relative_layout_wrapper_photo.getId(), 2, this.constraint_layout_root_view.getId(), 2, 0);
        constraintSet.applyTo(this.constraint_layout_root_view);
    }

    public void slideUpSaveView() {
        this.constraint_save_control.setVisibility(View.GONE);
    }


    public void slideDownSaveView() {
        this.constraint_save_control.setVisibility(View.VISIBLE);
    }

    public void onBackPressed() {
        if (this.currentMode != null) {
            try {
                switch (this.currentMode) {
                    case TEXT:
                        if (!this.photo_editor_view.getStickers().isEmpty()) {
                            this.photo_editor_view.getStickers().clear();
                            this.photo_editor_view.setHandlingSticker(null);
                        }
                        slideDown(this.relativeLayoutSaveText);
                        this.relativeLayoutText.setVisibility(View.GONE);
                        this.photo_editor_view.setHandlingSticker(null);
                        slideUp(this.recycler_view_tools);
                        this.photo_editor_view.setLocked(true);
                        slideDownSaveView();
                        setGuideLine();
                        this.currentMode = ToolEditor.NONE;
                        updateLayout();
                        return;
                    case ADJUST:
                        this.photoEditor.setFilterEffect("");
                        this.image_view_compare_adjust.setVisibility(View.GONE);
                        slideDown(this.constraint_layout_adjust);
                        slideUp(this.recycler_view_tools);
                        slideDownSaveView();
                        setGuideLine();
                        this.currentMode = ToolEditor.NONE;
                        updateLayout();
                        return;
                    case FILTER:
                        slideDown(this.constraint_layout_filter);
                        slideUp(this.recycler_view_tools);
                        slideDownSaveView();
                        this.photoEditor.setFilterEffect("");
                        this.image_view_compare_filter.setVisibility(View.GONE);
                        this.lstBitmapWithFilter.clear();
                        if (this.recycler_view_filter.getAdapter() != null) {
                            this.recycler_view_filter.getAdapter().notifyDataSetChanged();
                        }
                        setGuideLine();
                        this.currentMode = ToolEditor.NONE;
                        updateLayout();
                        return;
                    case STICKER:
                        if (this.photo_editor_view.getStickers().size() <= 0) {
                            this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
                            slideUp(recycler_view_tools);
                            slideDown(constraint_layout_sticker);
                            this.imageViewAddSticker.setVisibility(View.GONE);
                            this.photo_editor_view.setHandlingSticker(null);
                            this.photo_editor_view.setLocked(true);
                            this.currentMode = ToolEditor.NONE;
                        } else if (this.imageViewAddSticker.getVisibility() == View.VISIBLE) {
                            this.photo_editor_view.getStickers().clear();
                            this.imageViewAddSticker.setVisibility(View.GONE);
                            slideUp(recycler_view_tools);
                            slideDown(constraint_layout_sticker);
                            this.photo_editor_view.setHandlingSticker(null);
                            this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
                            this.currentMode = ToolEditor.NONE;
                        } else {
                            this.linear_layout_wrapper_sticker_list.setVisibility(View.GONE);
                            this.imageViewAddSticker.setVisibility(View.VISIBLE);
                        }
                        this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
                        this.currentMode = ToolEditor.NONE;
                        slideDownSaveView();
                        this.relativeLayoutSaveSticker.setVisibility(View.GONE);
                        setGuideLine();
                        updateLayout();
                        return;
                    case EFFECT:
                        this.photoEditor.setFilterEffect("");
                        this.image_view_compare_overlay.setVisibility(View.GONE);
                        this.lstBitmapWithOverlay.clear();
                        slideUp(this.recycler_view_tools);
                        slideDown(this.constraint_layout_overlay);
                        slideDownSaveView();
                        setGuideLine();
                        this.recycler_view_overlay.getAdapter().notifyDataSetChanged();
                        this.currentMode = ToolEditor.NONE;
                        updateLayout();
                        return;
                    case SQUARE:
                    case BG:
                    case CROP:
                    case HSL:
                    case NONE:
                        setOnBackPressDialog();
                        return;
                    default:
                        super.onBackPressed();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setOnBackPressDialog() {
        Dialog dialogOnBackPressed= new Dialog(this, R.style.Theme_Dialog);
        dialogOnBackPressed.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogOnBackPressed.setCancelable(false);
        dialogOnBackPressed.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialogOnBackPressed.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);
        dialogOnBackPressed.setContentView(R.layout.dialog_exit);
        TextView textViewCancel, textViewDiscard;
        textViewCancel = dialogOnBackPressed.findViewById(R.id.textViewCancel);
        textViewDiscard = dialogOnBackPressed.findViewById(R.id.textViewDiscard);
        textViewCancel.setOnClickListener(view -> {
            dialogOnBackPressed.dismiss();
        });

        textViewDiscard.setOnClickListener(view -> {
            dialogOnBackPressed.dismiss();
            PhotoEditorActivity.this.currentMode = null;
            PhotoEditorActivity.this.finish();
            finish();
        });

        dialogOnBackPressed.show();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void addSticker(int item, Bitmap bitmap) {
        this.photo_editor_view.addSticker(new DrawableSticker(new BitmapDrawable(getResources(), bitmap)));
        slideDown(this.linear_layout_wrapper_sticker_list);
        this.imageViewAddSticker.setVisibility(View.VISIBLE);
    }

    public void finishCrop(Bitmap bitmap) {
        this.photo_editor_view.setImageSource(bitmap);
        this.currentMode = ToolEditor.NONE;
        updateLayout();
    }

    public void onColorChanged(int item, String str) {
        this.photoEditor.setBrushColor(Color.parseColor(str));

    }

    public void ratioSavedBitmap(Bitmap bitmap) {
        this.photo_editor_view.setImageSource(bitmap);
        this.currentMode = ToolEditor.NONE;
        updateLayout();
    }

    @Override
    public void onSaveBlurBackground(Bitmap bitmap) {
        this.photo_editor_view.setImageSource(bitmap);
        this.currentMode = ToolEditor.NONE;
    }

    @Override
    public void onSaveFilter(Bitmap bitmap) {
        this.photo_editor_view.setImageSource(bitmap);
        this.currentMode = ToolEditor.NONE;
    }

    public void onFilterSelected(int itemCurrent, String string) {
        this.photoEditor.setFilterEffect(string);
        this.seekbar_filter.setProgress(50);
        this.seekbar_overlay.setProgress(70);
        if (this.currentMode == ToolEditor.EFFECT) {
            this.photo_editor_view.getGLSurfaceView().setFilterIntensity(0.7f);
        } else if (this.currentMode == ToolEditor.FILTER) {
            this.photo_editor_view.getGLSurfaceView().setFilterIntensity(0.5f);
        }
    }

    @Override
    public void onAdjustSelected(AdjustAdapter.AdjustModel adjustModel) {
        this.seekbar_adjust.setProgress((int) (adjustModel.seekbarIntensity * ((float) this.seekbar_adjust.getMax())));
    }


    class LoadFilterBitmap extends AsyncTask<Void, Void, Void> {
        LoadFilterBitmap() { }

        public void onPreExecute() {
            PhotoEditorActivity.this.mLoading(true);
        }

        public Void doInBackground(Void... voidArr) {
            PhotoEditorActivity.this.lstBitmapWithFilter.clear();
            PhotoEditorActivity.this.lstBitmapWithFilter.addAll(FilterFileAsset.getListBitmapFilter(ThumbnailUtils.extractThumbnail(PhotoEditorActivity.this.photo_editor_view.getCurrentBitmap(), 100, 100)));
            Log.d("XXXXXXXX", "LoadFilterBitmap " + PhotoEditorActivity.this.lstBitmapWithFilter.size());
            return null;
        }

        public void onPostExecute(Void voidR) {
            PhotoEditorActivity.this.recycler_view_filter.setAdapter(new FilterAdapter(PhotoEditorActivity.this.lstBitmapWithFilter, PhotoEditorActivity.this, PhotoEditorActivity.this.getApplicationContext(), Arrays.asList(FilterFileAsset.FILTERS)));
            PhotoEditorActivity.this.slideDown(PhotoEditorActivity.this.recycler_view_tools);
            PhotoEditorActivity.this.slideUp(PhotoEditorActivity.this.constraint_layout_filter);
            PhotoEditorActivity.this.image_view_compare_filter.setVisibility(View.VISIBLE);
            PhotoEditorActivity.this.seekbar_filter.setProgress(100);
            ConstraintSet constraintSetAdjust = new ConstraintSet();
            constraintSetAdjust.clone(constraint_layout_root_view);
            constraintSetAdjust.connect(relative_layout_wrapper_photo.getId(), 1, constraint_layout_root_view.getId(), 1, 0);
            constraintSetAdjust.connect(relative_layout_wrapper_photo.getId(), 4, constraint_layout_filter.getId(), 3, 0);
            constraintSetAdjust.connect(relative_layout_wrapper_photo.getId(), 2, constraint_layout_root_view.getId(), 2, 0);
            constraintSetAdjust.applyTo(constraint_layout_root_view);
            PhotoEditorActivity.this.mLoading(false);
            updateLayout();
        }
    }

    class LoadOverlayBitmap extends AsyncTask<Void, Void, Void> {
        LoadOverlayBitmap() {
        }

        public void onPreExecute() {
            PhotoEditorActivity.this.mLoading(true);
        }

        public Void doInBackground(Void... voidArr) {
            PhotoEditorActivity.this.lstBitmapWithOverlay.clear();
            PhotoEditorActivity.this.lstBitmapWithOverlay.addAll(OverlayFileAsset.getListBitmapOverlayEffect(ThumbnailUtils.extractThumbnail(PhotoEditorActivity.this.photo_editor_view.getCurrentBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voidR) {
            PhotoEditorActivity.this.recycler_view_overlay.setAdapter(new OverlayAdapter(PhotoEditorActivity.this.lstBitmapWithOverlay, PhotoEditorActivity.this, PhotoEditorActivity.this.getApplicationContext(), Arrays.asList(OverlayFileAsset.OVERLAY_EFFECTS)));
            PhotoEditorActivity.this.slideDown(PhotoEditorActivity.this.recycler_view_tools);
            PhotoEditorActivity.this.slideUp(PhotoEditorActivity.this.constraint_layout_overlay);
            PhotoEditorActivity.this.image_view_compare_overlay.setVisibility(View.VISIBLE);
            PhotoEditorActivity.this.seekbar_overlay.setProgress(100);
            PhotoEditorActivity.this.mLoading(false);
            ConstraintSet constraintSetAdjust = new ConstraintSet();
            constraintSetAdjust.clone(constraint_layout_root_view);
            constraintSetAdjust.connect(relative_layout_wrapper_photo.getId(), 1, constraint_layout_root_view.getId(), 1, 0);
            constraintSetAdjust.connect(relative_layout_wrapper_photo.getId(), 4, constraint_layout_overlay.getId(), 3, 0);
            constraintSetAdjust.connect(relative_layout_wrapper_photo.getId(), 2, constraint_layout_root_view.getId(), 2, 0);
            constraintSetAdjust.applyTo(constraint_layout_root_view);
            updateLayout();
        }
    }

    class ShowRatioFragment extends AsyncTask<Void, Bitmap, Bitmap> {
        ShowRatioFragment() {
        }

        public void onPreExecute() {
            PhotoEditorActivity.this.mLoading(true);
        }

        public Bitmap doInBackground(Void... voidArr) {
            return FilterFileAsset.getBlurImageFromBitmap(PhotoEditorActivity.this.photo_editor_view.getCurrentBitmap(), 5.0f);
        }

        public void onPostExecute(Bitmap bitmap) {
            PhotoEditorActivity.this.mLoading(false);
            RatioFragment.show(PhotoEditorActivity.this, PhotoEditorActivity.this, PhotoEditorActivity.this.photo_editor_view.getCurrentBitmap(), bitmap);
        }
    }
    class ShowSplashFragment extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {
        boolean isSplashSquared;

        public ShowSplashFragment(boolean z) {
            this.isSplashSquared = z;
        }

        public void onPreExecute() {
            mLoading(true);
        }

        public List<Bitmap> doInBackground(Void... voids) {
            Bitmap currentBitmap = photo_editor_view.getCurrentBitmap();
            List<Bitmap> arrayList = new ArrayList<>();
            arrayList.add(currentBitmap);
            if (this.isSplashSquared) {
                arrayList.add(FilterFileAsset.getBlurImageFromBitmap(currentBitmap, 2.5f));
            }
            return arrayList;
        }

        public void onPostExecute(List<Bitmap> list) {
            if (this.isSplashSquared) {
                SquareFragment.show(PhotoEditorActivity.this, list.get(0), null, list.get(1), PhotoEditorActivity.this, true);
            }
            PhotoEditorActivity.this.mLoading(false);
        }
    }

    class SaveFilterAsBitmap extends AsyncTask<Void, Void, Bitmap> {
        SaveFilterAsBitmap() {
        }

        public void onPreExecute() {
            PhotoEditorActivity.this.mLoading(true);
        }

        public Bitmap doInBackground(Void... voidArr) {
            final Bitmap[] bitmapArr = {null};
            PhotoEditorActivity.this.photo_editor_view.saveGLSurfaceViewAsBitmap(bitmap -> bitmapArr[0] = bitmap);
            while (bitmapArr[0] == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return bitmapArr[0];
        }


        public void onPostExecute(Bitmap bitmap) {
            PhotoEditorActivity.this.photo_editor_view.setImageSource(bitmap);
            PhotoEditorActivity.this.photo_editor_view.setFilterEffect("");
            PhotoEditorActivity.this.mLoading(false);
        }
    }

    class SaveStickerAsBitmap extends AsyncTask<Void, Void, Bitmap> {
        SaveStickerAsBitmap() {
        }

        public void onPreExecute() {
            PhotoEditorActivity.this.photo_editor_view.getGLSurfaceView().setAlpha(0.0f);
            PhotoEditorActivity.this.mLoading(true);
        }

        public Bitmap doInBackground(Void... voidArr) {
            final Bitmap[] bitmapArr = {null};
            while (bitmapArr[0] == null) {
                try {
                    PhotoEditorActivity.this.photoEditor.saveStickerAsBitmap(bitmap -> bitmapArr[0] = bitmap);
                    while (bitmapArr[0] == null) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                }
            }
            return bitmapArr[0];
        }

        public void onPostExecute(Bitmap bitmap) {
            PhotoEditorActivity.this.photo_editor_view.setImageSource(bitmap);
            PhotoEditorActivity.this.photo_editor_view.getStickers().clear();
            PhotoEditorActivity.this.photo_editor_view.getGLSurfaceView().setAlpha(1.0f);
            PhotoEditorActivity.this.mLoading(false);
            PhotoEditorActivity.this.updateLayout();
        }
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 123) {
            if (i2 == -1) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(intent.getData());
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    float width = (float) bitmap.getWidth();
                    float height = (float) bitmap.getHeight();
                    float max = Math.max(width / 1280.0f, height / 1280.0f);
                    if (max > 1.0f) {
                        bitmap = Bitmap.createScaledBitmap(bitmap, (int) (width / max), (int) (height / max), false);
                    }
                    if (SystemUtil.rotateBitmap(bitmap, new ExifInterface(inputStream).getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)) != bitmap) {
                        bitmap.recycle();
                        bitmap = null;
                    }
                    this.photo_editor_view.setImageSource(bitmap);
                    updateLayout();
                } catch (Exception e) {
                    e.printStackTrace();
                    MsgUtil.toastMsg(this, "Error: Can not open image");
                }
            } else {
                finish();
            }
        }
        else if (i == 900) {
            if (intent != null && intent.getStringExtra("MESSAGE").equals("done")){
                if (BitmapTransfer.bitmap != null){
                    new loadBitmap().execute(new Bitmap[]{BitmapTransfer.bitmap});
                }
            }

        }
    }

    public void isPermissionGranted(boolean z, String string) {
        if (z) {
            new SaveBitmap().execute();
        }
    }

    class loadBitmap extends AsyncTask<Bitmap, Bitmap, Bitmap> {
        loadBitmap() {
        }

        public void onPreExecute() {
            mLoading(true);
        }

        public Bitmap doInBackground(Bitmap... bitmaps) {
            try {
                Bitmap bitmap = bitmaps[0];//MediaStore.Images.Media.getBitmap(QueShotEditorActivity.this.getContentResolver(), fromFile);
                float width = (float) bitmap.getWidth();
                float height = (float) bitmap.getHeight();
                float max = Math.max(width / 1280.0f, height / 1280.0f);
                if (max > 1.0f) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, (int) (width / max), (int) (height / max), false);
                }
                return bitmap;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }


        public void onPostExecute(Bitmap bitmap) {
            photo_editor_view.setImageSource(bitmap);
            updateLayout();
        }
    }


    class OnLoadBitmapFromUri extends AsyncTask<String, Bitmap, Bitmap> {
        OnLoadBitmapFromUri() {
        }

        public void onPreExecute() {
            PhotoEditorActivity.this.mLoading(true);
        }

        public Bitmap doInBackground(String... strArr) {
            try {
                Uri fromFile = Uri.fromFile(new File(strArr[0]));
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(PhotoEditorActivity.this.getContentResolver(), fromFile);
                float width = (float) bitmap.getWidth();
                float height = (float) bitmap.getHeight();
                float max = Math.max(width / 1280.0f, height / 1280.0f);
                if (max > 1.0f) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, (int) (width / max), (int) (height / max), false);
                }
                Bitmap rotateBitmap = SystemUtil.rotateBitmap(bitmap, new ExifInterface(PhotoEditorActivity.this.getContentResolver().openInputStream(fromFile)).getAttributeInt(ExifInterface.TAG_ORIENTATION, 1));
                if (rotateBitmap != bitmap) {
                    bitmap.recycle();
                }
                return rotateBitmap;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }


        public void onPostExecute(Bitmap bitmap) {
            PhotoEditorActivity.this.photo_editor_view.setImageSource(bitmap);
            PhotoEditorActivity.this.updateLayout();
        }
    }

    public void updateLayout() {
        this.photo_editor_view.postDelayed(() -> {
            try {
                Display defaultDisplay = PhotoEditorActivity.this.getWindowManager().getDefaultDisplay();
                Point point = new Point();
                defaultDisplay.getSize(point);
                int i = point.x;
                int height = PhotoEditorActivity.this.relative_layout_wrapper_photo.getHeight();
                int i2 = PhotoEditorActivity.this.photo_editor_view.getGLSurfaceView().getRenderViewport().width;
                float f = (float) PhotoEditorActivity.this.photo_editor_view.getGLSurfaceView().getRenderViewport().height;
                float f2 = (float) i2;
                if (((int) ((((float) i) * f) / f2)) <= height) {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
                    layoutParams.addRule(13);
                    PhotoEditorActivity.this.photo_editor_view.setLayoutParams(layoutParams);
                    PhotoEditorActivity.this.photo_editor_view.setVisibility(View.VISIBLE);
                } else {
                    RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams((int) ((((float) height) * f2) / f), -1);
                    layoutParams2.addRule(13);
                    PhotoEditorActivity.this.photo_editor_view.setLayoutParams(layoutParams2);
                    PhotoEditorActivity.this.photo_editor_view.setVisibility(View.VISIBLE);
                }
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
            PhotoEditorActivity.this.mLoading(false);
        }, 300);
    }


    class SaveBitmap extends AsyncTask<Void, String, String> {
        SaveBitmap() {
        }

        public void onPreExecute() {
            mLoading(true);
        }

        public String doInBackground(Void... voids) {
            try {
                return SaveFileUtils.saveBitmapFileEditor(PhotoEditorActivity.this, PhotoEditorActivity.this.photo_editor_view.getCurrentBitmap(), new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date()), null).getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        public void onPostExecute(String string) {
            mLoading(false);
            if (string == null) {
                Toast.makeText(PhotoEditorActivity.this.getApplicationContext(), "Oop! Something went wrong", Toast.LENGTH_LONG).show();
                return;
            }
            Intent i = new Intent(PhotoEditorActivity.this, ShareActivity.class);
            i.putExtra("path", string);
            PhotoEditorActivity.this.startActivity(i);
        }

    }

    public void mLoading(boolean z) {
        if (z) {
            getWindow().setFlags(16, 16);
            this.relative_layout_loading.setVisibility(View.VISIBLE);
            return;
        }
        getWindow().clearFlags(16);
        this.relative_layout_loading.setVisibility(View.GONE);
    }
}
