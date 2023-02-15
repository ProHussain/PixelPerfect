package com.example.pixelperfect.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pixelperfect.Adapters.FontAdapter;
import com.example.pixelperfect.Adapters.ShadowAdapter;
import com.example.pixelperfect.Adapters.TextBackgroundAdapter;
import com.example.pixelperfect.Adapters.TextColorAdapter;
import com.example.pixelperfect.Assets.FontFileAsset;
import com.example.pixelperfect.Editor.EditText;
import com.example.pixelperfect.Editor.Text;
import com.example.pixelperfect.R;
import com.example.pixelperfect.Utils.PreferenceUtil;
import com.example.pixelperfect.Utils.SystemUtil;

import java.util.ArrayList;
import java.util.List;

public class TextFragment extends DialogFragment implements View.OnClickListener, FontAdapter.ItemClickListener,
        ShadowAdapter.ShadowItemClickListener, TextColorAdapter.ColorListener, TextBackgroundAdapter.BackgroundColorListener {
    public static final String EXTRA_COLOR_CODE = "extra_color_code";
    public static final String EXTRA_INPUT_TEXT = "extra_input_text";
    public static final String TAG = "TextFragment";
    ConstraintLayout linear_layout_edit_text_tools;

    public Text polishText;
    SeekBar seekbar_radius;
    SeekBar seekbar_height;
    SeekBar seekbar_background_opacity;
    SeekBar seekbar_width;
    ImageView image_view_color;
    TextView textViewFont;
    TextView textViewColor;
    TextView textViewBg;
    TextView textViewShadow;
    RelativeLayout relativeLayoutBg;
    ImageView image_view_adjust;
    ScrollView scroll_view_change_font_adjust;
    LinearLayout scroll_view_change_font_layout;
    ImageView image_view_align_left;
    ImageView image_view_align_center;
    ImageView image_view_align_right;
    public RecyclerView recycler_view_color;
    public RecyclerView recycler_view_background;
    private FontAdapter fontAdapter;
    private ShadowAdapter shadowAdapter;
    LinearLayout linear_layout_preview;
    RecyclerView recycler_view_fonts;
    RecyclerView recycler_view_shadow;
    EditText add_text_edit_text;
    private InputMethodManager inputMethodManager;
    TextView text_view_preview_effect;
    TextView textViewSeekBarSize;
    TextView textViewSeekBarColor;
    TextView textViewSeekBarBackground;
    TextView textViewSeekBarRadius;
    TextView textViewSeekBarWith;
    TextView textViewSeekBarHeight;
    ImageView image_view_save_change;
    ImageView image_view_keyboard;
    CheckBox checkbox_background;
    private TextEditor textEditor;
    private List<ImageView> textFunctions;
    SeekBar seekbar_text_size;
    SeekBar textColorOpacity;

    public interface TextEditor {
        void onBackButton();
        void onDone(Text polishText);
    }

    public void onItemClick(View view, int i) {
        FontFileAsset.setFontByName(requireContext(), this.text_view_preview_effect, FontFileAsset.getListFonts().get(i));
        this.polishText.setFontName(FontFileAsset.getListFonts().get(i));
        this.polishText.setFontIndex(i);
    }

    public static TextFragment show(@NonNull AppCompatActivity appCompatActivity, @NonNull String str, @ColorInt int i) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_INPUT_TEXT, str);
        bundle.putInt(EXTRA_COLOR_CODE, i);
        TextFragment textFragment = new TextFragment();
        textFragment.setArguments(bundle);
        textFragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return textFragment;
    }

    public static TextFragment show(@NonNull AppCompatActivity appCompatActivity, Text addTextProperties) {
        TextFragment addTextFragment = new TextFragment();
        addTextFragment.setPolishText(addTextProperties);
        addTextFragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return addTextFragment;
    }

    public void onShadowItemClick(View view, int i) {
        Text.TextShadow textShadow = Text.getLstTextShadow().get(i);
        this.text_view_preview_effect.setShadowLayer((float) textShadow.getRadius(), (float) textShadow.getDx(), (float) textShadow.getDy(), textShadow.getColorShadow());
        this.text_view_preview_effect.invalidate();
        this.polishText.setTextShadow(textShadow);
        this.polishText.setTextShadowIndex(i);
    }

    public static TextFragment show(@NonNull AppCompatActivity appCompatActivity) {
        return show(appCompatActivity, "Test", ContextCompat.getColor(appCompatActivity, R.color.white));
    }

    public void setPolishText(Text polishText) {
        this.polishText = polishText;
    }

    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        getDialog().getWindow().requestFeature(1);
        getDialog().getWindow().setFlags(1024, 1024);
        return layoutInflater.inflate(R.layout.fragment_text, viewGroup, false);
    }

    public void dismissAndShowSticker() {
        if (this.textEditor != null) {
            this.textEditor.onBackButton();
        }
        dismiss();
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (this.polishText == null) {
            this.polishText = Text.getDefaultProperties();
        }

        this.add_text_edit_text = view.findViewById(R.id.add_text_edit_text);
        this.image_view_keyboard = view.findViewById(R.id.image_view_keyboard);
        this.image_view_color = view.findViewById(R.id.image_view_color);
        this.image_view_align_left = view.findViewById(R.id.imageViewAlignLeft);
        this.image_view_align_center = view.findViewById(R.id.imageViewAlignCenter);
        this.image_view_align_right = view.findViewById(R.id.imageViewAlignRight);
        this.textViewFont = view.findViewById(R.id.textViewFont);
        this.textViewColor = view.findViewById(R.id.textViewColor);
        this.textViewBg = view.findViewById(R.id.textViewBg);
        this.textViewShadow = view.findViewById(R.id.textViewShadow);
        this.relativeLayoutBg = view.findViewById(R.id.relativeLayoutBg);
        this.textViewSeekBarSize = view.findViewById(R.id.seekbarSize);
        this.textViewSeekBarColor = view.findViewById(R.id.seekbarColor);
        this.textViewSeekBarBackground = view.findViewById(R.id.seekbarBackground);
        this.textViewSeekBarRadius = view.findViewById(R.id.seekbarRadius);
        this.textViewSeekBarWith = view.findViewById(R.id.seekbarWith);
        this.textViewSeekBarHeight = view.findViewById(R.id.seekbarHeight);
        this.image_view_adjust = view.findViewById(R.id.image_view_adjust);
        this.image_view_save_change = view.findViewById(R.id.image_view_save_change);
        this.scroll_view_change_font_layout = view.findViewById(R.id.scroll_view_change_font_layout);
        this.scroll_view_change_font_adjust = view.findViewById(R.id.scroll_view_change_color_adjust);
        this.linear_layout_edit_text_tools = view.findViewById(R.id.linear_layout_edit_text_tools);
        this.recycler_view_fonts = view.findViewById(R.id.recycler_view_fonts);
        this.recycler_view_shadow = view.findViewById(R.id.recycler_view_shadow);

        this.textColorOpacity = view.findViewById(R.id.seekbar_text_opacity);
        this.text_view_preview_effect = view.findViewById(R.id.text_view_preview_effect);
        this.linear_layout_preview = view.findViewById(R.id.linear_layout_preview);
        this.checkbox_background = view.findViewById(R.id.checkbox_background);
        this.seekbar_width = view.findViewById(R.id.seekbar_width);
        this.seekbar_height = view.findViewById(R.id.seekbar_height);
        this.seekbar_background_opacity = view.findViewById(R.id.seekbar_background_opacity);
        this.seekbar_text_size = view.findViewById(R.id.seekbar_text_size);
        this.seekbar_radius = view.findViewById(R.id.seekbar_radius);

        this.recycler_view_color = view.findViewById(R.id.recyclerViewColor);
        this.recycler_view_color.setLayoutManager(new GridLayoutManager(getContext(), 5));
        this.recycler_view_color.setAdapter(new TextColorAdapter(getContext(), this));

        this.recycler_view_background = view.findViewById(R.id.recyclerViewBg);
        this.recycler_view_background.setLayoutManager(new GridLayoutManager(getContext(), 5));
        this.recycler_view_background.setAdapter(new TextBackgroundAdapter(getContext(), this));

        this.add_text_edit_text.setTextFragment(this);
        initAddTextLayout();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(new DisplayMetrics());
        this.inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        setDefaultStyleForEdittext();
        this.inputMethodManager.toggleSoftInput(2, 0);
        this.recycler_view_fonts.setLayoutManager(new GridLayoutManager(getContext(), 5));
        this.fontAdapter = new FontAdapter(getContext(), FontFileAsset.getListFonts());
        this.fontAdapter.setClickListener(this);
        this.recycler_view_fonts.setAdapter(this.fontAdapter);
        this.recycler_view_shadow.setLayoutManager(new GridLayoutManager(getContext(), 5));
        this.shadowAdapter = new ShadowAdapter(getContext(), Text.getLstTextShadow());
        this.shadowAdapter.setClickListener(this);
        this.recycler_view_shadow.setAdapter(this.shadowAdapter);

        this.textColorOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                String value = String.valueOf(i);
                textViewSeekBarColor.setText(value);
                int i2 = 255 - i;
                TextFragment.this.polishText.setTextAlpha(i2);
                TextFragment.this.text_view_preview_effect.setTextColor(Color.argb(i2, Color.red(TextFragment.this.polishText.getTextColor()), Color.green(TextFragment.this.polishText.getTextColor()), Color.blue(TextFragment.this.polishText.getTextColor())));
            }
        });
        this.add_text_edit_text.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                TextFragment.this.text_view_preview_effect.setText(charSequence.toString());
                TextFragment.this.polishText.setText(charSequence.toString());
            }
        });
        this.checkbox_background.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (!z) {
                    TextFragment.this.polishText.setShowBackground(false);
                    TextFragment.this.text_view_preview_effect.setBackgroundResource(0);
                    TextFragment.this.text_view_preview_effect.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
                } else if (TextFragment.this.checkbox_background.isPressed() || TextFragment.this.polishText.isShowBackground()) {
                    TextFragment.this.polishText.setShowBackground(true);
                    TextFragment.this.initPreviewText();
                } else {
                    TextFragment.this.checkbox_background.setChecked(false);
                    TextFragment.this.polishText.setShowBackground(false);
                    TextFragment.this.initPreviewText();
                }
            }
        });
        this.seekbar_width.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                String value = String.valueOf(i);
                textViewSeekBarWith.setText(value);
                TextFragment.this.text_view_preview_effect.setPadding(SystemUtil.dpToPx(TextFragment.this.requireContext(), i), TextFragment.this.text_view_preview_effect.getPaddingTop(), SystemUtil.dpToPx(TextFragment.this.getContext(), i), TextFragment.this.text_view_preview_effect.getPaddingBottom());
                TextFragment.this.polishText.setPaddingWidth(i);
            }
        });
        this.seekbar_height.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                String value = String.valueOf(i);
                textViewSeekBarHeight.setText(value);
                TextFragment.this.text_view_preview_effect.setPadding(TextFragment.this.text_view_preview_effect.getPaddingLeft(), SystemUtil.dpToPx(TextFragment.this.requireContext(), i), TextFragment.this.text_view_preview_effect.getPaddingRight(), SystemUtil.dpToPx(TextFragment.this.getContext(), i));
                TextFragment.this.polishText.setPaddingHeight(i);
            }
        });
        this.seekbar_background_opacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                String value = String.valueOf(i);
                textViewSeekBarBackground.setText(value);
                TextFragment.this.polishText.setBackgroundAlpha(255 - i);
                    int red = Color.red(TextFragment.this.polishText.getBackgroundColor());
                    int green = Color.green(TextFragment.this.polishText.getBackgroundColor());
                    int blue = Color.blue(TextFragment.this.polishText.getBackgroundColor());
                    GradientDrawable gradientDrawable = new GradientDrawable();
                    gradientDrawable.setColor(Color.argb(TextFragment.this.polishText.getBackgroundAlpha(), red, green, blue));
                    gradientDrawable.setCornerRadius((float) SystemUtil.dpToPx(TextFragment.this.requireContext(), TextFragment.this.polishText.getBackgroundBorder()));
                    TextFragment.this.text_view_preview_effect.setBackground(gradientDrawable);

            }
        });
        this.seekbar_text_size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                String value = String.valueOf(i);
                textViewSeekBarSize.setText(value);
                int i2 = 15;
                if (i >= 15) {
                    i2 = i;
                }
                TextFragment.this.text_view_preview_effect.setTextSize((float) i2);
                TextFragment.this.polishText.setTextSize(i2);
            }
        });
        this.seekbar_radius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                String value = String.valueOf(i);
                textViewSeekBarRadius.setText(value);
                TextFragment.this.polishText.setBackgroundBorder(i);
                    GradientDrawable gradientDrawable = new GradientDrawable();
                    gradientDrawable.setCornerRadius((float) SystemUtil.dpToPx(TextFragment.this.requireContext(), i));
                    gradientDrawable.setColor(Color.argb(TextFragment.this.polishText.getBackgroundAlpha(), Color.red(TextFragment.this.polishText.getBackgroundColor()), Color.green(TextFragment.this.polishText.getBackgroundColor()), Color.blue(TextFragment.this.polishText.getBackgroundColor())));
                    TextFragment.this.text_view_preview_effect.setBackground(gradientDrawable);

            }
        });
        if (PreferenceUtil.getKeyboard(requireContext()) > 0) {
            updateAddTextBottomToolbarHeight(PreferenceUtil.getKeyboard(getContext()));
        }
        initPreviewText();
    }

    @SuppressLint("WrongConstant")
    public void initPreviewText() {
        if (this.polishText.isShowBackground()) {
            if (this.polishText.getBackgroundColor() != 0) {
                this.text_view_preview_effect.setBackgroundColor(this.polishText.getBackgroundColor());
            }
            if (this.polishText.getBackgroundAlpha() < 255) {
                this.text_view_preview_effect.setBackgroundColor(Color.argb(this.polishText.getBackgroundAlpha(), Color.red(this.polishText.getBackgroundColor()), Color.green(this.polishText.getBackgroundColor()), Color.blue(this.polishText.getBackgroundColor())));
            }
            if (this.polishText.getBackgroundBorder() > 0) {
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setCornerRadius((float) SystemUtil.dpToPx(requireContext(), this.polishText.getBackgroundBorder()));
                gradientDrawable.setColor(Color.argb(this.polishText.getBackgroundAlpha(), Color.red(this.polishText.getBackgroundColor()), Color.green(this.polishText.getBackgroundColor()), Color.blue(this.polishText.getBackgroundColor())));
                this.text_view_preview_effect.setBackground(gradientDrawable);
            }
        }
        if (this.polishText.getPaddingHeight() > 0) {
            this.text_view_preview_effect.setPadding(this.text_view_preview_effect.getPaddingLeft(), this.polishText.getPaddingHeight(), this.text_view_preview_effect.getPaddingRight(), this.polishText.getPaddingHeight());
            this.seekbar_height.setProgress(this.polishText.getPaddingHeight());
        }
        if (this.polishText.getPaddingWidth() > 0) {
            this.text_view_preview_effect.setPadding(this.polishText.getPaddingWidth(), this.text_view_preview_effect.getPaddingTop(), this.polishText.getPaddingWidth(), this.text_view_preview_effect.getPaddingBottom());
            this.seekbar_width.setProgress(this.polishText.getPaddingWidth());
        }
        if (this.polishText.getText() != null) {
            this.text_view_preview_effect.setText(this.polishText.getText());
            this.add_text_edit_text.setText(this.polishText.getText());
        }
        if (this.polishText.getTextShader() != null) {
            this.text_view_preview_effect.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            this.text_view_preview_effect.getPaint().setShader(this.polishText.getTextShader());
        }
        if (this.polishText.getTextAlign() == 4) {
            this.image_view_align_center.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_center_select));
        } else if (this.polishText.getTextAlign() == 3) {
            this.image_view_align_right.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_right));
        } else if (this.polishText.getTextAlign() == 2) {
            this.image_view_align_left.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_left));
        }
        this.text_view_preview_effect.setPadding(SystemUtil.dpToPx(getContext(), this.polishText.getPaddingWidth()), this.text_view_preview_effect.getPaddingTop(), SystemUtil.dpToPx(getContext(), this.polishText.getPaddingWidth()), this.text_view_preview_effect.getPaddingBottom());
        this.text_view_preview_effect.setTextColor(this.polishText.getTextColor());
        this.text_view_preview_effect.setTextAlignment(this.polishText.getTextAlign());
        this.text_view_preview_effect.setTextSize((float) this.polishText.getTextSize());
        FontFileAsset.setFontByName(getContext(), this.text_view_preview_effect, this.polishText.getFontName());
        this.text_view_preview_effect.invalidate();
    }

    private void setDefaultStyleForEdittext() {
        this.add_text_edit_text.requestFocus();
        this.add_text_edit_text.setTextSize(20.0f);
        this.add_text_edit_text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        this.add_text_edit_text.setTextColor(Color.parseColor("#ffffff"));
    }

    private void initAddTextLayout() {
        this.textFunctions = getTextFunctions();
        this.image_view_keyboard.setOnClickListener(this);
        this.textViewFont.setOnClickListener(this);
        this.textViewColor.setOnClickListener(this);
        this.textViewBg.setOnClickListener(this);
        this.textViewShadow.setOnClickListener(this);
        this.image_view_adjust.setOnClickListener(this);
        this.image_view_color.setOnClickListener(this);
        this.image_view_save_change.setOnClickListener(this);
        this.image_view_align_left.setOnClickListener(this);
        this.image_view_align_center.setOnClickListener(this);
        this.image_view_align_right.setOnClickListener(this);
        this.scroll_view_change_font_layout.setVisibility(View.GONE);
        this.scroll_view_change_font_adjust.setVisibility(View.GONE);
        this.seekbar_width.setProgress(this.polishText.getPaddingWidth());
    }

    @Override
    public void onColorSelected(int item, TextColorAdapter.SquareView squareView) {
        if (squareView.isColor) {
            TextFragment.this.text_view_preview_effect.setTextColor(squareView.drawableId);
            TextFragment.this.polishText.setTextColor(squareView.drawableId);
            TextFragment.this.text_view_preview_effect.getPaint().setShader(null);
            TextFragment.this.polishText.setTextShader(null);
        }  else {
            TextFragment.this.text_view_preview_effect.setTextColor(squareView.drawableId);
            TextFragment.this.polishText.setTextColor(squareView.drawableId);
            TextFragment.this.text_view_preview_effect.getPaint().setShader(null);
            TextFragment.this.polishText.setTextShader(null);
        }
    }

    @Override
    public void onBackgroundColorSelected(int item, TextBackgroundAdapter.SquareView squareView) {
        if (squareView.isColor) {
            TextFragment.this.text_view_preview_effect.setBackgroundColor(squareView.drawableId);
            TextFragment.this.polishText.setBackgroundColor(squareView.drawableId);
            TextFragment.this.seekbar_radius.setEnabled(true);
            TextFragment.this.polishText.setShowBackground(true);
            if (!TextFragment.this.checkbox_background.isChecked()) {
                TextFragment.this.checkbox_background.setChecked(true);
            }
            int red = Color.red(TextFragment.this.polishText.getBackgroundColor());
            int green = Color.green(TextFragment.this.polishText.getBackgroundColor());
            int blue = Color.blue(TextFragment.this.polishText.getBackgroundColor());
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(Color.argb(TextFragment.this.polishText.getBackgroundAlpha(), red, green, blue));
            gradientDrawable.setCornerRadius((float) SystemUtil.dpToPx(TextFragment.this.requireContext(), TextFragment.this.polishText.getBackgroundBorder()));
            TextFragment.this.text_view_preview_effect.setBackground(gradientDrawable);

        }  else {
            TextFragment.this.text_view_preview_effect.setBackgroundColor(squareView.drawableId);
            TextFragment.this.polishText.setBackgroundColor(squareView.drawableId);
            TextFragment.this.seekbar_radius.setEnabled(true);
            TextFragment.this.polishText.setShowBackground(true);
            if (!TextFragment.this.checkbox_background.isChecked()) {
                TextFragment.this.checkbox_background.setChecked(true);
            }
            int red = Color.red(TextFragment.this.polishText.getBackgroundColor());
            int green = Color.green(TextFragment.this.polishText.getBackgroundColor());
            int blue = Color.blue(TextFragment.this.polishText.getBackgroundColor());
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(Color.argb(TextFragment.this.polishText.getBackgroundAlpha(), red, green, blue));
            gradientDrawable.setCornerRadius((float) SystemUtil.dpToPx(TextFragment.this.requireContext(), TextFragment.this.polishText.getBackgroundBorder()));
            TextFragment.this.text_view_preview_effect.setBackground(gradientDrawable);
        }
    }

    public void onResume() {
        super.onResume();
        ViewCompat.setOnApplyWindowInsetsListener(getDialog().getWindow().getDecorView(), new OnApplyWindowInsetsListener() {
            public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                return ViewCompat.onApplyWindowInsets(
                        TextFragment.this.getDialog().getWindow().getDecorView(),
                        windowInsetsCompat.inset(windowInsetsCompat.getSystemWindowInsetLeft(), 0, windowInsetsCompat.getSystemWindowInsetRight(), windowInsetsCompat.getSystemWindowInsetBottom()));
            }
        });

    }

    public void updateAddTextBottomToolbarHeight(final int i) {
        new Handler().post(new Runnable() {
            public void run() {
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) TextFragment.this.linear_layout_edit_text_tools.getLayoutParams();
                layoutParams.bottomMargin = i;
                TextFragment.this.linear_layout_edit_text_tools.setLayoutParams(layoutParams);
                TextFragment.this.linear_layout_edit_text_tools.invalidate();
                TextFragment.this.scroll_view_change_font_layout.invalidate();
                TextFragment.this.scroll_view_change_font_adjust.invalidate();
                Log.i("HIHIH", i + "");
            }
        });
    }

    public void setOnTextEditorListener(TextEditor textEditor2) {
        this.textEditor = textEditor2;
    }

    @SuppressLint("WrongConstant")
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewFont:
                this.textViewFont.setBackgroundResource(R.drawable.background_selected);
                this.textViewShadow.setBackgroundResource(R.drawable.background_unslelected);
                this.textViewColor.setBackgroundResource(R.drawable.background_unslelected);
                this.textViewBg.setBackgroundResource(R.drawable.background_unslelected);
                this.recycler_view_fonts.setVisibility(View.VISIBLE);
                this.recycler_view_shadow.setVisibility(View.GONE);
                this.recycler_view_color.setVisibility(View.GONE);
                this.relativeLayoutBg.setVisibility(View.GONE);
                return;
            case R.id.textViewShadow:
                this.textViewFont.setBackgroundResource(R.drawable.background_unslelected);
                this.textViewShadow.setBackgroundResource(R.drawable.background_selected);
                this.textViewColor.setBackgroundResource(R.drawable.background_unslelected);
                this.textViewBg.setBackgroundResource(R.drawable.background_unslelected);
                this.recycler_view_fonts.setVisibility(View.GONE);
                this.recycler_view_shadow.setVisibility(View.VISIBLE);
                this.recycler_view_color.setVisibility(View.GONE);
                this.relativeLayoutBg.setVisibility(View.GONE);
                return;
            case R.id.textViewColor:
                this.textViewFont.setBackgroundResource(R.drawable.background_unslelected);
                this.textViewColor.setBackgroundResource(R.drawable.background_selected);
                this.textViewShadow.setBackgroundResource(R.drawable.background_unslelected);
                this.textViewBg.setBackgroundResource(R.drawable.background_unslelected);
                this.recycler_view_fonts.setVisibility(View.GONE);
                this.recycler_view_shadow.setVisibility(View.GONE);
                this.recycler_view_color.setVisibility(View.VISIBLE);
                this.relativeLayoutBg.setVisibility(View.GONE);
                return;
            case R.id.textViewBg:
                this.textViewFont.setBackgroundResource(R.drawable.background_unslelected);
                this.textViewBg.setBackgroundResource(R.drawable.background_selected);
                this.textViewColor.setBackgroundResource(R.drawable.background_unslelected);
                this.textViewShadow.setBackgroundResource(R.drawable.background_unslelected);
                this.recycler_view_fonts.setVisibility(View.GONE);
                this.recycler_view_shadow.setVisibility(View.GONE);
                this.recycler_view_color.setVisibility(View.GONE);
                this.relativeLayoutBg.setVisibility(View.VISIBLE);
                return;
            case R.id.imageViewAlignLeft:
                if (this.polishText.getTextAlign() == 3 || this.polishText.getTextAlign() == 4) {
                    this.polishText.setTextAlign(2);
                    this.image_view_align_left.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_left_select));
                    this.image_view_align_center.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_center));
                    this.image_view_align_right.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_right));
                }
                this.text_view_preview_effect.setTextAlignment(this.polishText.getTextAlign());
                TextView textView = this.text_view_preview_effect;
                textView.setText(this.text_view_preview_effect.getText().toString().trim() + " ");
                this.text_view_preview_effect.setText(this.text_view_preview_effect.getText().toString().trim());
                return;
            case R.id.imageViewAlignCenter:
                if (this.polishText.getTextAlign() == 2 || this.polishText.getTextAlign() == 3) {
                    this.polishText.setTextAlign(4);
                    this.image_view_align_center.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_center_select));
                    this.image_view_align_left.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_left));
                    this.image_view_align_right.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_right));
                }
                this.text_view_preview_effect.setTextAlignment(this.polishText.getTextAlign());
                TextView textViews = this.text_view_preview_effect;
                textViews.setText(this.text_view_preview_effect.getText().toString().trim() + " ");
                this.text_view_preview_effect.setText(this.text_view_preview_effect.getText().toString().trim());
                return;
            case R.id.imageViewAlignRight:
                if (this.polishText.getTextAlign() == 4 || this.polishText.getTextAlign() == 2) {
                    this.polishText.setTextAlign(3);
                    this.image_view_align_left.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_left));
                    this.image_view_align_center.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_center));
                    this.image_view_align_right.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_format_align_right_select));
                }
                this.text_view_preview_effect.setTextAlignment(this.polishText.getTextAlign());
                TextView textViewz = this.text_view_preview_effect;
                textViewz.setText(this.text_view_preview_effect.getText().toString().trim() + " ");
                this.text_view_preview_effect.setText(this.text_view_preview_effect.getText().toString().trim());
                return;
            case R.id.image_view_adjust:
                image_view_keyboard.setBackgroundResource(R.drawable.background_unslelected);
                image_view_adjust.setBackgroundResource(R.drawable.background_selected_color);
                image_view_color.setBackgroundResource(R.drawable.background_unslelected);
                image_view_keyboard.setColorFilter(getResources().getColor(R.color.white));
                image_view_adjust.setColorFilter(getResources().getColor(R.color.mainColor));
                image_view_color.setColorFilter(getResources().getColor(R.color.white));
                this.inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                this.scroll_view_change_font_adjust.setVisibility(View.VISIBLE);
                this.scroll_view_change_font_layout.setVisibility(View.GONE);
                this.seekbar_background_opacity.setProgress(255 - this.polishText.getBackgroundAlpha());
                this.seekbar_text_size.setProgress(this.polishText.getTextSize());
                this.seekbar_radius.setProgress(this.polishText.getBackgroundBorder());
                this.seekbar_width.setProgress(this.polishText.getPaddingWidth());
                this.seekbar_height.setProgress(this.polishText.getPaddingHeight());
                this.textColorOpacity.setProgress(255 - this.polishText.getTextAlpha());
                toggleTextEditEditable(false);
                return;
            case R.id.image_view_color:
                image_view_keyboard.setBackgroundResource(R.drawable.background_unslelected);
                image_view_adjust.setBackgroundResource(R.drawable.background_unslelected);
                image_view_color.setBackgroundResource(R.drawable.background_selected_color);
                image_view_keyboard.setColorFilter(getResources().getColor(R.color.white));
                image_view_adjust.setColorFilter(getResources().getColor(R.color.white));
                image_view_color.setColorFilter(getResources().getColor(R.color.mainColor));

                this.inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                this.scroll_view_change_font_layout.setVisibility(View.VISIBLE);
                this.scroll_view_change_font_adjust.setVisibility(View.GONE);
                toggleTextEditEditable(false);
                this.add_text_edit_text.setVisibility(View.GONE);
                this.shadowAdapter.setSelectedItem(this.polishText.getFontIndex());
                this.fontAdapter.setSelectedItem(this.polishText.getFontIndex());
                this.checkbox_background.setChecked(this.polishText.isShowBackground());
                this.checkbox_background.setChecked(this.polishText.isShowBackground());
                return;
            case R.id.image_view_save_change:
                if (this.polishText.getText() == null || this.polishText.getText().length() == 0) {
                    this.inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    this.textEditor.onBackButton();
                    dismiss();
                    return;
                }
                this.polishText.setTextWidth(this.text_view_preview_effect.getMeasuredWidth());
                this.polishText.setTextHeight(this.text_view_preview_effect.getMeasuredHeight());
                this.inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                this.textEditor.onDone(this.polishText);
                dismiss();
                return;
            case R.id.image_view_keyboard:
                image_view_keyboard.setBackgroundResource(R.drawable.background_selected_color);
                image_view_adjust.setBackgroundResource(R.drawable.background_unslelected);
                image_view_color.setBackgroundResource(R.drawable.background_unslelected);
                image_view_keyboard.setColorFilter(getResources().getColor(R.color.mainColor));
                image_view_adjust.setColorFilter(getResources().getColor(R.color.white));
                image_view_color.setColorFilter(getResources().getColor(R.color.white));
                toggleTextEditEditable(true);
                this.add_text_edit_text.setVisibility(View.VISIBLE);
                this.add_text_edit_text.requestFocus();
                this.scroll_view_change_font_layout.setVisibility(View.GONE);
                this.scroll_view_change_font_adjust.setVisibility(View.GONE);
                this.linear_layout_edit_text_tools.invalidate();
                this.inputMethodManager.toggleSoftInput(2, 0);
                return;
            default:
        }
    }

    private void toggleTextEditEditable(boolean z) {
        this.add_text_edit_text.setFocusable(z);
        this.add_text_edit_text.setFocusableInTouchMode(z);
        this.add_text_edit_text.setClickable(z);

    }

    private List<ImageView> getTextFunctions() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.image_view_keyboard);
        arrayList.add(this.image_view_color);
        arrayList.add(this.image_view_adjust);
        arrayList.add(this.image_view_save_change);
        return arrayList;
    }

}
