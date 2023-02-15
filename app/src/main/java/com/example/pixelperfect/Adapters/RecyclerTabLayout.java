package com.example.pixelperfect.Adapters;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.pixelperfect.R;

public class RecyclerTabLayout extends RecyclerView {
    protected static final float DEFAULT_POSITION_THRESHOLD = 0.6f;
    protected static final long DEFAULT_SCROLL_DURATION = 200;
    protected static final float POSITION_THRESHOLD_ALLOWABLE = 0.001f;
    protected Adapter<?> mAdapter;
    protected int mIndicatorGap;
    protected int mIndicatorHeight;
    protected Paint mIndicatorPaint;
    protected int mIndicatorPosition;
    protected int mIndicatorScroll;
    protected LinearLayoutManager mLinearLayoutManager;
    private int mOldPosition;
    protected float mOldPositionOffset;
    private int mOldScrollOffset;
    protected float mPositionThreshold;
    protected RecyclerOnScrollListener mRecyclerOnScrollListener;
    protected boolean mRequestScrollToTab;
    protected boolean mScrollEanbled;
    protected int mTabBackgroundResId;
    protected int mTabMaxWidth;
    protected int mTabMinWidth;
    protected int mTabOnScreenLimit;
    protected int mTabPaddingBottom;
    protected int mTabPaddingEnd;
    protected int mTabPaddingStart;
    protected int mTabPaddingTop;
    protected int mTabSelectedTextColor;
    protected boolean mTabSelectedTextColorSet;
    protected int mTabTextAppearance;
    protected ViewPager mViewPager;

    public RecyclerTabLayout(Context context) {
        this(context, null);
    }

