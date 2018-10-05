import React, { Component } from 'react';

class Col12 extends Component {
    render() {
        return (
            <div className="col-xs-12 col-sm-12 col-md-12">{this.props.children}</div>
        );
    }
}
export default Col12;
