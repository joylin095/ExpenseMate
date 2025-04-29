package com.example.expensemate.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class TagManager {
    private Set<Tag> tagList;

    public TagManager() {
        this.tagList = new LinkedHashSet<>();
        this.tagList.add(new Tag("食物"));
        this.tagList.add(new Tag("娛樂"));
        this.tagList.add(new Tag("交通"));
    }

    public void createTag(String tagName) {
        for (Tag tag : tagList) {
            if (tag.matchName(tagName)) {
                return; // Tag already exists
            }
        }
        Tag newTag = new Tag(tagName);
        tagList.add(newTag);
    }

    public Tag selectTag(String tagName) {
        for (Tag tag : tagList) {
            if (tag.matchName(tagName)) {
                return tag;
            }
        }
        return null;
    }

    public List<String> getTagList() {
        List<String> tagNames = new ArrayList<>();
        for (Tag tag : tagList) {
            tagNames.add(tag.getName());
        }
        return tagNames;
    }
}
