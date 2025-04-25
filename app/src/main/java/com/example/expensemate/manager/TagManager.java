package com.example.expensemate.manager;

import com.example.expensemate.model.Tag;

import java.util.LinkedHashSet;
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

    public Set<Tag> getTagList() {
        return tagList;
    }
}
