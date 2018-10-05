import React, { Component } from 'react';

class Col10 extends Component {
    render() {
        return (
            <div className="col-xs-10 col-sm-10 col-md-10">{this.props.children}</div>
        );
    }
}
export default Col10;
