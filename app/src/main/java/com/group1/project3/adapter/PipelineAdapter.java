package com.group1.project3.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group1.project3.R;
import com.group1.project3.model.Card;
import com.group1.project3.model.Pipeline;
import com.group1.project3.model.Project;
import com.group1.project3.repository.FirestoreProjectRepository;
import com.group1.project3.repository.ProjectRepository;
import com.group1.project3.view.dialog.EditCardDialogBuilder;

import java.util.UUID;

public class PipelineAdapter extends RecyclerView.Adapter<PipelineAdapter.ViewHolder> {

    private final Project project;
    private ProjectRepository projectRepository;

    public interface OnPipelineClickListener {
        void onClick(View view, int position);
    }

    private final OnPipelineClickListener onClickAddCardListener;
    private final OnPipelineClickListener onClickMenuListener;

    public PipelineAdapter(Project project, OnPipelineClickListener onClickAddCardListener, OnPipelineClickListener onClickMenuListener) {
        this.project = project;
        this.onClickAddCardListener = onClickAddCardListener;
        this.onClickMenuListener = onClickMenuListener;
        projectRepository = new FirestoreProjectRepository();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_pipeline, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pipeline pipeline = project.getPipelines().get(position);

        holder.text_pipelineName.setText(pipeline.getName());

        holder.button_addCard.setOnClickListener(view -> onClickAddCardListener.onClick(view, position));
        holder.button_menu.setOnClickListener(view -> onClickMenuListener.onClick(view, position));
        CardAdapter adapter = new CardAdapter(pipeline.getCards(), (view, card) -> onClickCard(view, pipeline, card));
        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.recyclerView_cards.getContext(), LinearLayoutManager.VERTICAL, false);

        holder.recyclerView_cards.setLayoutManager(layoutManager);
        holder.setAdapter(adapter);
    }

    private void onClickCard(View view, Pipeline pipeline, Card card) {
        EditCardDialogBuilder dialog = new EditCardDialogBuilder(view.getContext())
                .setTitle("Create card")
                .setView(R.layout.dialog_edit_card)
                .setProject(project)
                .setCard(card)
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton("Save", (dialogInterface, i, _card) -> {
                    notifyDataSetChanged();
                    projectRepository.updateProject(project);
                });
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return project.getPipelines().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView text_pipelineName;
        ImageButton button_addCard;
        ImageButton button_menu;
        RecyclerView recyclerView_cards;
        CardAdapter cardAdapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text_pipelineName = itemView.findViewById(R.id.item_pipeline_name);
            button_addCard = itemView.findViewById(R.id.item_pipeline_button_add);
            button_menu = itemView.findViewById(R.id.item_pipeline_button_menu);
            recyclerView_cards = itemView.findViewById(R.id.item_pipeline_card_recyclerView);
        }

        public CardAdapter getAdapter() {
            return cardAdapter;
        }

        public void setAdapter(CardAdapter adapter) {
            this.cardAdapter = adapter;
            recyclerView_cards.setAdapter(adapter);
        }
    }
}
