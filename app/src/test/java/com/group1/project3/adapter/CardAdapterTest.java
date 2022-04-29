package com.group1.project3.adapter;

import junit.framework.TestCase;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.instanceOf;


public class CardAdapterTest extends TestCase {

    public void testOnCreateViewHolder() {
    }

    public void testOnBindViewHolder() {
    }

    public void testGetItemCount() {
        CardAdapter test = mock(CardAdapter.class);
        when(test.getItemCount()).thenReturn(9999);
        assertEquals(test.getItemCount(), 9999);
    }
}