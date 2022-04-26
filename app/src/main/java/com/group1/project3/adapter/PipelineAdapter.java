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
import com.group1.project3.model.Pipeline;

import java.util.List;

public class PipelineAdapter extends RecyclerView.Adapter<PipelineAdapter.ViewHolder> {

    private final List<Pipeline> data;

    public interface OnPipelineClickListener {
        void onClick(View view, int position);
    }

    private final OnPipelineClickListener onClickAddCardListener;
    private final OnPipelineClickListener onClickMenuListener;

    public PipelineAdapter(List<Pipeline> pipelines, OnPipelineClickListener onClickAddCardListener, OnPipelineClickListener onClickMenuListener) {
        data = pipelines;
        this.onClickAddCardListener = onClickAddCardListener;
        this.onClickMenuListener = onClickMenuListener;
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
        Pipeline pipeline = data.get(position);

        holder.text_pipelineName.setText(pipeline.getName());

        holder.button_addCard.setOnClickListener(view -> onClickAddCardListener.onClick(view, position));
        holder.button_menu.setOnClickListener(view -> onClickMenuListener.onClick(view, position));
        CardAdapter adapter = new CardAdapter(pipeline.getCards());
        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.recyclerView_cards.getContext(), LinearLayoutManager.VERTICAL, false);

        holder.recyclerView_cards.setLayoutManager(layoutManager);
        holder.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return data.size();
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
