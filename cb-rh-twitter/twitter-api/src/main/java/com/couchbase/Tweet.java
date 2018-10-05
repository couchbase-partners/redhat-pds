package com.couchbase;


public class Tweet {

    private Long id;
    private Long epochMin;
    private String text;
    private String createdAt;
    private String name;
    private String profileImg;

    public Tweet(Long id, String text, String createdAt, Long epochMin, String name, String profileImg) {
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
        this.epochMin = epochMin;
        this.name = name;
        this.profileImg = profileImg;
    }

    public Tweet() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Long getEpochMin() {
        return epochMin;
    }

    public void setEpochMin(Long epochMin) {
        this.epochMin = epochMin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }
}
