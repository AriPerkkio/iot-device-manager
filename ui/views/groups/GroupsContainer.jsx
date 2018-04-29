import React from 'react';
import Groups from './Groups';

// State
import { connect } from 'react-redux'
import { generateGetGroups, generateModifyGroups } from '../../reducers/groups/actions';

export class GroupsContainer extends React.Component {
    state = {
        selectedRow: null,
        selectedRowId: null
    }

    constructor(props) {
        super(props);

        const { dispatch } = props;
        this.getGroups = generateGetGroups(dispatch);
        this.modifyGroups = generateModifyGroups(dispatch);
    }

    componentDidMount() {
        this.getGroups();
    }

    onRowSelect(selectedRow, selectedRowId) {
        this.setState({
            selectedRow,
            selectedRowId
        });
    }

    onSaveButtonClick(data) {
        this.modifyGroups(data);
    }

    render() {
        const { items, links, queries, template,
            isFetching, hasFetched, fetchingError, fetchingErrorMessage,
            isUpdating, hasUpdated, updateError, updateErrorMessage } = this.props;
        const { selectedRow, selectedRowId } = this.state;
        const { getGroups, onRowSelect, onSaveButtonClick } = this;

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
                onSaveButtonClick: onSaveButtonClick.bind(this)
            }} />
        );
    }
}

function mapStateToProps(state) {
    return state.groups;
}

export default connect(mapStateToProps)(GroupsContainer);