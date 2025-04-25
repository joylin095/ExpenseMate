package com.example.expensemate.model;

public class Tag {
    private String name;

    public Tag(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean matchName(String name) {
        return this.name.equals(name);
    }
}
