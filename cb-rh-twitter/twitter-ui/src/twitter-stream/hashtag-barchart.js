
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


class HashTagBarChart extends Component {
    constructor(props) {
        super(props);
        this.state = {
            date : new Date(),
            counts : null,
            categories: null,
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
        fetch(this.props.apiBase + '/hashtags')
        .then(res => res.json())
                .then(
                    (result) => {
                        var counts = ['count'];
                        var categories = [];
                        Object.keys(result).forEach(function(key) {
                            categories.push(key);
                            counts.push(result[key]);
                        });
                        this.setState({
                            counts: counts,
                            categories: categories,
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
        let chart;

        if (this.state.counts && this.state.categories) {

            if (this.state.counts.length <= 0 || this.state.categories.length <= 0) {
                return (<div></div>);
            }

            data = {
                columns: [
                this.state.counts
                ],
                type: 'bar'
            };
            axis = {
                rotated: true,
                x: {
                    type: 'category', // this needed to load string x value
                    //categories: ['tag1', 'tag2', 'tag3', 'tag4', 'tag5', 'tag6', 'tag7', 'tag8', 'tag9', 'tag10']   
                    categories: this.state.categories  
                }
            }
            chart = <C3Chart data={data} axis={axis} />
        } else {
            chart = <div></div>
        }
        return (
            <div>
                {chart}
            </div>
        );
    }
}

export default HashTagBarChart;



