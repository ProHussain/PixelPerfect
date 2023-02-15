package com.example.pixelperfect.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.example.pixelperfect.R;

public class StickerTabAdapter extends RecyclerTabLayout.Adapter<StickerTabAdapter.ViewHolder> {
    private Context context;
    private PagerAdapter mAdapater = this.mViewPager.getAdapter();

    public StickerTabAdapter(ViewPager viewPager, Context context2) {
        super(viewPager);
        this.context = context2;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tab_sticker, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        switch (i) {
            case 0:
                viewHolder.imageView.setImageDrawable(this.context.getDrawable(R.drawable.amoji));
                break;
            case 1:
                viewHolder.imageView.setImageDrawable(this.context.getDrawable(R.drawable.chicken));
                break;
            case 2:
                viewHolder.imageView.setImageDrawable(this.context.getDrawable(R.drawable.child));
                break;
            case 3:
                viewHolder.imageView.setImageDrawable(this.context.getDrawable(R.drawable.christmas));
                break;
            case 4:
                viewHolder.imageView.setImageDrawable(this.context.getDrawable(R.drawable.cute));
                break;
            case 5:
                viewHolder.imageView.setImageDrawable(this.context.getDrawable(R.drawable.emoj));
                break;
            case 6:
                viewHolder.imageView.setImageDrawable(this.context.getDrawable(R.drawable.emoji));
                break;
            case 7:
                viewHolder.imageView.setImageDrawable(this.context.getDrawable(R.drawable.fruit));
                break;
            case 8:
                viewHolder.imageView.setImageDrawable(this.context.getDrawable(R.drawable.heart));
                break;
            case 9:
                viewHolder.imageView.setImageDrawable(this.context.getDrawable(R.drawable.loveday));
                break;
            case 10:
                viewHolder.imageView.setImageDrawable(this.context.getDrawable(R.drawable.plant));
                break;
            case 11:
                viewHolder.imageView.setImageDrawable(this.context.getDrawable(R.drawable.sticker));
                break;
            case 12:
                viewHolder.imageView.setImageDrawable(this.context.getDrawable(R.drawable.sweet));
                break;
            case 13:
                viewHolder.imageView.setImageDrawable(this.context.getDrawable(R.drawable.textcolor));
                break;
            case 14:
                viewHolder.imageView.setImageDrawable(this.context.getDrawable(R.drawable.textneon));
                break;

        }
        viewHolder.imageView.setSelected(i == getCurrentIndicatorPosition());
    }

    public int getItemCount() {
        return this.mAdapater.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            this.imageView = view.findViewById(R.id.image);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    StickerTabAdapter.this.getViewPager().setCurrentItem(ViewHolder.this.getAdapterPosition());
                }
            });
        }
    }
}
