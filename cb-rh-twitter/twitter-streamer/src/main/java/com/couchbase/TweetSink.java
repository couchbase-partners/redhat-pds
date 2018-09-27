package com.couchbase;

public interface TweetSink {

    public void connect();
    public void addTweet(String msg);
}
