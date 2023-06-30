

# Couchbase Operator For OpenShift

The Couchbase Operator allows users to deploy Couchbase on OpenShift while significantly reducing the operational complexities typically associated with managing and scaling databases.

Related resources:

- [Autonomous Operator Overview](https://docs.couchbase.com/operator/current/overview.html)
- [Installing on OpenShift](https://docs.couchbase.com/operator/current/install-openshift.html)
- [Red Hat Partner Demo System](https://rhpds.redhat.com/)

## Summary

This guide is intended to be run with with Red Hat's OpenShift Workshop on [RHPDS (Partner Demo System)](https://rhpds.redhat.com/) for demos and workshops. It provides steps and examples for deploying Couchbase clusters using the Autonomous operator.

All of these steps can also be run on any other supported OpenShift environment (tested on 4.10). Just replace the URLs in the steps below with the address to your environment. Throughout this guide you will see references to `CLUSTER_ID`. This ID is different each time a PDS cluster is created. Please reach out to partners@couchbase.com if you need access to a cluster, and a complete URL with `CLUSTER_ID` will be provided.

The RHPDS environment should be deployed using the "Couchbase Cluster OpenShift Demo & Workshop" from the "Multi-Product Demo" Service Catalog.  Your cluster will be deployed with a current version of OpenShift and the latest published Couchbase Operator.

# Setup

### Login to Red Hat OpenShift Container Platform

The first step is to login to OpenShift from your local browser AND terminal.

Open  https://console-openshift-console.apps.cluster-<CLUSTER_ID>.opentlc.com in your browser and login:

- username: cluster-mgr
- password: <Provided-in-RHPDS-confirmation-email>


On the main console page, navigate to 'cluster-mgr' in the upper right hand corner and click 'copy login command' for command-line access.  Click 'Display Token' and paste the output to your terminal.

```
oc login --token=sha256~QMJIQOfJq00YNoVh2nQUgwsy6ZifXS7Jt4OTsnJAP8Q --server=https://api.cluster-<CLUSTER_ID>.opentlc.com:6443
```

> Note: cluster-mgr is a cluster admin account. Cluster admin privileges are advised to install the Couchbase Cluster.

### Create Project

Next, we need to create a project.

```
oc new-project couchbase
```

This command creates the `couchbase` project and switches to it.

### Deploy Couchbase Credentials Secret

The Couchbase clusters deployed in the following steps will use the credentials provided in the `cb-example-auth` secret. Deploying `secret.yaml` will create the secret.

```
oc create -f secret.yaml
```

# Cluster Recipes

### Deploy a Basic Couchbase Cluster

The first cluster that we'll deploy will be a simple, 3 node cluster, with one bucket and 2 replicas.

```
oc create -f cluster-basic.yaml
```

You should start seeing Couchbase pods appearing immediately. It will take a couple of minutes for the cluster to be ready.

```
oc get pods -w
NAME                                  READY     STATUS    RESTARTS   AGE
cb-example-0000                       1/1       Running   0          3m
cb-example-0001                       1/1       Running   0          3m
cb-example-0002                       1/1       Running   0          2m
```
> Remember to hit `ctrl + c` to escape.

### Expose the Couchbase UI

Next, expose the Couchbase UI so you can log into it:

```
oc expose service/cb-example-ui
```

Get the route to the Couchbase UI:

```
oc get routes
NAME            HOST/PORT                                                             PATH      SERVICES        PORT        TERMINATION   WILDCARD
cb-example-ui   cb-example-ui-couchbase.apps.cluster-<DLUSTER_ID>.opentlc.com            cb-example-ui   couchbase-ui                 None
```

Open the URL outputted by `oc get routes` in your browser adding /ui/index.html at the end and login with:
> Username: Administrator
> Password: password 

Navigate to "Servers" to see the server list:

![Basic Couchbase Cluster](img/cb-cluster-basic.png)

On the Pods page in OpenShift (https://console-openshift-console.apps.cluster-<CLUSTER_ID>.opentlc.com/k8s/ns/couchbase/pods):

![](img/os-cluster-basic.png)


### Build and Deploy an App

![](img/couchbase-app-1.png)

> Note: in order to follow this section, you will need a twitter developer account. If you do not have an account, please contact david.schexnayder@couchbase.com and I will provide temporary credentials.

In order to help demonstrate the Couchbase Autonomous Operator in action, we'll deploy a simple real-time analytics application that ingests tweets from Twitter's API into Couchbase. We will then simulate a node failure and observe how the application and Couchbase respond.

The application is made up of 3 microservices:

1. Tweet Ingester Service - The tweet ingester is a Java application that consumes tweet in real-time from Twitter's APIs into Couchbase.
2. API Service - The API service is Java application that provides several REST end points for exposing data ingested by the Tweet Ingester Service. Under the hood, it is running SQL queries against Couchbase.
3. UI Service - The UI service is a simple Node server that serves a React SPA (single page application). The UI provides visualizations of the data provided by the API Service.

#### Deploy the API Service

First, we'll deploy the API service.

```
oc new-app registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift:latest~https://github.com/couchbase-partners/redhat-pds.git#release-2.0 \
      -e COUCHBASE_CLUSTER=cb-example \
      -e COUCHBASE_USER=Administrator \
      -e COUCHBASE_PASSWORD=password \
      -e COUCHBASE_TWEET_BUCKET=tweets \
      --context-dir=cb-rh-twitter/twitter-api \
      --name=twitter-api
```

You can watch the build process by running `oc logs -f bc/twitter-api`. Once this is completed it will deploy a pod running the API service.

Now let's expose the API service so it is accessible publicly:

```
oc expose svc twitter-api
```

This should create a route to http://twitter-api-operator-example.apps.couchbase-<CLUSTER_ID>.openshiftworkshop.com. Open the URL http://twitter-api-operator-example.apps.couchbase-<CLUSTER_ID>.openshiftworkshop.com/tweetcount in your browser and you should see a number 0 in your browser. This is a simple API endpoint that returns the number of tweets ingested.

#### Deploy the UI Service

Next, we'll deploy the UI service. This service is a simple node server serving up a ReactJS app. For expediency, a Docker image is already built. We can also deploy Docker images directly with the `new-app` command:

```
oc new-app ezeev/twitter-ui:latest
```

This will deploy our UI service. Let's expose it so we can access it:

```
oc expose svc twitter-ui
```

This should expose a route to https://twitter-ui-couchbase.apps.cluster-<CLUSTER_ID>.opentlc.com/. Visit this link. You should see a dashboard load **with empty charts**. We will start populating them in the next step after deploying the Tweet Ingester Service.

Now, add the following request parameter to the URL in your browser: `?apiBase=<exposed route to API service>`. The complete URL should look like:

```
https://twitter-ui-couchbase.apps.cluster-<CLUSTER_ID>.opentlc.com/?apiBase=http://twitter-api-operator-example.apps.couchbase-<CLUSTER_ID>.openshiftworkshop.com
```


#### Deploy the Tweet Ingester Service

Now that we have our API and UI deployed, we are ready to start ingesting and visualizing twitter data! This is a Java application like the API service, so we will deploy it the exact same way:

```
oc new-app registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift:latest~https://github.com/couchbase-partners/redhat-pds.git#release-2.0 \
       -e TWITTER_CONSUMER_KEY=p1AIfFhwlD0HUVOPwNwD3ZpR4 \
       -e TWITTER_CONSUMER_SECRET=5hnvkBgziPNnhGY6dMg2C5GEWB7dsayXwdp2F5Sl9B6bMfwzl6 \
       -e TWITTER_TOKEN=26575781-4KKPE5adSBRXN8s3OUWa6tFcYQNiPJ43UpBtXDiaD \
       -e TWITTER_SECRET=tybhfMESsotG6zUCFpqUsUHr08L0W0eHbxv2CglvY98Of \
       -e TWITTER_FILTER='#coronavirus' \
       -e COUCHBASE_CLUSTER=cb-example \
       -e COUCHBASE_USER=Administrator \
       -e COUCHBASE_PASSWORD=password \
       -e COUCHBASE_TWEET_BUCKET=tweets \
       --context-dir=cb-rh-twitter/twitter-streamer \
       --name=twitter-streamer
```

You can watch the build with `oc logs -f bc/cb-rh-twitter`. When this is completed you should see a new pod created for the twitter streamer.

At this point you should also see new documents appearing in the tweets bucket in Couchbase, and in the UI at http://twitter-ui-operator-example.apps.couchbase-<CLUSTER_ID>.openshiftworkshop.com/.

### Failover Demo

Now that we have a cluster up with some data, we can demonstrate the operator in action.

First, delete one of the pods:

```
oc delete pod cb-example-0002
```

By deleting the pod, we are destroying one of the Couchbase nodes. At this point the operator should take over and try to recover the cluster to our desired state.

Couchbase recognizes that a node is missing and triggers fail-over:

![](img/failover-1.png)

Couchbase recognizes the new node coming online and begins rebalancing:

![](img/failover-2.png)

The rebalance continues until the cluster is fully healed.

![](img/failover-3.png)

### Cleanup

Delete the cluster before moving onto the next example:

```
oc delete -f cluster-basic.yaml
```

To remove the twitter streaming app:

```
oc delete dc twitter-streamer
oc delete bc twitter-streamer
oc delete svc twitter-streamer
```

## Deploy a Cluster with Server Groups Enabled

First, we need to add labels to our OpenShift nodes. Labels are used to tell the Operator which zone a particular node belongs too. In this example, we'll declare the node1 and node2 belong to ServerGroup1. Our node2 and node3 will belong to ServerGroup2.

```
 oc label --overwrite nodes node1.couchbase.internal server-group.couchbase.com/zone=ServerGroup1
 oc label --overwrite nodes node2.couchbase.internal server-group.couchbase.com/zone=ServerGroup1
 oc label --overwrite nodes node3.couchbase.internal server-group.couchbase.com/zone=ServerGroup2
 oc label --overwrite nodes node4.couchbase.internal server-group.couchbase.com/zone=ServerGroup2
```

Now deploy the new cluster:

```
oc create -f cluster-server-groups.yaml
```

This deploys a 9 node cluster with ServerGroups enabled. The distribution of nodes is setup so that each zone has 2 Data nodes and 1 Query node. This allows us to keep 2 replicas of the default bucket in each zone.

![](img/9node-server-list.png)


[def]: https://docs.couchbase.com/operator/current/install-openshift.html