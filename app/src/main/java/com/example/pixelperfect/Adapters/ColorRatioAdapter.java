package com.example.pixelperfect.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pixelperfect.Assets.BrushColorAsset;
import com.example.pixelperfect.R;

import java.util.ArrayList;
import java.util.List;

public class ColorRatioAdapter extends RecyclerView.Adapter<ColorRatioAdapter.ViewHolder> {
    public BackgroundColorListener backgroundInstaListener;
    private Context context;
    public int selectedSquareIndex;
    public List<SquareView> squareViews = new ArrayList();

    public interface BackgroundColorListener {
        void onBackgroundColorSelected(int i, SquareView squareView);
    }

    public ColorRatioAdapter(Context context2, BackgroundColorListener backgroundInstaListener2) {
        this.context = context2;
        this.backgroundInstaListener = backgroundInstaListener2;
        this.squareViews.add(new SquareView(R.drawable.background_blur, "Blur"));
        List<String> lstColorForBrush = BrushColorAsset.listColorBrush();
        for (int i = 0; i < lstColorForBrush.size() - 2; i++) {
            this.squareViews.add(new SquareView(Color.parseColor(lstColorForBrush.get(i)), "", true));
        }
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_color_ratio, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        SquareView squareView = this.squareViews.get(i);
        if (squareView.isColor) {
            viewHolder.squareView.setBackgroundColor(squareView.drawableId);
        } else {
            viewHolder.squareView.setBackgroundResource(squareView.drawableId);
        }
        if (this.selectedSquareIndex == i) {
            viewHolder.imageViewSelected.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imageViewSelected.setVisibility(View.GONE);
        }
    }

    public int getItemCount() {
        return this.squareViews.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public View squareView;
        public ImageView imageViewSelected;
        public RelativeLayout wrapSquareView;

        public ViewHolder(View view) {
            super(view);
            this.squareView = view.findViewById(R.id.square_view);
            this.imageViewSelected = view.findViewById(R.id.imageSelection);
            this.wrapSquareView = view.findViewById(R.id.filterRoot);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            ColorRatioAdapter.this.selectedSquareIndex = getAdapterPosition();
            ColorRatioAdapter.this.backgroundInstaListener.onBackgroundColorSelected(ColorRatioAdapter.this.selectedSquareIndex, ColorRatioAdapter.this.squareViews.get(ColorRatioAdapter.this.selectedSquareIndex));
            ColorRatioAdapter.this.notifyDataSetChanged();
        }
    }

    public class SquareView {
        public int drawableId;
        public boolean isColor;
        public String text;

        SquareView(int i, String str) {
            this.drawableId = i;
            this.text = str;
        }

        SquareView(int i, String str, boolean z) {
            this.drawableId = i;
            this.text = str;
            this.isColor = z;
        }
    }
}
