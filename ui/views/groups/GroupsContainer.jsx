import React from 'react';
import Groups from './Groups';

// State
import { connect } from 'react-redux'
import { generateGetGroups, generateModifyGroup, generateAddGroup, generateDeleteGroup,
    resetModifyErrors, resetAddingErrors } from '../../reducers/groups/actions';

export class GroupsContainer extends React.Component {
    state = {
        selectedRow: null,
        selectedRowId: null,
        showAddForm: false,
    }

    constructor(props) {
        super(props);

        const { dispatch } = props;
        this.getGroups = generateGetGroups(dispatch);
        this.modifyGroup = generateModifyGroup(dispatch);
        this.addGroup = generateAddGroup(dispatch);
        this.deleteGroup = generateDeleteGroup(dispatch);
        this.resetModifyErrors = () => resetModifyErrors(dispatch);
        this.resetAddingErrors = () => resetAddingErrors(dispatch);
    }

    componentDidMount() {
        this.getGroups();
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
        this.modifyGroup(data);
    }

    onDeleteButtonClick(data) {
        this.deleteGroup(data);

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
        this.addGroup(data, href);
    }

    render() {
        const { items, links, queries, template,
            isFetching, hasFetched, fetchingError, fetchingErrorMessage,
            isUpdating, hasUpdated, updateError, updateErrorMessage,
            isAdding, hasAdded, addError, addErrorMessage } = this.props;
        const { selectedRow, selectedRowId, showAddForm } = this.state;
        const { getGroups, onRowSelect,
            onSaveButtonClick, onDeleteButtonClick, onTableAddButtonClick, onFormAddButtonClick } = this;

        return (
            <Groups { ...{
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
                getGroups,
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
    return state.groups;
}

export default connect(mapStateToProps)(GroupsContainer);