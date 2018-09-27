package com.couchbase;

import java.util.concurrent.atomic.AtomicInteger;

public final class TweetStreamStatsCollector {


    private static AtomicInteger processedTweetCounter = new AtomicInteger();
    public static int getProcessedTweetCount() {
        return processedTweetCounter.get();
    }

    public static void incrementTweetsProcessed() {
        processedTweetCounter.incrementAndGet();
    }



}
