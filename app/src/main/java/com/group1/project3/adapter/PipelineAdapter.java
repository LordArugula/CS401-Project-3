package com.group1.project3.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group1.project3.R;
import com.group1.project3.model.Pipeline;

import java.util.List;

public class PipelineAdapter extends RecyclerView.Adapter<PipelineAdapter.ViewHolder> {

    private final List<Pipeline> data;
    private final RecyclerView.RecycledViewPool viewPool;

    public PipelineAdapter(List<Pipeline> pipelines) {
        data = pipelines;
        viewPool = new RecyclerView.RecycledViewPool();
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
        RecyclerView recyclerView_cards;
        CardAdapter cardAdapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text_pipelineName = itemView.findViewById(R.id.item_pipeline_name);
            recyclerView_cards = itemView.findViewById(R.id.item_pipeline_card_recyclerView);
        }

        public void setAdapter(CardAdapter adapter) {
            this.cardAdapter = adapter;
            recyclerView_cards.setAdapter(adapter);
        }

        public CardAdapter getAdapter() {
            return cardAdapter;
        }
    }
}
