package com.group1.project3.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group1.project3.R;
import com.group1.project3.model.Project;

import java.util.List;

/**
 * Displays a list of projects.
 */
public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {

    /**
     * The onClickListener for projects.
     */
    public interface OnProjectCardClickListener {
        /**
         * The onClick event when a project is clicked.
         *
         * @param project the project that was clicked on.
         */
        void onClickProjectCard(Project project);
    }

    /**
     * The projects list.
     */
    private final List<Project> data;
    /**
     * The onClickListener for the projects.
     */
    private final OnProjectCardClickListener projectCardClickListener;

    /**
     * Creates the adapter from list of projects and an onClickListener.
     *
     * @param data                     the list of projects.
     * @param projectCardClickListener the onClickListener.
     */
    public ProjectAdapter(List<Project> data, OnProjectCardClickListener projectCardClickListener) {
        this.data = data;
        this.projectCardClickListener = projectCardClickListener;
    }

    /**
     * Creates the ViewHolder for the project.
     *
     * @param parent   the parent for the ViewHolder.
     * @param viewType The type of view.
     * @return the created ViewHolder.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_project, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Binds the project to the ViewHolder.
     *
     * @param holder   the ViewHolder.
     * @param position the index of the project.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Project project = data.get(position);
        holder.text_projectName.setText(project.getName());

        ImageButton projectBackgroundButton = holder.imageButton_projectBackground;
        projectBackgroundButton.setBackgroundColor(project.getColor());
        projectBackgroundButton.setOnClickListener(view -> {
            projectCardClickListener.onClickProjectCard(project);
        });
    }

    /**
     * Gets the number of projects in the adapter.
     *
     * @return the number of projects in the adapter.
     */
    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * The Project ViewHolder.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * The background image for the project.
         */
        ImageButton imageButton_projectBackground;
        /**
         * The project name TextView.
         */
        TextView text_projectName;

        /**
         * Creates the ViewHolder.
         *
         * @param itemView the project view.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageButton_projectBackground = itemView.findViewById(R.id.item_project_image);
            text_projectName = itemView.findViewById(R.id.item_project_name);
        }
    }

}
