import React, { Component } from 'react';

class Row extends Component {
    render() {
        return (
            <div className="row row-cards-pf">{this.props.children}</div>
        );
    }
}
export default Row;
