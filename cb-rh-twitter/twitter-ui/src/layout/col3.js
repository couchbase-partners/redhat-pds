import React, { Component } from 'react';

class Col3 extends Component {
    render() {
        return (
            <div className="col-xs-12 col-sm-6 col-md-3">{this.props.children}</div>
        );
    }
}
export default Col3;
