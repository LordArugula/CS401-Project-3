package com.group1.project3.adapter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group1.project3.R;
import com.group1.project3.model.Card;
import com.group1.project3.repository.FirestoreUserRepository;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    public interface OnClickListener {
        void onClick(View view, Card card);
    }

    private List<Card> data;
    private final OnClickListener onClickListener;

    public CardAdapter(List<Card> cards, OnClickListener onClickListener) {
        data = cards;
        this.onClickListener = onClickListener;
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

        if (card.getContent() != null) {
            Parser parser = Parser.builder().build();
            Node document = parser.parse(card.title());
            HtmlRenderer renderer = HtmlRenderer.builder().build();
            holder.text_title.setText(Html.fromHtml(renderer.render(document), Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.text_title.setText("");
        }

        holder.text_date.setText(card.getAssignedDate() == null ? "" : DateFormat.format("MM/dd/yyyy", card.getAssignedDate()));

        holder.itemView.setOnClickListener(view -> onClickListener.onClick(view, card));

        if (card.getAssignedUser() != null) {
            FirestoreUserRepository userRepository = new FirestoreUserRepository();
            userRepository.getUser(card.getAssignedUser())
                    .addOnSuccessListener(user -> {
                        holder.image_userProfile.setTooltipText(user.getUsername());
                        Picasso picasso = Picasso.get();
                        RequestCreator requestCreator;
                        if (user.getProfilePicUri() != null && !user.getProfilePicUri().isEmpty()) {
                            Uri uri = Uri.parse(user.getProfilePicUri());
                            requestCreator = picasso.load(uri);
                        }
                        requestCreator = picasso.load(R.drawable.ic_baseline_person_24)
                                .resize(64, 64)
                                .config(Bitmap.Config.ARGB_8888)
                                .placeholder(R.drawable.ic_baseline_person_24);
                        requestCreator.into(holder.image_userProfile);
                    });
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.recyclerView_tags.getContext(), LinearLayoutManager.HORIZONTAL, false);
        holder.recyclerView_tags.setLayoutManager(layoutManager);
        TagIconAdapter tagAdapter = new TagIconAdapter(card.getTags());
        holder.setAdapter(tagAdapter);
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
        TagIconAdapter tagAdapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text_title = itemView.findViewById(R.id.item_card_title);
            text_date = itemView.findViewById(R.id.item_card_date);
            image_userProfile = itemView.findViewById(R.id.item_card_assigned_user_profile_image);
            recyclerView_tags = itemView.findViewById(R.id.item_card_recylerView_tags);
        }

        public void setAdapter(TagIconAdapter adapter) {
            this.tagAdapter = adapter;
            recyclerView_tags.setAdapter(adapter);
        }
    }
}
