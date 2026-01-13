package com.example.mlbbtutorial.model;

import java.util.ArrayList;

public class ListHeroModel {

    private final String heroId;
    private final String heroName;
    private final String heroIcon;
    private final String heroUrl;
    private final ArrayList<String> roles;

    public ListHeroModel(
            String heroId,
            String heroName,
            String heroIcon,
            String heroUrl,
            ArrayList<String> roles
    ) {
        this.heroId = heroId;
        this.heroName = heroName;
        this.heroIcon = heroIcon;
        this.heroUrl = heroUrl;
        this.roles = roles;
    }

    public String getHeroId() {
        return heroId;
    }

    public String getHeroName() {
        return heroName;
    }

    public String getHeroIcon() {
        return heroIcon;
    }

    public String getHeroUrl() {
        return heroUrl;
    }

    public ArrayList<String> getRoles() {
        return roles;
    }
}
