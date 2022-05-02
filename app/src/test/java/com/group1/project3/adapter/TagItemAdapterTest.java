package com.group1.project3.adapter;

import com.group1.project3.model.Project;
import com.group1.project3.model.Tag;
import static org.mockito.Mockito.*;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

public class TagItemAdapterTest extends TestCase {

    /**
     * test for onCreateViewHolder
     */
    @Test
    public void testOnCreateViewHolder() {
        TagItemAdapter.ViewHolder mockViewHolder = mock(TagItemAdapter.ViewHolder.class);
        assertNotNull(mockViewHolder);
        assertTrue(mockViewHolder instanceof TagItemAdapter.ViewHolder);
    }

    /**
     * test for onBindViewHolder()
     */
    @Test
    public void testOnBindViewHolder() {
        //create mock tag adapter
        TagItemAdapter tagAdapter = mock(TagItemAdapter.class);

        //create mock arguments
        TagItemAdapter.ViewHolder mockViewHolder = mock(TagItemAdapter.ViewHolder.class);
        int mockPosition = 0;

        doAnswer(invocation -> {
            //grab arguments
            TagItemAdapter.ViewHolder holder = invocation.getArgument(0);
            int position = invocation.getArgument(1);

            //check arguments
            assertEquals(mockViewHolder, holder);
            assertEquals(mockPosition, position);

            return null;
        }).when(tagAdapter).onBindViewHolder(any(TagItemAdapter.ViewHolder.class), anyInt());

        //test function
        tagAdapter.onBindViewHolder(mockViewHolder, mockPosition);
        //verify that it ran once
        verify(tagAdapter, times(1));
    }

    /**
     * test for getItemCount()
     */
    @Test
    public void testGetItemCount() {
        Project project = new Project();
        project.addTag(new Tag());
        project.addTag(new Tag("tset"));
        TagItemAdapter test = new TagItemAdapter(project);

        Assert.assertEquals(test.getItemCount(), 2);
    }
}