package com.couchbase;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.TimeUnit;

public class CbQueryManager {

    private static final Logger log = LoggerFactory.getLogger(CbQueryManager.class);

    @Value("#{environment['COUCHBASE_CLUSTER']}")
    private String couchbaseAddress;

    @Value("#{environment['COUCHBASE_USER']}")
    private String couchbaseUserName;

    @Value("#{environment['COUCHBASE_PASSWORD']}")
    private String couchbasePassword;

    @Value("#{environment['COUCHBASE_TWEET_BUCKET']}")
    private String bucketName;

    private CouchbaseCluster cluster;
    private Bucket tweetBucket;

    private boolean acceptingConnections;

    public CbQueryManager() {
        this.connect();
    }


    public void connect() {
        if (couchbaseAddress == null) {
            log.error("Missing COUCHBASE_CLUSTER environment variable.");
            this.acceptingConnections = false;
            return;
        }
        try {
            //connect
            cluster = CouchbaseCluster.create(couchbaseAddress);
            cluster.authenticate(couchbaseUserName, couchbasePassword);
            tweetBucket = cluster.openBucket(bucketName);
            //create primary index, if it doesn't exist
            tweetBucket.bucketManager().createN1qlPrimaryIndex(true, false);
            this.acceptingConnections = true;
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            this.acceptingConnections = false;
        }
    }

    public boolean isAcceptingConnections() {
        return acceptingConnections;
    }

    public N1qlQueryResult queryBucket(Bucket bucket, N1qlQuery query) {

        try {
            N1qlQueryResult result = bucket.query(
                    query,
                    5,
                    TimeUnit.SECONDS);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            this.acceptingConnections = false;
            return null;
        }
    }

}
