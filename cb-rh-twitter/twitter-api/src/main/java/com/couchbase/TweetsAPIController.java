package com.couchbase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.couchbase.client.java.*;
import com.couchbase.client.java.document.*;
import com.couchbase.client.java.document.json.*;
import com.couchbase.client.java.query.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TweetsAPIController {


    @Value("#{environment['COUCHBASE_CLUSTER']}")
    private String couchbaseAddress;

    @Value("#{environment['COUCHBASE_USER']}")
    private String couchbaseUserName;

    @Value("#{environment['COUCHBASE_PASSWORD']}")
    private String couchbasePassword;

    @Value("#{environment['COUCHBASE_TWEET_BUCKET']}")
    private String bucketName;

    private static final Logger log = LoggerFactory.getLogger(TweetsAPIController.class);

    private CouchbaseCluster cluster;
    private Bucket tweetBucket;


    public TweetsAPIController() {
    }

    @PostConstruct
    public void init() {
        if (couchbaseAddress == null) {
            log.error("Missing COUCHBASE_CLUSTER environment variable.");
            return;
        }

        //connect
        cluster = CouchbaseCluster.create(couchbaseAddress);
        cluster.authenticate(couchbaseUserName, couchbasePassword);
        tweetBucket = cluster.openBucket(bucketName);

        //create primary index, if it doesn't exist
        //tweetBucket.bucketManager().createN1qlPrimaryIndex(true, false);
        tweetBucket.query(
                N1qlQuery.simple("CREATE PRIMARY INDEX `#primary` ON `tweets` WITH { \"num_replica\" : 2 }")
        );
    }


    @CrossOrigin(origins = "*")
    @RequestMapping("/tweets")
    public  List<Tweet> tweets() {
        //query
        N1qlQueryResult result = tweetBucket.query(
                N1qlQuery.simple("select id, text, created_at, epoch_min, `user`.name, `user`.screen_name, `user`.location, `user`.profile_image_url_https from tweets order by created_at desc limit 10;")
        );

        List<Tweet> resp = new ArrayList<>();
        for (N1qlQueryRow row : result) {
            Tweet tweet = new Tweet(
                    row.value().getLong("id"),
                    row.value().getString("text"),
                    row.value().getString("created_at"),
                    row.value().getLong("epoch_min"),
                    row.value().getString("name"),
                    row.value().getString("profile_image_url_https"));
            resp.add(tweet);
        }
        return resp;

    }
}
