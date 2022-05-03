package com.group1.project3.adapter;

import com.group1.project3.model.Tag;

import junit.framework.TestCase;
import static org.mockito.Mockito.*;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * tests the {@link TagIconAdapter} class
 */
public class TagIconAdapterTest extends TestCase {

    /**
     * tests the getItemCount() method
     */
    public void testGetItemCount() {
        List<Tag> tags = new ArrayList<>();
        Tag tag = new Tag();
        Tag tag1 = new Tag("tag1");
        tags.add(tag);
        tags.add(tag1);
        TagIconAdapter tagIconAdapter = new TagIconAdapter(tags);

        Assert.assertEquals(tagIconAdapter.getItemCount(), tags.size());
    }
}