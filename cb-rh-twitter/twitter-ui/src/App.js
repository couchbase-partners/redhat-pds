import React, { Component } from 'react';
import './App.css';
import TweetStream from './twitter-stream/tweet-stream.js'
import HashTagBarChart from './twitter-stream/hashtag-barchart.js'
import TweetRateLineChart from './twitter-stream/tweet-rate-line-chart.js'
import TweetCountStat from './twitter-stream/tweet-count-single-stat.js'
import Row from './layout/row.js'
import Col6 from './layout/col6.js'
import Col12 from './layout/col12.js'
import Card from './layout/card.js'


class App extends Component {
  render() {

    var apiBase = 'http://twitter-api-operator-example.apps.couchbase-9728.openshiftworkshop.com';

    var urlParams = new URLSearchParams(window.location.search);
    if (urlParams.has("apiBase")) {
      apiBase = urlParams.get("apiBase");
    }
    
    //const apiBase = 'http://twitter-api-operator-example.192.168.64.3.nip.io';

    return (
      <div className="container-fluid container-cards-pf">
        <Row>
          <Col6>
            <Card title="Live Tweets">
              <TweetStream apiBase={apiBase}></TweetStream>
            </Card>
          </Col6>
          <Col6>
            <Row>
            <Col6>
                <Card title="Tweets Ingested">
                 <TweetCountStat apiBase={apiBase}></TweetCountStat>
                </Card>
              </Col6>
              <Col6>
                <Card title="Ingestion Rate">
                 <TweetRateLineChart apiBase={apiBase}></TweetRateLineChart>
                </Card>
              </Col6>
            </Row>
            <Row>
              <Col12>
                <Card title="Popular Hashtags">
                  <HashTagBarChart apiBase={apiBase}></HashTagBarChart>
                </Card>
              </Col12>
             </Row>
          </Col6>
        </Row>
      </div>
    );
  }
}

export default App;
