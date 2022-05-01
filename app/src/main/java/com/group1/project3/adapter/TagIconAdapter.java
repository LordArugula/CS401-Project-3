package com.group1.project3.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group1.project3.R;
import com.group1.project3.model.Tag;

import java.util.List;

public class TagIconAdapter extends RecyclerView.Adapter<TagIconAdapter.ViewHolder> {

    private List<Tag> items;

    public TagIconAdapter(List<Tag> tags) {
        this.items = tags;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_tag_icon, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tag tag = items.get(position);
        holder.bind(tag);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View view_icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            view_icon = itemView.findViewById(R.id.item_tag_icon);
        }

        public void bind(Tag tag) {
            view_icon.setTooltipText(tag.getName());
            Drawable background = view_icon.getBackground();
            background.setTint(tag.getColor());
        }
    }
}
