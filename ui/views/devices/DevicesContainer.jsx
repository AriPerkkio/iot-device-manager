import React from 'react';

import Devices from './Devices';

// State
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux';
import { generateGetDevices } from '../../reducers/devices/actions';

export class DevicesContainer extends React.Component {
    state = {
        selectedRow: null,
        selectedRowId: null
    }

    constructor(props) {
        super(props);

        const { dispatch } = props;

        this.getDevices = generateGetDevices(dispatch);
    }

    componentDidMount() {
        this.getDevices();
    }

    onRowSelect(selectedRow, selectedRowId) {
        this.setState({
            selectedRow,
            selectedRowId
        });
    }

    render() {
        const { items, links, queries, template, isFetching, hasFetched, error, errorMessage } = this.props;
        const { selectedRow, selectedRowId } = this.state;
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
                selectedRow,
                selectedRowId
            }} />
        );
    }
}

function mapStateToProps(state) {
    return  state.devices;
}

export default connect(mapStateToProps)(DevicesContainer);