
## Deploying

Make sure you have the openjdk source builder image:

```
oc import-image registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift --confirm
```

Now, deploy directly from github:

```
oc new-app registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift:latest~https://github.com/ezeev/cb-rh-twitter.git \
       -e TWITTER_CONSUMER_KEY=YOUR_CONSUMER_KEY \
       -e TWITTER_CONSUMER_SECRET=YOUR_CONSUMER_SECRET \
       -e TWITTER_TOKEN=YOUR_TOKEN \
       -e TWITTER_SECRET=YOUR_SECRET \
       -e TWITTER_FILTER='filter1,filter2' \
       -e COUCHBASE_CLUSTER=cb-example \
       -e COUCHBASE_USER=Administrator \
       -e COUCHBASE_PASSWORD=password \
       -e COUCHBASE_TWEET_BUCKET=tweets \       
       --context-dir=twitter-streamer \
```

### Minishift Bug Workarounds
https://github.com/minishift/minishift-centos-iso/issues/251

Fix for image pulling error:
```
minishift ssh
sudo su
cd /etc/rhsm/ca/
touch redhat-uep.pem
```

Fix for 401 from twitter stream because of minishift time drift. This will correct the time locally on minishift:
https://github.com/docker/for-mac/issues/2076
```
ssh minishift
sudo su
docker run --rm --privileged alpine hwclock -s
```
