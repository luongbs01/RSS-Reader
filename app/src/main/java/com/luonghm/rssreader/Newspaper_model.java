package com.luonghm.rssreader;

import java.util.LinkedHashMap;

public class Newspaper_model {
    private String name;
    private String image;
    private LinkedHashMap<String, String> tab;

    public Newspaper_model() {}

    public Newspaper_model(String name, String image, LinkedHashMap<String, String> tab) {
        this.name = name;
        this.image = image;
        this.tab = tab;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public LinkedHashMap<String, String> getTab() {
        return tab;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTab(LinkedHashMap<String, String> tab) {
        this.tab = tab;
    }
}
