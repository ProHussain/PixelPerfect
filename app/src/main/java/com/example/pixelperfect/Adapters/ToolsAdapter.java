package com.example.pixelperfect.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pixelperfect.R;
import com.example.pixelperfect.Utils.ToolEditor;

import java.util.ArrayList;
import java.util.List;

public class ToolsAdapter extends RecyclerView.Adapter<ToolsAdapter.ViewHolder> {

    public OnItemSelected onItemSelected;
    public List<ToolModel> toolLists = new ArrayList<>();
    public int selectedSquareIndex = 0;

    public interface OnItemSelected {
        void onToolSelected(ToolEditor toolType);
    }

    public ToolsAdapter(OnItemSelected onItemSelected) {
        this.onItemSelected = onItemSelected;
        this.toolLists.add(new ToolModel(R.string.filter, R.drawable.ic_filter, ToolEditor.FILTER));
        this.toolLists.add(new ToolModel(R.string.adjust, R.drawable.ic_set, ToolEditor.ADJUST));
        this.toolLists.add(new ToolModel(R.string.overlay, R.drawable.ic_overlay, ToolEditor.EFFECT));
        this.toolLists.add(new ToolModel(R.string.hsl, R.drawable.ic_hsl, ToolEditor.HSL));
        this.toolLists.add(new ToolModel(R.string.crop, R.drawable.ic_crop, ToolEditor.CROP));
        this.toolLists.add(new ToolModel(R.string.sticker, R.drawable.ic_sticker, ToolEditor.STICKER));
        this.toolLists.add(new ToolModel(R.string.text, R.drawable.ic_text, ToolEditor.TEXT));
        this.toolLists.add(new ToolModel(R.string.ratio, R.drawable.ic_ratio, ToolEditor.RATIO));
        this.toolLists.add(new ToolModel(R.string.square, R.drawable.ic_blur_bg, ToolEditor.SQUARE));
    }

    class ToolModel {
        public int toolIcon;
        public int toolName;
        public ToolEditor toolType;

        ToolModel(int str, int i, ToolEditor toolType) {
            this.toolName = str;
            this.toolIcon = i;
            this.toolType = toolType;
        }
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_editing, viewGroup, false));
    }

    public void reset() {
        this.selectedSquareIndex = 0;
        notifyDataSetChanged();
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ToolModel toolModel = this.toolLists.get(i);
        viewHolder.text_view_tool_name.setText(toolModel.toolName);
        viewHolder.image_view_tool_icon.setImageResource(toolModel.toolIcon);

        /*if (this.selectedSquareIndex == i) {
            viewHolder.text_view_tool_name.setTextColor(Color.parseColor("#0004FF"));
            viewHolder.image_view_tool_icon.setColorFilter(Color.parseColor("#0004FF"));
            return;
        } else {
            viewHolder.text_view_tool_name.setTextColor(Color.parseColor("#000000"));
            viewHolder.image_view_tool_icon.setColorFilter(Color.parseColor("#000000"));
        }*/
    }

    public int getItemCount() {
        return this.toolLists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image_view_tool_icon;
        TextView text_view_tool_name;
        RelativeLayout relative_layout_wrapper_tool;

        ViewHolder(View view) {
            super(view);
            this.image_view_tool_icon = view.findViewById(R.id.image_view_tool_icon);
            this.text_view_tool_name = view.findViewById(R.id.text_view_tool_name);
            this.relative_layout_wrapper_tool = view.findViewById(R.id.relative_layout_wrapper_tool);
            relative_layout_wrapper_tool.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    ToolsAdapter.this.selectedSquareIndex = ViewHolder.this.getLayoutPosition();
                    ToolsAdapter.this.onItemSelected.onToolSelected((ToolsAdapter.this.toolLists.get(ToolsAdapter.this.selectedSquareIndex)).toolType);
                    notifyDataSetChanged();
                }
            });

        }
    }
}
