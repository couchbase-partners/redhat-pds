import React, { Component } from 'react';

class Col2 extends Component {
    render() {
        return (
            <div className="col-xs-6 col-sm-4 col-md-2">{this.props.children}</div>
        );
    }
}
export default Col2;
