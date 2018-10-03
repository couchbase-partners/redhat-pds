import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import Clock from './twitter-stream/ticker.js'
import Row from './layout/row.js'
import Col3 from './layout/col3.js'

class App extends Component {
  render() {
    return (
      <div className="container-fluid container-cards-pf">
        <Row>
          <Clock></Clock>
        </Row>
        <Row>
          <Col3>first column</Col3>
          <Col3>column</Col3>
          <Col3>another column</Col3>
          <Col3>last column</Col3>
        </Row>
      </div>
    );
  }
}

export default App;
