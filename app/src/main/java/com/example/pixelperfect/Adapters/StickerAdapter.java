package com.example.pixelperfect.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pixelperfect.Assets.StickerFileAsset;
import com.example.pixelperfect.R;

import java.util.List;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder> {

    public Context context;

    public int screenWidth;

    public OnClickStickerListener stickerListener;

    public List<String> stickers;

    public interface OnClickStickerListener {
        void addSticker(int i, Bitmap bitmap);
    }

    public StickerAdapter(Context context2, List<String> list, int i, OnClickStickerListener onClickStickerListener) {
        this.context = context2;
        this.stickers = list;
        this.screenWidth = i;
        this.stickerListener = onClickStickerListener;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_sticker, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Bitmap bitmap = StickerFileAsset.loadBitmapFromAssets(this.context, this.stickers.get(i));
        Glide.with(context).load(bitmap).into(viewHolder.sticker);
    }

    public int getItemCount() {
        return this.stickers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView sticker;

        public ViewHolder(View view) {
            super(view);
            this.sticker = view.findViewById(R.id.image_view_item_sticker);

            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            StickerAdapter.this.stickerListener.addSticker(getAdapterPosition(),StickerFileAsset.loadBitmapFromAssets(StickerAdapter.this.context, (String) StickerAdapter.this.stickers.get(getAdapterPosition())));
        }
    }
}
