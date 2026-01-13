package com.example.mlbbtutorial.model;

public class ItemCategoryModel {

    private final String name; // tampil di UI (Attack)
    private final String slug; // untuk intent (attack)

    public ItemCategoryModel(String name, String slug) {
        this.name = name;
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }
}
