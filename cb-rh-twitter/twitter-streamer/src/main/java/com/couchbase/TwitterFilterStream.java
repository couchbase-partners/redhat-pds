package com.couchbase;

import com.google.common.collect.Lists;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import com.twitter.hbc.ClientBuilder;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class TwitterFilterStream implements DisposableBean, Runnable {

    private Thread thread;

    public TwitterFilterStream() {

    }

    @PostConstruct
    public void startThread() {
        this.thread = new Thread(this);
        this.thread.start();
    }


    @Autowired
    private CouchbaseSink sink;

    private static final Logger log = LoggerFactory.getLogger(TwitterFilterStream.class);

    @Value("#{environment['TWITTER_CONSUMER_KEY']}")
    private String consumerKey;

    @Value("#{environment['TWITTER_CONSUMER_SECRET']}")
    private String consumerSecret;

    @Value("#{environment['TWITTER_TOKEN']}")
    private String token;

    @Value("#{environment['TWITTER_SECRET']}")
    private String secret;

    @Value("#{environment['TWITTER_FILTER']}")
    private String filterString;


    @Override
    public void run() {

        log.info("Starting twitter stream using filters: " + filterString);

        List<String> terms = Lists.newArrayList(filterString.split(","));

        BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10000);
        StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();

        endpoint.trackTerms(terms);

        Authentication auth = new OAuth1(consumerKey, consumerSecret, token, secret);

        while(true) {

            Client client = new ClientBuilder()
                    .hosts(Constants.STREAM_HOST)
                    .endpoint(endpoint)
                    .authentication(auth)
                    .processor(new StringDelimitedProcessor(queue))
                    .build();
            client.connect();


            for (int msgRead = 0; msgRead < 1000; msgRead++) {
                try {
                    String msg = queue.take();
                    sink.addTweet(msg);
                    TweetStreamStatsCollector.incrementTweetsProcessed();
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                    e.printStackTrace();
                } catch (Exception e) {
                    log.error(e.getMessage());
                    e.printStackTrace();
                }
            }
            client.stop();
        }

    }

    @Override
    public void destroy() throws Exception {

    }
}
