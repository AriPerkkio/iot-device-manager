import React from 'react';
import Devices from './Devices';

// State
import { connect } from 'react-redux'
import { generateGetDevices, generateModifyDevice, generateAddDevice, generateDeleteDevice,
    resetModifyErrors, resetAddingErrors } from '../../reducers/devices/actions';

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
        this.addDevice = generateAddDevice(dispatch);
        this.deleteDevice = generateDeleteDevice(dispatch);
        this.resetModifyErrors = () => resetModifyErrors(dispatch);
        this.resetAddingErrors = () => resetAddingErrors(dispatch);
    }

    componentDidMount() {
        this.getDevices();
    }

    componentWillReceiveProps(newProps) {
        const { isAdding: newIsAdding, addError } = newProps;
        const { isAdding: prevIsAdding } = this.props;

        if(addError === false && prevIsAdding === true && newIsAdding === false) {
            this.setState({
                showAddForm: false
            })
        }
    }

    onRowSelect(selectedRow, selectedRowId) {
        this.resetModifyErrors();
        this.resetAddingErrors();

        this.setState({
            selectedRow,
            selectedRowId,
            showAddForm: false
        });
    }

    onSaveButtonClick(data) {
        this.modifyDevice(data);
    }

    onDeleteButtonClick(data) {
        this.deleteDevice(data);

        this.setState({
            selectedRow: null,
            selectedRowId: null,
        });
    }

    onTableAddButtonClick() {
        this.setState({
            showAddForm: true,
            selectedRow: null,
            selectedRowId: null,
        });
    }

    onFormAddButtonClick(data, href) {
        this.addDevice(data, href);
    }

    render() {
        const { items, links, queries, template,
            isFetching, hasFetched, fetchingError, fetchingErrorMessage,
            isUpdating, hasUpdated, updateError, updateErrorMessage,
            isAdding, hasAdded, addError, addErrorMessage } = this.props;
        const { selectedRow, selectedRowId, showAddForm } = this.state;
        const { getDevices, onRowSelect,
            onSaveButtonClick, onDeleteButtonClick, onTableAddButtonClick, onFormAddButtonClick } = this;

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
                onDeleteButtonClick: onDeleteButtonClick.bind(this),
                onTableAddButtonClick: onTableAddButtonClick.bind(this),
                onFormAddButtonClick: onFormAddButtonClick.bind(this),
                showAddForm,
                isAdding,
                hasAdded,
                addError,
                addErrorMessage,
            }} />
        );
    }
}

function mapStateToProps(state) {
    return state.devices;
}

export default connect(mapStateToProps)(DevicesContainer);