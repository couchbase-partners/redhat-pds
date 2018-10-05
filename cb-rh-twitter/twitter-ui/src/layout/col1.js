import React, { Component } from 'react';

class Col1 extends Component {
    render() {
        return (
            <div className="col-xs-3 col-sm-2 col-md-1">{this.props.children}</div>
        );
    }
}
export default Col1;
