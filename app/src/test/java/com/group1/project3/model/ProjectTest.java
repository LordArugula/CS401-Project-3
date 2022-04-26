package com.group1.project3.model;

import android.graphics.Color;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * test for {@link Project} class
 */
public class ProjectTest extends TestCase {
    /**
     * tests setters for id, name, ownerId, and color and getters for id, name, ownerId, color, user roles, editors, pipelines, and tags
     */
    public void testBasicSettersGetters() {
        String id = "123";
        String name = "name";
        String ownerId = "ownerId";
        Map<String, Role> editors = new HashMap<>();
        editors.put(ownerId, Role.Admin);
        List<Pipeline> pipelines = new ArrayList<>();
        List<Tag> tags = new ArrayList<>();
        int color = 0;

        Project test = new Project();
        test.setId(id);
        test.setName(name);
        test.setOwnerId(ownerId);
        test.setColor(color);

        assertEquals(test.getId(), id);
        assertEquals(test.getName(), name);
        assertEquals(test.getOwnerId(), ownerId);
        assertEquals(test.getColor(), color);
        assertEquals(test.getUserRole("none"), Role.None);
        assertEquals(test.getEditors(), editors);
        assertEquals(test.getPipelines(), pipelines);
        assertEquals(test.getTags(), tags);
    }

    /**
     * tests the second constructor
     */
    public void testSecondConstructor() {
        String id = "123";
        String name = "name";
        String ownerId = "ownerId";
        Map<String, Role> editors = new HashMap<>();

        Project test = new Project(id, name, ownerId);
        editors.put(ownerId, Role.Admin);

        assertEquals(test.getId(), id);
        assertEquals(test.getName(), name);
        assertEquals(test.getOwnerId(), ownerId);
        assertEquals(test.getEditors(), editors);
    }

    /**
     * tests updateOrAddEditor(), isEditor(), and removeEditor()
     */
    public void testEditorFunctions() {
        String ownerId = "ownerId";
        Project test = new Project();
        Map<String, Role> editors = new HashMap<>();
        editors.put(ownerId, Role.Admin);

        //test updateOrAddEditor
        test.updateOrAddEditor(ownerId, Role.Admin);
        assertEquals(test.getEditors(), editors);

        //test isEditor
        assertTrue(test.isEditor(ownerId));

        //test removeEditor
        test.removeEditor(ownerId);
        assertFalse(test.isEditor(ownerId));
    }

    /**
     * tests addPipeline() and removePipeline()
     */
    public void testPipelineFunctions() {
        List<Pipeline> pipelines = new ArrayList<>();
        Project test = new Project();
        Pipeline testPipeline = new Pipeline();
        Pipeline testPipeline2 = new Pipeline();
        pipelines.add(testPipeline);
        pipelines.add(testPipeline2);

        assertTrue(test.addPipeline(testPipeline));
        assertTrue(test.addPipeline(testPipeline2));
        assertEquals(test.getPipelines(), pipelines);

        assertTrue(test.removePipeline(testPipeline));
        pipelines.remove(testPipeline);
        assertEquals(test.getPipelines(), pipelines);
    }

    /**
     * tests addTag() and removeTag()
     */
    public void testTagFunctions() {
        List<Tag> tags = new ArrayList<>();
        Project test = new Project();
        String name = "name";
        int color = 123;
        String name2 = "name2";
        int color2 = 321;
        //int color = Color.parseColor("#00ff00");
        Tag testTag = new Tag(name, color);
        Tag testTag2 = new Tag(name2, color2);

        assertTrue(test.addTag(testTag));
        assertTrue(test.addTag(testTag2));
        tags.add(testTag);
        tags.add(testTag2);
        assertEquals(test.getTags(), tags);

        assertTrue(test.removeTag(testTag));
        tags.remove(testTag);
        assertEquals(test.getTags(), tags);
    }
}