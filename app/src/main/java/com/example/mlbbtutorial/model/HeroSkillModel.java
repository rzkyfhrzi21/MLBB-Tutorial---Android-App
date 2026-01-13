package com.example.mlbbtutorial.model;

import java.io.Serializable;
import java.util.ArrayList;

public class HeroSkillModel implements Serializable {

    private final String name;
    private final String iconUrl;
    private final String type;
    private final String description;

    private final ArrayList<String> uniques;
    private final ArrayList<Integer> cooldown;
    private final ArrayList<Integer> manaCost;

    public HeroSkillModel(
            String name,
            String iconUrl,
            String type,
            String description,
            ArrayList<String> uniques,
            ArrayList<Integer> cooldown,
            ArrayList<Integer> manaCost
    ) {
        this.name = name;
        this.iconUrl = iconUrl;
        this.type = type;
        this.description = description;
        this.uniques = uniques;
        this.cooldown = cooldown;
        this.manaCost = manaCost;
    }

    public String getName() { return name; }
    public String getIconUrl() { return iconUrl; }
    public String getType() { return type; }
    public String getDescription() { return description; }
    public ArrayList<String> getUniques() { return uniques; }
    public ArrayList<Integer> getCooldown() { return cooldown; }
    public ArrayList<Integer> getManaCost() { return manaCost; }
}
