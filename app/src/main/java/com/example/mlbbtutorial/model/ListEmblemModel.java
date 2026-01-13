package com.example.mlbbtutorial.model;

import java.util.ArrayList;

public class ListEmblemModel {

    // "main" atau "ability"
    private String kind;

    private String name;
    private String iconPath;

    // MAIN EMBLEM
    private ArrayList<String> attributes;

    // ABILITY EMBLEM
    private int section;         // 1/2/3
    private String benefits;
    private String desc;
    private Integer cd;          // nullable

    // ===== MAIN EMBLEM CONSTRUCTOR =====
    public ListEmblemModel(String name, String iconPath, ArrayList<String> attributes) {
        this.kind = "main";
        this.name = name;
        this.iconPath = iconPath;
        this.attributes = attributes;
    }

    // ===== ABILITY EMBLEM CONSTRUCTOR =====
    public ListEmblemModel(String name, String iconPath, int section, String benefits, String desc, Integer cd) {
        this.kind = "ability";
        this.name = name;
        this.iconPath = iconPath;
        this.section = section;
        this.benefits = benefits;
        this.desc = desc;
        this.cd = cd;
    }

    public String getKind() {
        return kind;
    }

    public String getName() {
        return name;
    }

    public String getIconPath() {
        return iconPath;
    }

    public ArrayList<String> getAttributes() {
        return attributes;
    }

    public int getSection() {
        return section;
    }

    public String getBenefits() {
        return benefits;
    }

    public String getDesc() {
        return desc;
    }

    public Integer getCd() {
        return cd;
    }
}
