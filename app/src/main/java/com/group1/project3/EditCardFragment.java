package com.group1.project3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.group1.project3.model.Card;

public class EditCardFragment extends Fragment {

    protected RecyclerView recyclerView;

    private EditText text_content;
    private TextView text_date;
    private Card card;

    public EditCardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            String cardAsJson = getArguments().getString("card");
            card = new Gson().fromJson(cardAsJson, Card.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_card, container, false);

        TextView text_cardContent = view.findViewById(R.id.card_edit_content);
        text_cardContent.setText(card.getContent());

        TextView text_date = view.findViewById(R.id.card_edit_date);
        text_date.setText(card.getAssignedDate() == null ? "" : card.getAssignedDate().toString());

        return view;
    }
}