import React from 'react';

import Devices from './Devices';

// State
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux';
import { generateGetDevices } from '../../reducers/devices/actions';

export class DevicesContainer extends React.Component {
    state = {
        selectedRow: null
    }

    constructor(props) {
        super(props);

        const { dispatch } = props;

        this.getDevices = generateGetDevices(dispatch);
    }

    componentDidMount() {
        this.getDevices();
    }

    onRowSelect(selectedRow) {
        this.setState({
            selectedRow
        });
    }

    render() {
        const { items, links, queries, template, isFetching, hasFetched, error, errorMessage } = this.props;
        const { selectedRow } = this.state;
        const { getDevices, onRowSelect } = this;

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
                getDevices,
                onRowSelect: onRowSelect.bind(this),
                selectedRow
            }} />
        );
    }
}

function mapStateToProps(state) {
    return  state.devices;
}

export default connect(mapStateToProps)(DevicesContainer);