package com.example.pixelperfect.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pixelperfect.Assets.StickerFileAsset;
import com.example.pixelperfect.R;
import com.example.pixelperfect.Sticker.SplashSticker;
import com.example.pixelperfect.Utils.Constants;
import com.example.pixelperfect.Utils.SystemUtil;

import java.util.ArrayList;
import java.util.List;

public class SquareAdapter extends RecyclerView.Adapter<SquareAdapter.ViewHolder> {

    private int borderWidth;
    private Context context;

    public int selectedSquareIndex;

    public SplashChangeListener splashChangeListener;

    public List<SplashItem> splashList = new ArrayList();

    public interface SplashChangeListener {
        void onSelected(SplashSticker splashSticker);
    }

    public SquareAdapter(Context context2, SplashChangeListener splashChangeListener2, boolean z) {
        this.context = context2;
        this.splashChangeListener = splashChangeListener2;
        this.borderWidth = SystemUtil.dpToPx(context2, Constants.BORDER_WIDTH);
        this.splashList.add(new SplashItem(new SplashSticker(StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_mask_1.webp"), StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_frame_1.webp")), R.drawable.image_mask_1));
        this.splashList.add(new SplashItem(new SplashSticker(StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_mask_2.webp"), StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_frame_2.webp")), R.drawable.image_mask_2));
        this.splashList.add(new SplashItem(new SplashSticker(StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_mask_3.webp"), StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_frame_3.webp")), R.drawable.image_mask_3));
        this.splashList.add(new SplashItem(new SplashSticker(StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_mask_4.webp"), StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_frame_4.webp")), R.drawable.image_mask_4));
        this.splashList.add(new SplashItem(new SplashSticker(StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_mask_5.webp"), StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_frame_5.webp")), R.drawable.image_mask_5));
        this.splashList.add(new SplashItem(new SplashSticker(StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_mask_6.webp"), StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_frame_6.webp")), R.drawable.image_mask_6));
        this.splashList.add(new SplashItem(new SplashSticker(StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_mask_7.webp"), StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_frame_7.webp")), R.drawable.image_mask_7));
        this.splashList.add(new SplashItem(new SplashSticker(StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_mask_8.webp"), StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_frame_8.webp")), R.drawable.image_mask_8));
        this.splashList.add(new SplashItem(new SplashSticker(StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_mask_9.webp"), StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_frame_9.webp")), R.drawable.image_mask_9));
        this.splashList.add(new SplashItem(new SplashSticker(StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_mask_10.webp"), StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_frame_10.webp")), R.drawable.image_mask_10));
        this.splashList.add(new SplashItem(new SplashSticker(StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_mask_11.webp"), StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_frame_11.webp")), R.drawable.image_mask_11));
        this.splashList.add(new SplashItem(new SplashSticker(StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_mask_12.webp"), StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_frame_12.webp")), R.drawable.image_mask_12));
        this.splashList.add(new SplashItem(new SplashSticker(StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_mask_13.webp"), StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_frame_13.webp")), R.drawable.image_mask_13));
        this.splashList.add(new SplashItem(new SplashSticker(StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_mask_14.webp"), StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_frame_14.webp")), R.drawable.image_mask_14));
        this.splashList.add(new SplashItem(new SplashSticker(StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_mask_15.webp"), StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_frame_15.webp")), R.drawable.image_mask_15));
        this.splashList.add(new SplashItem(new SplashSticker(StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_mask_16.webp"), StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_frame_16.webp")), R.drawable.image_mask_16));
        this.splashList.add(new SplashItem(new SplashSticker(StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_mask_17.webp"), StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_frame_17.webp")), R.drawable.image_mask_17));
        this.splashList.add(new SplashItem(new SplashSticker(StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_mask_18.webp"), StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_frame_18.webp")), R.drawable.image_mask_18));
        this.splashList.add(new SplashItem(new SplashSticker(StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_mask_19.webp"), StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_frame_19.webp")), R.drawable.image_mask_19));
        this.splashList.add(new SplashItem(new SplashSticker(StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_mask_20.webp"), StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_frame_20.webp")), R.drawable.image_mask_20));
        this.splashList.add(new SplashItem(new SplashSticker(StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_mask_21.webp"), StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_frame_21.webp")), R.drawable.image_mask_21));
        this.splashList.add(new SplashItem(new SplashSticker(StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_mask_22.webp"), StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_frame_22.webp")), R.drawable.image_mask_22));
        this.splashList.add(new SplashItem(new SplashSticker(StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_mask_23.webp"), StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_frame_23.webp")), R.drawable.image_mask_23));
        this.splashList.add(new SplashItem(new SplashSticker(StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_mask_24.webp"), StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_frame_24.webp")), R.drawable.image_mask_24));
        this.splashList.add(new SplashItem(new SplashSticker(StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_mask_25.webp"), StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_frame_25.webp")), R.drawable.image_mask_25));
        this.splashList.add(new SplashItem(new SplashSticker(StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_mask_26.webp"), StickerFileAsset.loadBitmapFromAssets(context2, "blur/image_frame_26.webp")), R.drawable.image_mask_26));
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_splash, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.splash.setImageResource(this.splashList.get(i).drawableId);
        if (this.selectedSquareIndex == i) {
            viewHolder.relativeLayoutImage.setBackgroundResource(R.drawable.background_item_selected);
            return;
        }
        viewHolder.relativeLayoutImage.setBackgroundResource(R.drawable.background_item);
    }

    public int getItemCount() {
        return this.splashList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView splash;
        public RelativeLayout relativeLayoutImage;
        public ViewHolder(View view) {
            super(view);
            this.splash = view.findViewById(R.id.round_image_view_splash_item);
            this.relativeLayoutImage = view.findViewById(R.id.relativeLayoutImage);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            SquareAdapter.this.selectedSquareIndex = getAdapterPosition();
            if (SquareAdapter.this.selectedSquareIndex < 0) {
                SquareAdapter.this.selectedSquareIndex = 0;
            }
            if (SquareAdapter.this.selectedSquareIndex >= SquareAdapter.this.splashList.size()) {
                SquareAdapter.this.selectedSquareIndex = SquareAdapter.this.splashList.size() - 1;
            }
            SquareAdapter.this.splashChangeListener.onSelected((SquareAdapter.this.splashList.get(SquareAdapter.this.selectedSquareIndex)).splashSticker);
            SquareAdapter.this.notifyDataSetChanged();
        }
    }

    static class SplashItem {
        int drawableId;
        SplashSticker splashSticker;

        SplashItem(SplashSticker splashSticker2, int i) {
            this.splashSticker = splashSticker2;
            this.drawableId = i;
        }
    }


}
