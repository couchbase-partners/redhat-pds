import React, { Component } from 'react';

class Col4 extends Component {
    render() {
        return (
            <div className="col-xs-12 col-sm-6 col-md-4">{this.props.children}</div>
        );
    }
}
export default Col4;
