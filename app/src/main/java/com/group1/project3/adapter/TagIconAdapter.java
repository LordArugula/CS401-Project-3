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

/**
 * Displays a list of tag icons.
 */
public class TagIconAdapter extends RecyclerView.Adapter<TagIconAdapter.ViewHolder> {

    /**
     * The list of tags.
     */
    private List<Tag> items;

    /**
     * Creates the adapter from a list of tags.
     *
     * @param tags the list of tags.
     */
    public TagIconAdapter(List<Tag> tags) {
        this.items = tags;
    }

    /**
     * Creates the tag ViewHolder.
     *
     * @param parent   the parent of the ViewHolder.
     * @param viewType the type of view.
     * @return the created ViewHolder.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_tag_icon, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Gets the number of tags in the adapter.
     *
     * @return the number of tags in the adapter.
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Binds the tag to the ViewHolder.
     *
     * @param holder   the ViewHolder.
     * @param position the index of the tag.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tag tag = items.get(position);
        holder.bind(tag);
    }

    /**
     * The tag ViewHolder.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * The icon view.
         */
        View view_icon;

        /**
         * Creates the tag ViewHolder.
         *
         * @param itemView the tag view.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            view_icon = itemView.findViewById(R.id.item_tag_icon);
        }

        /**
         * Binds the tag to the ViewHolder
         *
         * @param tag the tag.
         */
        public void bind(Tag tag) {
            view_icon.setTooltipText(tag.getName());
            Drawable background = view_icon.getBackground();
            background.setTint(tag.getColor());
        }
    }
}
