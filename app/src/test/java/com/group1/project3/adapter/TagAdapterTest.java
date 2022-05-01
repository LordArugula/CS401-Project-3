package com.group1.project3.adapter;

import com.group1.project3.model.Project;
import com.group1.project3.model.Tag;
import static org.mockito.Mockito.*;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

public class TagAdapterTest extends TestCase {

    /**
     * test for onCreateViewHolder
     */
    @Test
    public void testOnCreateViewHolder() {
    }

    /**
     * test for onBindViewHolder()
     */
    @Test
    public void testOnBindViewHolder() {
        //create mock tag adapter
        TagAdapter tagAdapter = mock(TagAdapter.class);

        //create mock arguments
        TagAdapter.ViewHolder mockViewHolder = mock(TagAdapter.ViewHolder.class);
        int mockPosition = 0;

        doAnswer(invocation -> {
            //grab arguments
            TagAdapter.ViewHolder holder = invocation.getArgument(0);
            int position = invocation.getArgument(1);

            //check arguments
            assertEquals(mockViewHolder, holder);
            assertEquals(mockPosition, position);

            return null;
        }).when(tagAdapter).onBindViewHolder(any(TagAdapter.ViewHolder.class), anyInt());

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
        project.addTag(new Tag());
        TagAdapter test = new TagAdapter(project);

        Assert.assertEquals(test.getItemCount(), 2);
    }
}