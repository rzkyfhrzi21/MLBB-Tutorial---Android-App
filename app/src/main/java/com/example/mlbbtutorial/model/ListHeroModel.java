package com.example.mlbbtutorial.model;

public class ListHeroModel {

    private String heroId;
    private String heroName;
    private String heroIcon;
    private String heroUrl;

    public ListHeroModel(String heroId, String heroName, String heroIcon, String heroUrl) {
        this.heroId = heroId;
        this.heroName = heroName;
        this.heroIcon = heroIcon;
        this.heroUrl = heroUrl;
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
}
