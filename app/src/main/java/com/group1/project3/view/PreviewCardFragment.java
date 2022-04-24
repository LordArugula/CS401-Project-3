package com.group1.project3.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.group1.project3.R;
import com.group1.project3.model.Card;

public class PreviewCardFragment extends Fragment {

    private Card card;

    public PreviewCardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

//            String cardAsJson = getArguments().getString("card");
//            card = new Gson().fromJson(cardAsJson, Card.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_preview_card, container, false);

        TextView titleText = view.findViewById(R.id.card_preview_title);
        titleText.setText(card.getTitle());

        TextView text_date = view.findViewById(R.id.card_preview_date);
        text_date.setText(card.getAssignedDate() == null ? "" : card.getAssignedDate().toString());

        return view;
    }
}