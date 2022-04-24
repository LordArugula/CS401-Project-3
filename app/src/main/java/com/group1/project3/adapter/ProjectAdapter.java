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

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {

    public interface OnProjectCardClickListener {
        void onClickProjectCard(Project project);
    }

    private final List<Project> data;
    private final OnProjectCardClickListener projectCardClickListener;

    public ProjectAdapter(List<Project> data, OnProjectCardClickListener projectCardClickListener) {
        this.data = data;
        this.projectCardClickListener = projectCardClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_project, parent, false);
        return new ViewHolder(view);
    }

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

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageButton imageButton_projectBackground;
        TextView text_projectName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageButton_projectBackground = itemView.findViewById(R.id.item_project_image);
            text_projectName = itemView.findViewById(R.id.item_project_name);
        }
    }

}
