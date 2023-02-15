package com.example.pixelperfect.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pixelperfect.Assets.BrushColorAsset;
import com.example.pixelperfect.Listener.BrushColorListener;
import com.example.pixelperfect.R;

import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> {
    public BrushColorListener brushColorListener;
    public List<String> colors = BrushColorAsset.listColorBrush();
    private Context context;
    public int selectedColorIndex;

    public ColorAdapter(Context context2, BrushColorListener brushColorListener2) {
        this.context = context2;
        this.brushColorListener = brushColorListener2;
    }

    public ColorAdapter(Context context2, BrushColorListener brushColorListener2, boolean z) {
        this.context = context2;
        this.brushColorListener = brushColorListener2;
        this.colors.add(0, "#00000000");
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_color, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.circle_view_color.setBackgroundColor(Color.parseColor(this.colors.get(i)));

        if (this.selectedColorIndex == i) {
            viewHolder.imageViewSelected.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imageViewSelected.setVisibility(View.GONE);
        }
    }

    public int getItemCount() {
        return this.colors.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View circle_view_color;
        ImageView imageViewSelected;
        ViewHolder(View view) {
            super(view);
            this.circle_view_color = view.findViewById(R.id.square_view);
            this.imageViewSelected = view.findViewById(R.id.imageSelection);
            this.circle_view_color.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    ColorAdapter.this.selectedColorIndex = ViewHolder.this.getLayoutPosition();
                    ColorAdapter.this.brushColorListener.onColorChanged(ColorAdapter.this.selectedColorIndex, ColorAdapter.this.colors.get(ColorAdapter.this.selectedColorIndex));

                    //ColorAdapter.this.brushColorListener.onColorChanged(ColorAdapter.this.colors.get(ColorAdapter.this.selectedColorIndex));
                    ColorAdapter.this.notifyDataSetChanged();
                }
            });
        }
    }

    public void setSelectedColorIndex(int i) {
        this.selectedColorIndex = i;
    }
}
