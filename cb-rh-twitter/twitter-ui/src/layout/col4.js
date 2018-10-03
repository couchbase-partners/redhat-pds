import React, { Component } from 'react';

class Col4 extends Component {
    render() {
        return (
            <div className="col-xs-12 col-sm-6 col-md-4">{this.props.children}</div>
        );
    }
}
export default Col4;

/*
curl -u admin:password123 -XPOST -H "Content-Type: application/json" \
http://127.0.0.1:8094/api/index/tweets/query \
-d '{
  "explain": false,
  "fields": [
    "entities.hashtags"
  ],
  "highlight": {},
  "query": {
    "query": "trump"
  }
}'
*/