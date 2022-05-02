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

/**
 * Displays the pipelines in a project.
 */
public class PipelineAdapter extends RecyclerView.Adapter<PipelineAdapter.ViewHolder> {

    /**
     * The pipeline list source.
     */
    private final Project project;
    /**
     * The project repository.
     */
    private ProjectRepository projectRepository;

    /**
     * The OnClickListener interface when a pipeline is clicked on.
     */
    public interface OnPipelineClickListener {
        /**
         * The onClick callback.
         *
         * @param view     the pipeline that was clicked on.
         * @param position the index of the pipeline.
         */
        void onClick(View view, int position);
    }

    /**
     * The listener for clicking on the add card button.
     */
    private final OnPipelineClickListener onClickAddCardListener;
    /**
     * The listener for clicking on the menu button.
     */
    private final OnPipelineClickListener onClickMenuListener;

    /**
     * Creates the adapter with a project and the onClickListeners.
     *
     * @param project                the project.
     * @param onClickAddCardListener the add card click listener.
     * @param onClickMenuListener    the menu click listener.
     */
    public PipelineAdapter(Project project, OnPipelineClickListener onClickAddCardListener, OnPipelineClickListener onClickMenuListener) {
        this.project = project;
        this.onClickAddCardListener = onClickAddCardListener;
        this.onClickMenuListener = onClickMenuListener;
        projectRepository = new FirestoreProjectRepository();
    }

    /**
     * Creates the ViewHolder for the pipeline.
     *
     * @param parent   the parent for the ViewHolder.
     * @param viewType the type of view.
     * @return the created ViewHolder.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_pipeline, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Binds the pipeline to the ViewHolder.
     *
     * @param holder   the ViewHolder.
     * @param position the index of the pipeline.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pipeline pipeline = project.getPipelines().get(position);

        holder.text_pipelineName.setText(pipeline.getName());

        holder.button_addCard.setOnClickListener(view -> onClickAddCardListener.onClick(view, position));
        holder.button_menu.setOnClickListener(view -> onClickMenuListener.onClick(view, position));
        CardAdapter adapter = new CardAdapter(pipeline.getCards(), (view, card) -> onClickCard(view, pipeline, position));
        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.recyclerView_cards.getContext(), LinearLayoutManager.VERTICAL, false);
        holder.recyclerView_cards.setLayoutManager(layoutManager);
        holder.setAdapter(adapter);
    }

    /**
     * The callback when a card is clicked on. Opens the EditCardDialog
     *
     * @param view      the card that was clicked on.
     * @param pipeline  the pipeline.
     * @param cardIndex the index of the card.
     */
    private void onClickCard(View view, Pipeline pipeline, int cardIndex) {
        Card card = pipeline.getCards().get(cardIndex);
        EditCardDialogBuilder dialog = new EditCardDialogBuilder(view.getContext())
                .setTitle("Create card")
                .setView(R.layout.dialog_edit_card)
                .setRemoveButton((dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    pipeline.removeCard(card);
                    notifyDataSetChanged();
                    projectRepository.updateProject(project);
                })
                .setCard(card, pipeline, project)
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton("Save", (dialogInterface, i, _card) -> {
                    notifyDataSetChanged();
                    projectRepository.updateProject(project);
                });
        dialog.show();
    }

    /**
     * Returns the number of pipelines in the adapter.
     *
     * @return the number of pipelines in the adapter.
     */
    @Override
    public int getItemCount() {
        return project.getPipelines().size();
    }

    /**
     * The ViewHolder for the pipeline.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * The pipeline name TextView.
         */
        TextView text_pipelineName;
        /**
         * The add card button.
         */
        ImageButton button_addCard;
        /**
         * The menu button.
         */
        ImageButton button_menu;
        /**
         * The cards RecyclerView.
         */
        RecyclerView recyclerView_cards;
        /**
         * The card adapter.
         */
        CardAdapter cardAdapter;

        /**
         * Creates the ViewHolder.
         *
         * @param itemView the pipeline view.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text_pipelineName = itemView.findViewById(R.id.item_pipeline_name);
            button_addCard = itemView.findViewById(R.id.item_pipeline_button_add);
            button_menu = itemView.findViewById(R.id.item_pipeline_button_menu);
            recyclerView_cards = itemView.findViewById(R.id.item_pipeline_card_recyclerView);
        }

        /**
         * Sets the card adapter.
         *
         * @param adapter the card adapter.
         */
        public void setAdapter(CardAdapter adapter) {
            this.cardAdapter = adapter;
            recyclerView_cards.setAdapter(adapter);
        }
    }
}
