package com.example.mlbbtutorial.model;

public class ListBattleSpellModel {

    private String name;
    private String icon;
    private String usage;

    private String description;
    private int cooldown;
    private int unlockedAtLevel;

    public ListBattleSpellModel(
            String name,
            String icon,
            String usage,
            String description,
            int cooldown,
            int unlockedAtLevel
    ) {
        this.name = name;
        this.icon = icon;
        this.usage = usage;
        this.description = description;
        this.cooldown = cooldown;
        this.unlockedAtLevel = unlockedAtLevel;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public String getUsage() {
        return usage;
    }

    public String getDescription() {
        return description;
    }

    public int getCooldown() {
        return cooldown;
    }

    public int getUnlockedAtLevel() {
        return unlockedAtLevel;
    }
}
