package com.couchbase;

public class TweetStreamStats {

    private int tweetsProcessed;

    public TweetStreamStats() {
        tweetsProcessed = TweetStreamStatsCollector.getProcessedTweetCount();
    }

    public int getTweetsProcessed() {
        return tweetsProcessed;
    }

    public void setTweetsProcessed(int tweetsProcessed) {
        tweetsProcessed = tweetsProcessed;
    }
}
