package com.couchbase;


public class Tweet {

    private Long id;
    private Long epochMin;
    private String text;
    private String createdAt;

    public Tweet(Long id, String text, String createdAt, Long epochMin) {
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
        this.epochMin = epochMin;
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

}
