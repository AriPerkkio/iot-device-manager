import React from 'react';
import Configurations from './Configurations';

// State
import { connect } from 'react-redux'
import { generateGetConfigurations, generateModifyConfiguration, generateAddConfiguration, generateDeleteConfiguration,
    resetModifyErrors, resetAddingErrors } from '../../reducers/configurations/actions';

export class ConfigurationsContainer extends React.Component {
    state = {
        selectedRow: null,
        selectedRowId: null,
        showAddForm: false,
    }

    constructor(props) {
        super(props);

        const { dispatch } = props;
        this.getConfigurations = generateGetConfigurations(dispatch);
        this.modifyConfiguration = generateModifyConfiguration(dispatch);
        this.addConfiguration = generateAddConfiguration(dispatch);
        this.deleteConfiguration = generateDeleteConfiguration(dispatch);
        this.resetModifyErrors = () => resetModifyErrors(dispatch);
        this.resetAddingErrors = () => resetAddingErrors(dispatch);
    }

    componentDidMount() {
        this.getConfigurations();
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
        this.modifyConfiguration(data);
    }

    onDeleteButtonClick(data) {
        this.deleteConfiguration(data);

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
        this.addConfiguration(data, href);
    }

    render() {
        const { items, links, queries, template,
            isFetching, hasFetched, fetchingError, fetchingErrorMessage,
            isUpdating, hasUpdated, updateError, updateErrorMessage,
            isAdding, hasAdded, addError, addErrorMessage } = this.props;
        const { selectedRow, selectedRowId, showAddForm } = this.state;
        const { getConfigurations, onRowSelect,
            onSaveButtonClick, onDeleteButtonClick, onTableAddButtonClick, onFormAddButtonClick } = this;

        return (
            <Configurations { ...{
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
                getConfigurations,
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
    return state.configurations;
}

export default connect(mapStateToProps)(ConfigurationsContainer);