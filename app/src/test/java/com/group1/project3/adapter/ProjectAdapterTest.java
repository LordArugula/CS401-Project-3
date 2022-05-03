package com.group1.project3.adapter;

import junit.framework.TestCase;
import static org.mockito.Mockito.*;

import android.view.ViewGroup;

import com.group1.project3.model.Project;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ProjectAdapterTest extends TestCase {

    /**
     * tests the onCreateViewHolder() method
     */
    public void testOnCreateViewHolder() {
        ProjectAdapter projectAdapter = mock(ProjectAdapter.class);
        ProjectAdapter.ViewHolder mockViewHolder = mock(ProjectAdapter.ViewHolder.class);
        ViewGroup parent = mock(ViewGroup.class);
        when(projectAdapter.onCreateViewHolder(parent, 1)).thenReturn(mockViewHolder);

        Assert.assertSame(projectAdapter.onCreateViewHolder(parent, 1), mockViewHolder);
    }

    /**
     * tests the getItemCount() method
     */
    public void testGetItemCount() {
        List<Project> data = new ArrayList<>();
        Project project = new Project();
        Project project1 = new Project();
        data.add(project);
        data.add(project1);

        ProjectAdapter projectAdapter = new ProjectAdapter(data, mock(ProjectAdapter.OnProjectCardClickListener.class));

        Assert.assertEquals(projectAdapter.getItemCount(), data.size());
    }
}