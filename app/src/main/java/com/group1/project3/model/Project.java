package com.group1.project3.model;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A {@link Project} represents a real-life project using cards.
 */
public class Project {
    /**
     * The project id.
     */
    private String id;

    /**
     * The project name.
     */
    private String name;

    /**
     * The user id of the project owner.
     */
    private String ownerId;

    /**
     * The user ids and their roles of the users with access to this project.
     */
    private final HashMap<String, Role> editors;

    /**
     * The project pipelines.
     */
    private final List<Pipeline> pipelines;

    /**
     * The card tags in this project.
     */
    private final List<Tag> tags;

    /**
     * The project color.
     */
    private Color color;

    /**
     * Creates an empty project.
     */
    public Project() {
        this(null, null, null);
    }

    /**
     * Creates a project with a name and an associated owner.
     *
     * @param id      The project id.
     * @param name    The project name.
     * @param ownerId The user id of the project owner.
     */
    public Project(String id, String name, String ownerId) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        editors = new HashMap<>();
        editors.put(ownerId, Role.Owner);
        pipelines = new ArrayList<>();
        tags = new ArrayList<>();
    }

    /**
     * Gets the project id.
     *
     * @return the project id.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the project id.
     *
     * @param id the project id.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the project name.
     *
     * @return the project name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the project name
     *
     * @param name the project name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the user id of the project owner.
     *
     * @return the user id of the project owner.
     */
    public String getOwnerId() {
        return ownerId;
    }

    /**
     * Sets the project owner to the user with ownerId
     *
     * @param ownerId The user id of the new project owner.
     */
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * Gets the {@link Role} for the user with userId.
     *
     * @param userId The id of the user.
     * @return the {@link Role} of the user in this project.
     * Returns {@link Role}.None if the user does not have access to this project.
     */
    public Role getRoleForUser(String userId) {
        if (isEditor(userId)) {
            return editors.get(userId);
        }
        return Role.None;
    }

    /**
     * Updates the {@link Role} for the user.
     * If the user does not have access to this project, they are added to this project
     * with the {@link Role role}.
     * If the role is {@link Role}.None, they are removed from the project.
     *
     * @param userId The user id.
     * @param role   The {@link Role} of the user.
     */
    public void updateOrAddEditor(String userId, Role role) {
        if (role == Role.None) {
            editors.remove(userId);
            return;
        }
        editors.put(userId, role);
    }

    /**
     * Checks if the user with userId has access to this project.
     *
     * @param userId The user id.
     * @return True if the user has access to this project.
     */
    public boolean isEditor(String userId) {
        return editors.containsKey(userId) && editors.get(userId) != Role.None;
    }

    /**
     * Removes access from the user with userId.
     *
     * @param userId The user id.
     * @return True if the user was removed.
     */
    public boolean removeEditor(String userId) {
        if (editors.containsKey(userId)) {
            editors.remove(userId);
            return true;
        }
        return false;
    }

    /**
     * Gets a read-only map of user ids and their {@link Role}.
     *
     * @return a read-only map of user ids and their {@link Role}.
     */
    public Map<String, Role> getEditors() {
        return Collections.unmodifiableMap(editors);
    }

    /**
     * Gets a read-only collection of the project {@link Pipeline pipelines}.
     *
     * @return a read-only collection of the project {@link Pipeline pipelines}.
     */
    public List<Pipeline> getPipelines() {
        return Collections.unmodifiableList(pipelines);
    }

    /**
     * Adds a {@link Pipeline} to this project.
     *
     * @param pipeline The {@link Pipeline} to add.
     * @return True if the pipeline was added.
     */
    public boolean addPipeline(Pipeline pipeline) {
        return pipelines.add(pipeline);
    }

    /**
     * Removes a {@link Pipeline} from this project.
     *
     * @param pipeline The {@link Pipeline} to remove.
     * @return True if the {@link Pipeline} was removed.
     */
    public boolean removePipeline(Pipeline pipeline) {
        return pipelines.remove(pipeline);
    }

    /**
     * Gets a read-only collection of the {@link Tag tags} in this project.
     *
     * @return a read-only collection of the {@link Tag tags} in this project.
     */
    public List<Tag> getTags() {
        return Collections.unmodifiableList(tags);
    }

    /**
     * Adds a {@link Tag} to this project.
     *
     * @param tag the {@link Tag}.
     * @return True if the {@link Tag} was added.
     */
    public boolean addTag(Tag tag) {
        if (tags.contains(tag)) {
            return false;
        }
        return tags.add(tag);
    }

    /**
     * Removes the {@link Tag} from the project.
     *
     * @param tag The {@link Tag} to remove.
     * @return True if the tag was removed.
     */
    public boolean removeTag(Tag tag) {
        if (tags.contains(tag)) {
            for (Pipeline p : pipelines) {
                for (Card c : p.getCards()) {
                    c.removeTag(tag);
                }
            }
            return tags.remove(tag);
        }
        return false;
    }

    /**
     * Gets the project color.
     *
     * @return the project color.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the project color.
     *
     * @param color the project color.
     */
    public void setColor(Color color) {
        this.color = color;
    }

//    @Override
//    public Map<String, Object> serializeAsMap() {
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("name", name);
//        map.put("ownerId", ownerId);
//        map.put("color", color.toString());
//        List<Map<String, Object>> pipelinesAsMapArray = new ArrayList<>(pipelines.size());
//        for (Pipeline pipeline : pipelines) {
//            pipelinesAsMapArray.add(pipeline.serializeAsMap());
//        }
//        map.put("pipelines", pipelinesAsMapArray);
//
//        return Collections.unmodifiableMap(map);
//    }
}
