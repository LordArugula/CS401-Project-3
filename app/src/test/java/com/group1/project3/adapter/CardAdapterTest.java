package com.group1.project3.adapter;

import junit.framework.TestCase;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.instanceOf;

import android.content.Context;
import android.view.View;

import com.group1.project3.model.Card;

import java.util.ArrayList;
import java.util.List;


public class CardAdapterTest extends TestCase {

    public void testOnCreateViewHolder() {
    }

    public void testOnBindViewHolder() {
    }

    public void testGetItemCount() {
        //create mock view and card for use when creating mock onClickListener
        View mockedView = mock(View.class);
        Card mockedCard = mock(Card.class);

        //create mock onClickListener
        CardAdapter.OnClickListener mockedOnClickListener = mock(CardAdapter.OnClickListener.class);
        doAnswer(invocation -> {
            //create a view and card based on the passed arguments
            View view = invocation.getArgument(0);
            Card card = invocation.getArgument(1);

            //assert that the passed argument matches
            assertEquals(mockedView, view);
            assertEquals(mockedCard, card);

            return null;
        }).when(mockedOnClickListener).onClick(any(View.class), any(Card.class));

        //create list of two cards
        List<Card> cards = new ArrayList<>();
        cards.add(new Card());
        cards.add(new Card());

        //create card adapter using cards and the mocked onClickListener
        CardAdapter cardAdapter = new CardAdapter(cards, mockedOnClickListener);
        //verify that calling the constructor runs onClick once
        verify(mockedOnClickListener, times(1));

        //check that the created card adapter has the correct number of cards
        assertEquals(cardAdapter.getItemCount(), 2);
    }
}