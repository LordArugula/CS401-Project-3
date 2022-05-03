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

/**
 * Displays a list of {@link Card cards}.
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    /**
     * Listener interface when a card is clicked.
     */
    public interface OnClickListener {
        /**
         * The onclick event.
         *
         * @param view the card that was clicked.
         * @param card the card.
         */
        void onClick(View view, Card card);
    }

    /**
     * The list of cards to show.
     */
    private List<Card> data;

    /**
     * The onClickListener.
     */
    private final OnClickListener onClickListener;

    /**
     * Creates the adapter with the cards and the onClickListener.
     *
     * @param cards           the list of cards.
     * @param onClickListener the onClickListener.
     */
    public CardAdapter(List<Card> cards, OnClickListener onClickListener) {
        data = cards;
        this.onClickListener = onClickListener;
    }

    /**
     * Creates the card ViewHolder.
     *
     * @param parent   the ViewGroup parent.
     * @param viewType the type.
     * @return the created ViewHolder.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Binds the card to the ViewHolder.
     *
     * @param holder   the ViewHolder.
     * @param position the index of the card.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Card card = data.get(position);

        // Parses the markdown into html and displays it
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

        // Displays the assigned user's profile picture
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
                        requestCreator = picasso.load(user.getProfilePicUri())
                                .resize(64, 64)
                                .config(Bitmap.Config.ARGB_8888)
                                .placeholder(R.drawable.ic_baseline_person_24);
                        requestCreator.into(holder.image_userProfile);
                    });
        }

        // Creates the adapter for the tags
        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.recyclerView_tags.getContext(), LinearLayoutManager.HORIZONTAL, false);
        holder.recyclerView_tags.setLayoutManager(layoutManager);
        TagIconAdapter tagAdapter = new TagIconAdapter(card.getTags());
        holder.setAdapter(tagAdapter);
    }

    /**
     * Returns the number of cards in the adapter.
     *
     * @return the number of cards in the adapter.
     */
    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * The ViewHolder for the card.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * The card title TextView.
         */
        TextView text_title;
        /**
         * The assigned date TextView.
         */
        TextView text_date;
        /**
         * The ImageView for the assigned user's profile picture.
         */
        ImageView image_userProfile;

        /**
         * The RecyclerView for displaying the card's tags.
         */
        RecyclerView recyclerView_tags;
        /**
         * The adapter for the card tags.
         */
        TagIconAdapter tagAdapter;

        /**
         * Creates the card ViewHolder.
         *
         * @param itemView the card view.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text_title = itemView.findViewById(R.id.item_card_title);
            text_date = itemView.findViewById(R.id.item_card_date);
            image_userProfile = itemView.findViewById(R.id.item_card_assigned_user_profile_image);
            recyclerView_tags = itemView.findViewById(R.id.item_card_recylerView_tags);
        }

        /**
         * Sets the adapter for the tag icons.
         *
         * @param adapter the tag icon adapter.
         */
        public void setAdapter(TagIconAdapter adapter) {
            this.tagAdapter = adapter;
            recyclerView_tags.setAdapter(adapter);
        }
    }
}
