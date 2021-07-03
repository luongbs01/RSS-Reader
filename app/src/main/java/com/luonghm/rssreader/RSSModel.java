package com.luonghm.rssreader;

public class RSSModel {
    private String title;
    private String image;
    private String pubDate;
    private String link;

    public RSSModel() {}

    public RSSModel(String title, String image, String pubDate, String link) {
        this.title = title;
        this.image = image;
        this.pubDate = pubDate;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getLink() {
        return link;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
