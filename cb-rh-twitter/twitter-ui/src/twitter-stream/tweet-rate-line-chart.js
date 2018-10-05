
import React, { Component } from 'react';
import C3Chart from 'react-c3js';
import 'c3/c3.css';


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


class TweetRateLineChart extends Component {
    constructor(props) {
        super(props);
        this.state = {
            columns: null
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
        fetch('http://localhost:8080/tweetrate')
        .then(res => res.json())
                .then(
                    (result) => {
                        var columns = [];
                        var x = ['x'];
                        var y = ['tweets / minute']
                        Object.keys(result).forEach(function(key) {
                            var date = new Date((key*60)*1000)
                            x.push(date)

                            var count = result[key];
                            y.push(count);
                        });

                        columns.push(x);
                        columns.push(y);
                        this.setState({
                            columns: columns,
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

        let data;
        let axis;
        let tooltip;
        let chart;

        let size;

        size = {
            height: 130,
            width: 220
        }

        if (this.state.columns) {

            if (this.state.columns.length <= 0) {
                return (<div></div>);
            }

            data = {
                x: 'x',
        //        xFormat: '%Y%m%d', // 'xFormat' can be used as custom format of 'x'
                /*columns: [
                    ['x', new Date((25644499*60)*1000), new Date((25644496*60)*1000), new Date((25644500*60)*1000), new Date((25644501*60)*1000), new Date((25644498*60)*1000)],
        //            ['x', '20130101', '20130102', '20130103', '20130104', '20130105', '20130106'],
                    ['tweets / minute', 200, 100, 400, 150, 250]                
                ]*/
                columns: this.state.columns
            };
            axis = {
                x: {
                    type: 'timeseries',
                    tick: {
                        //format: '%m-%d %H:%M'
                        format: '%H:%M'
                    }
                }
            }
            tooltip = {
                show: false,                
            }

            chart = <C3Chart data={data} axis={axis} size={size} tooltip={tooltip} />
        }

        return (
            <div>
                {chart}
            </div>
        );
    }
}

export default TweetRateLineChart;



