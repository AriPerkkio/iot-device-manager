import React from 'react';
import './devices.scss';

// State
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux';
import { getDevices } from '../../reducers/devices/actions';

export class Devices extends React.Component {

    componentDidMount() {
        getDevices()(this.props.dispatch);
    }

    render() {
        return (
            <div id="devices">
                <h1>Devices view</h1>
            </div>
        );
    }
}

function mapStateToProps(state) {
    return {
        temp: state
    }
}

export default connect(mapStateToProps)(Devices);