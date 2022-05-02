package com.group1.project3.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group1.project3.R;
import com.group1.project3.model.Project;
import com.group1.project3.model.Tag;
import com.group1.project3.view.dialog.EditTagDialogBuilder;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Tag}.
 */
public class TagItemAdapter extends RecyclerView.Adapter<TagItemAdapter.ViewHolder> {

    /**
     * The project that contains the tags list.
     */
    private final Project project;

    /**
     * Creates the Tag Item adapter.
     *
     * @param project the project.
     */
    public TagItemAdapter(Project project) {
        this.project = project;
    }

    /**
     * Creates the tag item ViewHolder
     *
     * @param parent   the parent of the ViewHolder.
     * @param viewType the type of view.
     * @return the created tag item ViewHolder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_tag_entry, parent, false));
    }

    /**
     * Binds the tag to the ViewHolder.
     *
     * @param holder   the ViewHolder.
     * @param position the index of the tag.
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Tag tag = project.getTags().get(position);
        holder.text_tagName.setText(tag.getName());
        Drawable background = holder.view_tagColor.getBackground();
        background.setTint(tag.getColor());
        holder.button_edit.setOnClickListener(view -> openEditTagDialog(holder, tag));
    }

    /**
     * Opens the EditTagDialog.
     *
     * @param holder the ViewHolder that was clicked on.
     * @param tag    the tag.
     */
    private void openEditTagDialog(ViewHolder holder, Tag tag) {
        EditTagDialogBuilder dialogBuilder = new EditTagDialogBuilder(holder.itemView.getContext())
                .setTitle("Edit Tag")
                .setTag(tag)
                .setDeleteButton(true, (dialogInterface, i, tag12) -> {
                    project.removeTag(tag12);
                    notifyDataSetChanged();
                    dialogInterface.dismiss();
                })
                .setView(R.layout.dialog_edit_tag)
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton("Save", (dialogInterface, i, tag1) -> {
                    holder.text_tagName.setText(tag.getName());
                    Drawable background = holder.view_tagColor.getBackground();
                    background.setTint(tag.getColor());
                });

        dialogBuilder
                .show();
    }

    /**
     * Gets the number of tags.
     *
     * @return the number of tags.
     */
    @Override
    public int getItemCount() {
        return project.getTags().size();
    }

    /**
     * The tag ViewHolder.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * The tag name TextView.
         */
        public final TextView text_tagName;
        /**
         * The icon view.
         */
        public final View view_tagColor;
        /**
         * The edit button.
         */
        public final Button button_edit;

        /**
         * Creates the ViewHolder.
         *
         * @param itemView the tag view.
         */
        public ViewHolder(View itemView) {
            super(itemView);
            text_tagName = itemView.findViewById(R.id.item_tag_entry_text_name);
            view_tagColor = itemView.findViewById(R.id.item_tag_entry_view_color);
            button_edit = itemView.findViewById(R.id.item_tag_entry_button_edit);
        }
    }
}