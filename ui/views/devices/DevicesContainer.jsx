import React from 'react';

import Devices from './Devices';

// State
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux';
import { generateGetDevices } from '../../reducers/devices/actions';

export class DevicesContainer extends React.Component {

    constructor(props) {
        super(props);

        const { dispatch } = props;

        this.getDevices = generateGetDevices(dispatch);
    }

    componentDidMount() {
        this.getDevices();
    }

    render() {
        const { items, links, queries, template, isFetching, hasFetched, error, errorMessage } = this.props;
        const { getDevices } = this;

        return (
            <Devices { ...{
                items,
                links,
                queries,
                template,
                isFetching,
                hasFetched,
                error,
                errorMessage,
                getDevices
            }} />
        );
    }
}

function mapStateToProps(state) {
    return  state.devices;
}

export default connect(mapStateToProps)(DevicesContainer);