package com.group1.project3.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.group1.project3.view.CardActivity;
import com.group1.project3.R;
import com.group1.project3.model.Card;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private List<Card> data;

    public CardAdapter(List<Card> cards) {
        data = cards;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Card card = data.get(position);
        holder.text_title.setText(card.getTitle());
        holder.text_date.setText(card.getAssignedDate() == null ? "" : card.getAssignedDate().toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CardActivity.class);
                String cardAsJson = new Gson().toJson(card);
                intent.putExtra("card", cardAsJson);
                view.getContext().startActivity(intent);
            }
        });
//        TagAdapter tagAdapter = new TagAdapter(card.getTags());
//        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.recyclerView_tags.getContext(), LinearLayoutManager.HORIZONTAL, true);
//        holder.recyclerView_tags.setLayoutManager(layoutManager);
//        holder.recyclerView_tags.setAdapter(tagAdapter);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_title;
        TextView text_date;
        ImageView image_userProfile;
        RecyclerView recyclerView_tags;

        TagAdapter tagAdapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text_title = itemView.findViewById(R.id.item_card_title);
            text_date = itemView.findViewById(R.id.item_card_date);
            image_userProfile = itemView.findViewById(R.id.item_card_assigned_user_profile_image);
            recyclerView_tags = itemView.findViewById(R.id.item_card_tag_recyclerView);
        }

        public void setAdapter(TagAdapter adapter) {
            this.tagAdapter = adapter;
            recyclerView_tags.setAdapter(adapter);
        }
    }
}
