import React from 'react';

import Types from './Types';

// State
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux';
import { generateGetTypes } from '../../reducers/types/actions';

export class TypesContainer extends React.Component {

    constructor(props) {
        super(props);

        const { dispatch } = props;

        this.getTypes = generateGetTypes(dispatch);
    }

    componentDidMount() {
        this.getTypes();
    }

    render() {
        const { items, links, queries, template, isFetching, hasFetched, error, errorMessage } = this.props;
        const { getTypes } = this;

        return (
            <Types { ...{
                items,
                links,
                queries,
                template,
                isFetching,
                hasFetched,
                error,
                errorMessage,
                getTypes
            }} />
        );
    }
}

function mapStateToProps(state) {
    return  state.types;
}

export default connect(mapStateToProps)(TypesContainer);