package com.group1.project3.adapter;

import junit.framework.TestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.instanceOf;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.group1.project3.R;
import com.group1.project3.model.Card;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * tests the {@link CardAdapter} class
 */
public class CardAdapterTest {

    /**
     * tests the onCreateViewHolder() method
     */
    @Test
    public void testOnCreateViewHolder() {
        CardAdapter cardAdapter = mock(CardAdapter.class);
        CardAdapter.ViewHolder mockViewHolder = mock(CardAdapter.ViewHolder.class);
        ViewGroup parent = mock(ViewGroup.class);
        when(cardAdapter.onCreateViewHolder(parent, 1)).thenReturn(mockViewHolder);

        Assert.assertSame(cardAdapter.onCreateViewHolder(parent, 1), mockViewHolder);
    }

    @Test
    public void testOnBindViewHolder() {

    }

    /**
     * tests the getItemCount() method
     */
    @Test
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
            Assert.assertEquals(mockedView, view);
            Assert.assertEquals(mockedCard, card);

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
        Assert.assertEquals(cardAdapter.getItemCount(), 2);
    }
}