import React from 'react';
import './devices.scss';

// State
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux';
import { generateGetDevices } from '../../reducers/devices/actions';

export class Devices extends React.Component {

    constructor(props) {
        super(props);

        const { dispatch } = props;

        this.getDevices = generateGetDevices(dispatch);
    }

    componentDidMount() {
        this.getDevices();
    }

    render() {
        return (
            <div id="devices">
                <h1>Devices view</h1>
                <pre>
                    {JSON.stringify(this.props, null, 4)}
                </pre>
            </div>
        );
    }
}

function mapStateToProps(state) {
    return state.devices;
}

export default connect(mapStateToProps)(Devices);