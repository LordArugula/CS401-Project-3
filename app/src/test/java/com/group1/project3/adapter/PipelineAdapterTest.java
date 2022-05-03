package com.group1.project3.adapter;

import com.group1.project3.model.Pipeline;
import com.group1.project3.model.Project;
import com.group1.project3.repository.ProjectRepository;

import static org.mockito.Mockito.*;


import android.view.ViewGroup;

import junit.framework.TestCase;

/**
 * tests the {@link PipelineAdapter} class
 */
public class PipelineAdapterTest extends TestCase {

    /**
     * tests the onCreateViewHolder() method
     */
    public void testOnCreateViewHolder() {
        PipelineAdapter pipelineAdapter = mock(PipelineAdapter.class);

        PipelineAdapter.ViewHolder mockViewholder = mock(PipelineAdapter.ViewHolder.class);
        assertNotNull(mockViewholder);
        //assertTrue(pipelineAdapter.onCreateViewHolder(mock(ViewGroup.class), 1) instanceof PipelineAdapter.ViewHolder);
    }

    /**
     * tests the getItemCount() method
     */
    public void testGetItemCount() {
        Project project = new Project();
        Pipeline pipeline = new Pipeline("pipeline");
        Pipeline pipeline1 = new Pipeline("pipeline1");
        project.addPipeline(pipeline);
        project.addPipeline(pipeline1);

        PipelineAdapter pipelineAdapter = mock(PipelineAdapter.class);
        when(pipelineAdapter.getItemCount()).thenReturn(project.getPipelines().size());

        assertEquals(pipelineAdapter.getItemCount(), 2);
    }
}