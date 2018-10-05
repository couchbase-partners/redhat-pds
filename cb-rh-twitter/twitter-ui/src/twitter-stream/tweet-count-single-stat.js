
import React, { Component } from 'react';
import C3Chart from 'react-c3js';
import 'c3/c3.css';
import Clock from './ticker';


/*const data = {
    columns: [
      ['count', 30, 200, 100, 400, 150, 250]
    ],
    type: 'bar'
  };

const axis = {
    rotated: true,
    x: {
        type: 'category', // this needed to load string x value
        categories: ['tag1', 'tag2', 'tag3', 'tag4', 'tag5', 'tag6']   
    }
}*/


class TweetCountStat extends Component {
    constructor(props) {
        super(props);
        this.state = {
            count: 0,
        };
    }

    componentDidMount() {
        var interval = 5000;
        this.timerID = setInterval(
            () => this.tick(),
            interval
        )
        this.tick();
    }

    tick() {
        fetch('http://localhost:8080/tweetcount')
        .then(res => res.json())
                .then(
                    (result) => {
                        this.setState({
                            count: result.toLocaleString(),
                        });
                    },
                )
                .catch(error => {
                    this.setState({
                        errorMsg: "Unable to query tweets. Please contact support."
                    });
                });
    }

    componentWillUnmount() {
        clearInterval(this.timerID);
    }

    render() {

        return (
            <div>
                <div style={{fontSize:'5em'}}>
                    {this.state.count}
                </div>
                <div>
                    <Clock></Clock>
                </div>
            </div>
        );
    }
}

export default TweetCountStat;



