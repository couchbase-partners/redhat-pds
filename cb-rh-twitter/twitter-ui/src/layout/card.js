import React, { Component } from 'react';
import ResizeAware from 'react-resize-aware';

class Card extends Component {
    render() {

        let title;
        if (this.props.title) {
            title = <div className="card-pf-heading">
                <h2 className="card-pf-title">{this.props.title}</h2>
            </div>
        }

        return (
            <ResizeAware>
                <div className="card-pf">
                    {title}
                    <div className="card-pf-body">
                        {this.props.children}                            
                    </div>
                </div>
            </ResizeAware>
        );
    }
}
export default Card;
