import React from 'react';

import Devices from './Devices';

// State
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux';
import { generateGetDevices, generateModifyDevice } from '../../reducers/devices/actions';

export class DevicesContainer extends React.Component {
    state = {
        selectedRow: null,
        selectedRowId: null
    }

    constructor(props) {
        super(props);

        const { dispatch } = props;

        this.getDevices = generateGetDevices(dispatch);
        this.modifyDevice = generateModifyDevice(dispatch);
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

    onSaveButtonClick(data) {
        this.modifyDevice(data);
    }

    render() {
        const { items, links, queries, template,
            isFetching, hasFetched, fetchingError, fetchingErrorMessage,
            isUpdating, hasUpdated, updateError, updateErrorMessage } = this.props;
        const { selectedRow, selectedRowId } = this.state;
        const { getDevices, onRowSelect, onSaveButtonClick } = this;

        return (
            <Devices { ...{
                items,
                links,
                queries,
                template,
                isFetching,
                hasFetched,
                fetchingError,
                fetchingErrorMessage,
                isUpdating,
                hasUpdated,
                updateError,
                updateErrorMessage,
                getDevices,
                onRowSelect: onRowSelect.bind(this),
                selectedRow,
                selectedRowId,
                onSaveButtonClick: onSaveButtonClick.bind(this)
            }} />
        );
    }
}

function mapStateToProps(state) {
    return  state.devices;
}

export default connect(mapStateToProps)(DevicesContainer);