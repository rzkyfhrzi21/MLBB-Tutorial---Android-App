package com.example.mlbbtutorial.model;

import java.util.ArrayList;

public class ListItemModel {

    public static class Effect {
        public String name;
        public String desc;

        public Effect(String name, String desc) {
            this.name = name;
            this.desc = desc;
        }
    }

    public String name;
    public String icon;
    public Integer price;
    public Integer sell;

    public ArrayList<String> attributes;
    public ArrayList<Effect> uniquePassive;
    public ArrayList<Effect> uniqueActive;

    public String getName() {
        return name;
    }


    public ListItemModel(
            String name,
            String icon,
            Integer price,
            Integer sell,
            ArrayList<String> attributes,
            ArrayList<Effect> uniquePassive,
            ArrayList<Effect> uniqueActive
    ) {
        this.name = name;
        this.icon = icon;
        this.price = price;
        this.sell = sell;
        this.attributes = attributes;
        this.uniquePassive = uniquePassive;
        this.uniqueActive = uniqueActive;
    }
}
