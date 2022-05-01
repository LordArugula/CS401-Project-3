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

    private final Project project;

    public TagItemAdapter(Project project) {
        this.project = project;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_tag_entry, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Tag tag = project.getTags().get(position);
        holder.text_tagName.setText(tag.getName());
        Drawable background = holder.view_tagColor.getBackground();
        background.setTint(tag.getColor());
        holder.button_edit.setOnClickListener(view -> openEditTagDialog(holder, tag));
    }

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

    @Override
    public int getItemCount() {
        return project.getTags().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView text_tagName;
        public final View view_tagColor;
        public final Button button_edit;

        public ViewHolder(View itemView) {
            super(itemView);
            text_tagName = itemView.findViewById(R.id.item_tag_entry_text_name);
            view_tagColor = itemView.findViewById(R.id.item_tag_entry_view_color);
            button_edit = itemView.findViewById(R.id.item_tag_entry_button_edit);
        }
    }
}