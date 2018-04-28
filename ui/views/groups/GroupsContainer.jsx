import React from 'react';
import Groups from './Groups';

// State
import { connect } from 'react-redux'
import { generateGetGroups } from '../../reducers/groups/actions';

export class GroupsContainer extends React.Component {

    constructor(props) {
        super(props);

        const { dispatch } = props;
        this.getGroups = generateGetGroups(dispatch);
    }

    componentDidMount() {
        this.getGroups();
    }

    render() {
        const { items, links, queries, template, isFetching, hasFetched, fetchingError, fetchingErrorMessage } = this.props;
        const { getGroups } = this;

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
                getGroups
            }} />
        );
    }
}

function mapStateToProps(state) {
    return state.groups;
}

export default connect(mapStateToProps)(GroupsContainer);