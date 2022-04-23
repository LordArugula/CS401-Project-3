package com.group1.project3.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.group1.project3.R;
import com.group1.project3.model.Tag;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Tag}.
 */
public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {

    private final List<Tag> items;

    public TagAdapter(List<Tag> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_tag, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView contentView;

        public ViewHolder(View itemView) {
            super(itemView);
            contentView = itemView.findViewById(R.id.item_tag_content);
        }

        public void bind(Tag tag) {
            contentView.setTooltipText(tag.getName());
            contentView.setBackgroundColor(tag.getColor().toArgb());
        }
    }
}