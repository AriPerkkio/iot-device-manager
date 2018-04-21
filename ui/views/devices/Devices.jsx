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
        const filters = {
            name: "device-one",
            configurationId: 2,
            deviceGroupId: 3
        };

        this.getDevices(filters);
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