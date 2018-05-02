import React from 'react';
import Types from './Types';

// State
import { connect } from 'react-redux'
import { generateGetTypes, generateModifyType, generateAddType, generateDeleteType,
    resetModifyErrors, resetAddingErrors } from '../../reducers/types/actions';

export class TypesContainer extends React.Component {
    state = {
        selectedRow: null,
        selectedRowId: null,
        showAddForm: false,
    }

    constructor(props) {
        super(props);

        const { dispatch } = props;
        this.getTypes = generateGetTypes(dispatch);
        this.modifyType = generateModifyType(dispatch);
        this.addType = generateAddType(dispatch);
        this.deleteType = generateDeleteType(dispatch);
        this.resetModifyErrors = () => resetModifyErrors(dispatch);
        this.resetAddingErrors = () => resetAddingErrors(dispatch);
    }

    componentDidMount() {
        this.getTypes();
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
        this.modifyType(data);
    }

    onDeleteButtonClick(data) {
        this.deleteType(data);

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
        this.addType(data, href);
    }

    render() {
        const { items, links, queries, template,
            isFetching, hasFetched, fetchingError, fetchingErrorMessage,
            isUpdating, hasUpdated, updateError, updateErrorMessage,
            isAdding, hasAdded, addError, addErrorMessage } = this.props;
        const { selectedRow, selectedRowId, showAddForm } = this.state;
        const { getTypes, onRowSelect,
            onSaveButtonClick, onDeleteButtonClick, onTableAddButtonClick, onFormAddButtonClick } = this;

        return (
            <Types { ...{
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
                getTypes,
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
    return state.types;
}

export default connect(mapStateToProps)(TypesContainer);