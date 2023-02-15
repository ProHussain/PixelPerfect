package com.example.pixelperfect.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pixelperfect.Assets.OverlayFileAsset;
import com.example.pixelperfect.Listener.FilterListener;
import com.example.pixelperfect.R;
import com.example.pixelperfect.Utils.Constants;
import com.example.pixelperfect.Utils.SystemUtil;
import com.github.siyamed.shapeimageview.RoundedImageView;

import java.util.List;

public class OverlayAdapter extends RecyclerView.Adapter<OverlayAdapter.ViewHolder> {
    private List<Bitmap> bitmaps;
    private int borderWidth;
    private Context context;
    public List<OverlayFileAsset.OverlayCode> filterBeanList;
    public FilterListener filterListener;
    public int selectedFilterIndex = 0;

    public OverlayAdapter(List<Bitmap> bitmapList, FilterListener filterListener, Context mContext, List<OverlayFileAsset.OverlayCode> mfilterBeanList) {
        this.filterListener = filterListener;
        this.bitmaps = bitmapList;
        this.context = mContext;
        this.filterBeanList = mfilterBeanList;
        this.borderWidth = SystemUtil.dpToPx(mContext, Constants.BORDER_WIDTH);
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_filter, viewGroup, false));
    }

    public void reset() {
        this.selectedFilterIndex = 0;
        notifyDataSetChanged();
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.round_image_view_filter_item.setImageBitmap(this.bitmaps.get(i));
        if (this.selectedFilterIndex == i) {
            viewHolder.round_image_view_filter_item.setBorderColor(ContextCompat.getColor(context, R.color.mainColor));
            viewHolder.round_image_view_filter_item.setBorderWidth(this.borderWidth);
            viewHolder.round_image_view_filter_item.setMaxWidth(80);
            viewHolder.round_image_view_filter_item.setMaxHeight(80);
            return;
        }
        viewHolder.round_image_view_filter_item.setBorderColor(0);
        viewHolder.round_image_view_filter_item.setMaxWidth(70);
        viewHolder.round_image_view_filter_item.setMaxHeight(70);
        viewHolder.round_image_view_filter_item.setBorderWidth(this.borderWidth);

    }

    public int getItemCount() {
        return this.bitmaps.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView round_image_view_filter_item;
        RelativeLayout relative_layout_wrapper_filter_item;

        ViewHolder(View view) {
            super(view);
            this.round_image_view_filter_item = view.findViewById(R.id.round_image_view_filter_item);
            this.relative_layout_wrapper_filter_item = view.findViewById(R.id.relative_layout_wrapper_filter_item);

            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    OverlayAdapter.this.selectedFilterIndex = ViewHolder.this.getLayoutPosition();
                    OverlayAdapter.this.filterListener.onFilterSelected(selectedFilterIndex,((OverlayFileAsset.OverlayCode) OverlayAdapter.this.filterBeanList.get(OverlayAdapter.this.selectedFilterIndex)).getImage());
                    OverlayAdapter.this.notifyDataSetChanged();
                }
            });
        }
    }
}
