import React from 'react';
import Devices from './Devices';

// State
import { connect } from 'react-redux'
import { generateGetDevices, generateModifyDevice, resetModifyErrors } from '../../reducers/devices/actions';

export class DevicesContainer extends React.Component {
    state = {
        selectedRow: null,
        selectedRowId: null,
        showAddForm: false,
    }

    constructor(props) {
        super(props);

        const { dispatch } = props;
        this.getDevices = generateGetDevices(dispatch);
        this.modifyDevice = generateModifyDevice(dispatch);
        this.resetModifyErrors = () => resetModifyErrors(dispatch);
    }

    componentDidMount() {
        this.getDevices();
    }

    onRowSelect(selectedRow, selectedRowId) {
        this.resetModifyErrors();
        this.setState({
            selectedRow,
            selectedRowId,
            showAddForm: false
        });
    }

    onSaveButtonClick(data) {
        this.modifyDevice(data);
    }

    onTableAddButtonClick() {
        this.setState({
            showAddForm: true,
        });
    }

    onFormAddButtonClick(data, href) {
        console.log(data, href);
    }

    render() {
        const { items, links, queries, template,
            isFetching, hasFetched, fetchingError, fetchingErrorMessage,
            isUpdating, hasUpdated, updateError, updateErrorMessage } = this.props;
        const { selectedRow, selectedRowId, showAddForm } = this.state;
        const { getDevices, onRowSelect, onSaveButtonClick, onTableAddButtonClick, onFormAddButtonClick } = this;

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
                onSaveButtonClick: onSaveButtonClick.bind(this),
                onTableAddButtonClick: onTableAddButtonClick.bind(this),
                onFormAddButtonClick: onFormAddButtonClick.bind(this),
                showAddForm
            }} />
        );
    }
}

function mapStateToProps(state) {
    return state.devices;
}

export default connect(mapStateToProps)(DevicesContainer);