    public RecyclerTabLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public RecyclerTabLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setWillNotDraw(false);
        this.mIndicatorPaint = new Paint();
        getAttributes(context, attributeSet, i);
        this.mLinearLayoutManager = new LinearLayoutManager(getContext()) {
            public boolean canScrollHorizontally() {
                return RecyclerTabLayout.this.mScrollEanbled;
            }
        };
        this.mLinearLayoutManager.setOrientation(HORIZONTAL);
        setLayoutManager(this.mLinearLayoutManager);
        setItemAnimator(null);
        this.mPositionThreshold = DEFAULT_POSITION_THRESHOLD;
    }

    @SuppressLint("ResourceType")
    private void getAttributes(Context context, AttributeSet attributeSet, int i) {
        final int[] rtl_RecyclerTabLayout = {R.attr.mScrollEnabled, R.attr.mTabBackground, R.attr.mTabIndicatorColor, R.attr.mTabIndicatorHeight, R.attr.mTabMaxWidth, R.attr.mTabMinWidth, R.attr.mTabOnScreenLimit, R.attr.mTabPadding, R.attr.mTabPaddingBottom, R.attr.mTabPaddingEnd, R.attr.mTabPaddingStart, R.attr.mTabPaddingTop, R.attr.mTabSelectedTextColor, R.attr.mTabTextAppearance};

        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, rtl_RecyclerTabLayout, i, R.style.mRecyclerTabLayout);
        setIndicatorColor(obtainStyledAttributes.getColor(2, 0));
        setIndicatorHeight(obtainStyledAttributes.getDimensionPixelSize(3, 0));
        this.mTabTextAppearance = obtainStyledAttributes.getResourceId(13, R.style.mRecyclerTabLayout_Tab);
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(7, 0);
        this.mTabPaddingBottom = dimensionPixelSize;
        this.mTabPaddingEnd = dimensionPixelSize;
        this.mTabPaddingTop = dimensionPixelSize;
        this.mTabPaddingStart = dimensionPixelSize;
        this.mTabPaddingStart = obtainStyledAttributes.getDimensionPixelSize(10, this.mTabPaddingStart);
        this.mTabPaddingTop = obtainStyledAttributes.getDimensionPixelSize(11, this.mTabPaddingTop);
        this.mTabPaddingEnd = obtainStyledAttributes.getDimensionPixelSize(9, this.mTabPaddingEnd);
        this.mTabPaddingBottom = obtainStyledAttributes.getDimensionPixelSize(8, this.mTabPaddingBottom);
        if (obtainStyledAttributes.hasValue(12)) {
            this.mTabSelectedTextColor = obtainStyledAttributes.getColor(12, 0);
            this.mTabSelectedTextColorSet = true;
        }
        this.mTabOnScreenLimit = obtainStyledAttributes.getInteger(6, 0);
        if (this.mTabOnScreenLimit == 0) {
            this.mTabMinWidth = obtainStyledAttributes.getDimensionPixelSize(5, 0);
            this.mTabMaxWidth = obtainStyledAttributes.getDimensionPixelSize(4, 0);
        }
        this.mTabBackgroundResId = obtainStyledAttributes.getResourceId(1, 0);
        this.mScrollEanbled = obtainStyledAttributes.getBoolean(0, true);
        obtainStyledAttributes.recycle();
    }


    public void onDetachedFromWindow() {
        if (this.mRecyclerOnScrollListener != null) {
            removeOnScrollListener(this.mRecyclerOnScrollListener);
            this.mRecyclerOnScrollListener = null;
        }
        super.onDetachedFromWindow();
    }

    public void setIndicatorColor(int i) {
        this.mIndicatorPaint.setColor(i);
    }

    public void setIndicatorHeight(int i) {
        this.mIndicatorHeight = i;
    }

    public void setPositionThreshold(float f) {
        this.mPositionThreshold = f;
    }

    public void setUpWithAdapter(Adapter<?> adapter) {
        this.mAdapter = adapter;
        this.mViewPager = adapter.getViewPager();
        if (this.mViewPager.getAdapter() != null) {
            this.mViewPager.addOnPageChangeListener(new ViewPagerOnPageChangeListener(this));
            setAdapter(adapter);
            scrollToTab(this.mViewPager.getCurrentItem());
            return;
        }
        throw new IllegalArgumentException("ViewPager does not have a PagerAdapter set");
    }

    public void setCurrentItem(int i, boolean z) {
        if (this.mViewPager != null) {
            this.mViewPager.setCurrentItem(i, z);
            scrollToTab(this.mViewPager.getCurrentItem());
        } else if (!z || i == this.mIndicatorPosition) {
            scrollToTab(i);
        } else {
            startAnimation(i);
        }
    }


    public void startAnimation(final int i) {
        float f;
        ValueAnimator valueAnimator;
        View findViewByPosition = this.mLinearLayoutManager.findViewByPosition(i);
        if (findViewByPosition != null) {
            f = Math.abs((((float) getMeasuredWidth()) / 2.0f) - (findViewByPosition.getX() + (((float) findViewByPosition.getMeasuredWidth()) / 2.0f))) / ((float) findViewByPosition.getMeasuredWidth());
        } else {
            f = 1.0f;
        }
        if (i < this.mIndicatorPosition) {
            valueAnimator = ValueAnimator.ofFloat(new float[]{f, 0.0f});
        } else {
            valueAnimator = ValueAnimator.ofFloat(new float[]{-f, 0.0f});
        }
        valueAnimator.setDuration(DEFAULT_SCROLL_DURATION);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                RecyclerTabLayout.this.scrollToTab(i, ((Float) valueAnimator.getAnimatedValue()).floatValue(), true);
            }
        });
        valueAnimator.start();
    }


    public void scrollToTab(int i) {
        scrollToTab(i, 0.0f, false);
        this.mAdapter.setCurrentIndicatorPosition(i);
        this.mAdapter.notifyDataSetChanged();
    }


    public void scrollToTab(int i, float f, boolean z) {
        int i2 = 0;
        int i3;
        float f2;
        View findViewByPosition = this.mLinearLayoutManager.findViewByPosition(i);
        View findViewByPosition2 = this.mLinearLayoutManager.findViewByPosition(i + 1);
        if (findViewByPosition != null) {
            int measuredWidth = getMeasuredWidth();
            if (i == 0) {
                f2 = 0.0f;
            } else {
                f2 = (((float) measuredWidth) / 2.0f) - (((float) findViewByPosition.getMeasuredWidth()) / 2.0f);
            }
            float measuredWidth2 = ((float) findViewByPosition.getMeasuredWidth()) + f2;
            if (findViewByPosition2 != null) {
                float measuredWidth3 = (measuredWidth2 - ((((float) measuredWidth) / 2.0f) - (((float) findViewByPosition2.getMeasuredWidth()) / 2.0f))) * f;
                i2 = (int) (f2 - measuredWidth3);
                if (i == 0) {
                    float measuredWidth4 = (float) ((findViewByPosition2.getMeasuredWidth() - findViewByPosition.getMeasuredWidth()) / 2);
                    this.mIndicatorGap = (int) (measuredWidth4 * f);
                    this.mIndicatorScroll = (int) ((((float) findViewByPosition.getMeasuredWidth()) + measuredWidth4) * f);
                } else {
                    this.mIndicatorGap = (int) (((float) ((findViewByPosition2.getMeasuredWidth() - findViewByPosition.getMeasuredWidth()) / 2)) * f);
                    this.mIndicatorScroll = (int) measuredWidth3;
                }
            } else {
                i2 = (int) f2;
                this.mIndicatorScroll = 0;
                this.mIndicatorGap = 0;
            }
            if (z) {
                this.mIndicatorScroll = 0;
                this.mIndicatorGap = 0;
            }
        } else {
            if (getMeasuredWidth() <= 0 || this.mTabMaxWidth <= 0 || this.mTabMinWidth != this.mTabMaxWidth) {
                i3 = 0;
            } else {
                int i4 = this.mTabMinWidth;
                i3 = ((int) (((float) (-i4)) * f)) + ((int) (((float) (getMeasuredWidth() - i4)) / 2.0f));
            }
            this.mRequestScrollToTab = true;
        }
        updateCurrentIndicatorPosition(i, f - this.mOldPositionOffset, f);
        this.mIndicatorPosition = i;
        stopScroll();
        if (!(i == this.mOldPosition && i2 == this.mOldScrollOffset)) {
            this.mLinearLayoutManager.scrollToPositionWithOffset(i, i2);
        }
        if (this.mIndicatorHeight > 0) {
            invalidate();
        }
        this.mOldPosition = i;
        this.mOldScrollOffset = i2;
        this.mOldPositionOffset = f;
    }


    public void updateCurrentIndicatorPosition(int i, float f, float f2) {
        if (this.mAdapter != null) {
            if (f > 0.0f && f2 >= this.mPositionThreshold - POSITION_THRESHOLD_ALLOWABLE) {
                i++;
            } else if (f >= 0.0f || f2 > (1.0f - this.mPositionThreshold) + POSITION_THRESHOLD_ALLOWABLE) {
                i = -1;
            }
            if (i >= 0 && i != this.mAdapter.getCurrentIndicatorPosition()) {
                this.mAdapter.setCurrentIndicatorPosition(i);
                this.mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void onDraw(Canvas canvas) {
        int i;
        int i2;
        View findViewByPosition = this.mLinearLayoutManager.findViewByPosition(this.mIndicatorPosition);
        if (findViewByPosition != null) {
            this.mRequestScrollToTab = false;
            if (isLayoutRtl()) {
                i = (findViewByPosition.getLeft() - this.mIndicatorScroll) - this.mIndicatorGap;
                i2 = (findViewByPosition.getRight() - this.mIndicatorScroll) + this.mIndicatorGap;
            } else {
                i = (findViewByPosition.getLeft() + this.mIndicatorScroll) - this.mIndicatorGap;
                i2 = findViewByPosition.getRight() + this.mIndicatorScroll + this.mIndicatorGap;
            }
            Canvas canvas2 = canvas;
            canvas2.drawRect((float) i, (float) (getHeight() - this.mIndicatorHeight), (float) i2, (float) getHeight(), this.mIndicatorPaint);
        } else if (this.mRequestScrollToTab) {
            this.mRequestScrollToTab = false;
            scrollToTab(this.mViewPager.getCurrentItem());
        }
    }


    public boolean isLayoutRtl() {
        return ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL;
    }

    protected static class RecyclerOnScrollListener extends OnScrollListener {
        public int mDx;
        protected LinearLayoutManager mLinearLayoutManager;
        protected RecyclerTabLayout mRecyclerTabLayout;

        public RecyclerOnScrollListener(RecyclerTabLayout recyclerTabLayout, LinearLayoutManager linearLayoutManager) {
            this.mRecyclerTabLayout = recyclerTabLayout;
            this.mLinearLayoutManager = linearLayoutManager;
        }

        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            this.mDx += i;
        }

        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            if (i == 0) {
                if (this.mDx > 0) {
                    selectCenterTabForRightScroll();
                } else {
                    selectCenterTabForLeftScroll();
                }
                this.mDx = 0;
            }
        }


        public void selectCenterTabForRightScroll() {
            int findLastVisibleItemPosition = this.mLinearLayoutManager.findLastVisibleItemPosition();
            int width = this.mRecyclerTabLayout.getWidth() / 2;
            for (int findFirstVisibleItemPosition = this.mLinearLayoutManager.findFirstVisibleItemPosition(); findFirstVisibleItemPosition <= findLastVisibleItemPosition; findFirstVisibleItemPosition++) {
                View findViewByPosition = this.mLinearLayoutManager.findViewByPosition(findFirstVisibleItemPosition);
                if (findViewByPosition.getLeft() + findViewByPosition.getWidth() >= width) {
                    this.mRecyclerTabLayout.setCurrentItem(findFirstVisibleItemPosition, false);
                    return;
                }
            }
        }


        public void selectCenterTabForLeftScroll() {
            int findFirstVisibleItemPosition = this.mLinearLayoutManager.findFirstVisibleItemPosition();
            int width = this.mRecyclerTabLayout.getWidth() / 2;
            for (int findLastVisibleItemPosition = this.mLinearLayoutManager.findLastVisibleItemPosition(); findLastVisibleItemPosition >= findFirstVisibleItemPosition; findLastVisibleItemPosition--) {
                if (this.mLinearLayoutManager.findViewByPosition(findLastVisibleItemPosition).getLeft() <= width) {
                    this.mRecyclerTabLayout.setCurrentItem(findLastVisibleItemPosition, false);
                    return;
                }
            }
        }
    }

    protected static class ViewPagerOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private final RecyclerTabLayout mRecyclerTabLayout;
        private int mScrollState;

        public ViewPagerOnPageChangeListener(RecyclerTabLayout recyclerTabLayout) {
            this.mRecyclerTabLayout = recyclerTabLayout;
        }

        public void onPageScrolled(int i, float f, int i2) {
            this.mRecyclerTabLayout.scrollToTab(i, f, false);
        }

        public void onPageScrollStateChanged(int i) {
            this.mScrollState = i;
        }

        public void onPageSelected(int i) {
            if (this.mScrollState == 0 && this.mRecyclerTabLayout.mIndicatorPosition != i) {
                this.mRecyclerTabLayout.scrollToTab(i);
            }
        }
    }

    public static abstract class Adapter<T extends ViewHolder> extends RecyclerView.Adapter<T> {
        protected int mIndicatorPosition;
        protected ViewPager mViewPager;

        public Adapter(ViewPager viewPager) {
            this.mViewPager = viewPager;
        }

        public ViewPager getViewPager() {
            return this.mViewPager;
        }

        public void setCurrentIndicatorPosition(int i) {
            this.mIndicatorPosition = i;
        }

        public int getCurrentIndicatorPosition() {
            return this.mIndicatorPosition;
        }
    }

    public static class DefaultAdapter extends Adapter<DefaultAdapter.ViewHolder> {
        protected static final int MAX_TAB_TEXT_LINES = 2;
        private int mTabBackgroundResId;
        private int mTabMaxWidth;
        private int mTabMinWidth;
        private int mTabOnScreenLimit;
        protected int mTabPaddingBottom;
        protected int mTabPaddingEnd;
        protected int mTabPaddingStart;
        protected int mTabPaddingTop;
        protected int mTabSelectedTextColor;
        protected boolean mTabSelectedTextColorSet;
        protected int mTabTextAppearance;

        public DefaultAdapter(ViewPager viewPager) {
            super(viewPager);
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            TabTextView tabTextView = new TabTextView(viewGroup.getContext());
            if (this.mTabSelectedTextColorSet) {
                tabTextView.setTextColor(tabTextView.createColorStateList(tabTextView.getCurrentTextColor(), this.mTabSelectedTextColor));
            }
            ViewCompat.setPaddingRelative(tabTextView, this.mTabPaddingStart, this.mTabPaddingTop, this.mTabPaddingEnd, this.mTabPaddingBottom);
            tabTextView.setTextAppearance(viewGroup.getContext(), this.mTabTextAppearance);
            tabTextView.setGravity(17);
            tabTextView.setMaxLines(2);
            tabTextView.setEllipsize(TextUtils.TruncateAt.END);
            if (this.mTabOnScreenLimit > 0) {
                int measuredWidth = viewGroup.getMeasuredWidth() / this.mTabOnScreenLimit;
                tabTextView.setMaxWidth(measuredWidth);
                tabTextView.setMinWidth(measuredWidth);
            } else {
                if (this.mTabMaxWidth > 0) {
                    tabTextView.setMaxWidth(this.mTabMaxWidth);
                }
                tabTextView.setMinWidth(this.mTabMinWidth);
            }
            tabTextView.setTextAppearance(tabTextView.getContext(), this.mTabTextAppearance);
            if (this.mTabSelectedTextColorSet) {
                tabTextView.setTextColor(tabTextView.createColorStateList(tabTextView.getCurrentTextColor(), this.mTabSelectedTextColor));
            }
            if (this.mTabBackgroundResId != 0) {
                tabTextView.setBackgroundDrawable(AppCompatResources.getDrawable(tabTextView.getContext(), this.mTabBackgroundResId));
            }
            tabTextView.setLayoutParams(createLayoutParamsForTabs());
            return new ViewHolder(tabTextView);
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            viewHolder.title.setText(getViewPager().getAdapter().getPageTitle(i));
            viewHolder.title.setSelected(getCurrentIndicatorPosition() == i);
        }

        public int getItemCount() {
            return getViewPager().getAdapter().getCount();
        }

        public void setTabPadding(int i, int i2, int i3, int i4) {
            this.mTabPaddingStart = i;
            this.mTabPaddingTop = i2;
            this.mTabPaddingEnd = i3;
            this.mTabPaddingBottom = i4;
        }

        public void setTabTextAppearance(int i) {
            this.mTabTextAppearance = i;
        }

        public void setTabSelectedTextColor(boolean z, int i) {
            this.mTabSelectedTextColorSet = z;
            this.mTabSelectedTextColor = i;
        }

        public void setTabMaxWidth(int i) {
            this.mTabMaxWidth = i;
        }

        public void setTabMinWidth(int i) {
            this.mTabMinWidth = i;
        }

        public void setTabBackgroundResId(int i) {
            this.mTabBackgroundResId = i;
        }

        public void setTabOnScreenLimit(int i) {
            this.mTabOnScreenLimit = i;
        }


        public LayoutParams createLayoutParamsForTabs() {
            return new LayoutParams(-2, -1);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView title;

            public ViewHolder(View view) {
                super(view);
                this.title = (TextView) view;
                view.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        int adapterPosition = ViewHolder.this.getAdapterPosition();
                        if (adapterPosition != -1) {
                            DefaultAdapter.this.getViewPager().setCurrentItem(adapterPosition, true);
                        }
                    }
                });
            }
        }
    }

    public static class TabTextView extends AppCompatTextView {
        public TabTextView(Context context) {
            super(context);
        }

        public ColorStateList createColorStateList(int i, int i2) {
            return new ColorStateList(new int[][]{SELECTED_STATE_SET, EMPTY_STATE_SET}, new int[]{i2, i});
        }
    }
}

