
import React, { Component } from 'react';
import Row from '../layout/row';
import Col2 from '../layout/col2';
import Col10 from '../layout/col10';


class TweetStream extends Component {
    constructor(props) {
        super(props);
        this.state = {
            date : new Date(),
            tweets : [],
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
        fetch(this.props.apiBase + '/tweets')
        .then(res => res.json())
                .then(
                    (result) => {
                        this.setState({
                            date: new Date(),
                            tweets: result,
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

        let tweets;
        if (this.state.tweets) {
           tweets = this.state.tweets.map((tweet) =>
           <Row key={tweet.id}>
               <Col2>
                <img alt="profile" src={tweet.profileImg} />
               </Col2>
                <Col10>
                    <strong>{tweet.name}</strong> <br/>
                    {tweet.text}
               </Col10>
           </Row>
           );
        }

        return (
            <div>
                {tweets}
            </div>
        );
    }
}

export default TweetStream;



