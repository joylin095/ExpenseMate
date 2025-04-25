package com.example.expensemate.manager;

import com.example.expensemate.model.Tag;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class TagManager {
    private Set<Tag> tagList;

    public TagManager() {
        this.tagList = new LinkedHashSet<>();
        this.tagList.add(new Tag("食費"));
        this.tagList.add(new Tag("交通費"));
        this.tagList.add(new Tag("娯楽費"));
        this.tagList.add(new Tag("光熱費"));
        this.tagList.add(new Tag("通信費"));
        this.tagList.add(new Tag("医療費"));
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
