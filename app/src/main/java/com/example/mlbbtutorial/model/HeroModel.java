package com.example.mlbbtutorial.model;

import org.json.JSONObject;

import java.util.ArrayList;

public class HeroModel {
    public String heroName;
    public String heroIcon;
    public ArrayList<String> role;
    public ArrayList<String> speciality;
    public ArrayList<HeroSkillModel> skills;
    public JSONObject baseAttributes;

    public HeroModel(
            String heroName,
            String heroIcon,
            ArrayList<String> role,
            ArrayList<String> speciality,
            ArrayList<HeroSkillModel> skills,
            JSONObject baseAttributes
    ) {
        this.heroName = heroName;
        this.heroIcon = heroIcon;
        this.role = role;
        this.speciality = speciality;
        this.skills = skills;
        this.baseAttributes = baseAttributes;
    }
}
