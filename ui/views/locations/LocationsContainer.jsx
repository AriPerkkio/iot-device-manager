import React from 'react';

import Locations from './Locations';

// State
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux';
import { generateGetLocations } from '../../reducers/locations/actions';

export class LocationsContainer extends React.Component {

    constructor(props) {
        super(props);

        const { dispatch } = props;

        this.getLocations = generateGetLocations(dispatch);
    }

    componentDidMount() {
        this.getLocations();
    }

    render() {
        const { items, links, queries, template, isFetching, hasFetched, error, errorMessage } = this.props;
        const { getLocations } = this;

        return (
            <Locations { ...{
                items,
                links,
                queries,
                template,
                isFetching,
                hasFetched,
                error,
                errorMessage,
                getLocations
            }} />
        );
    }
}

function mapStateToProps(state) {
    return  state.locations;
}

export default connect(mapStateToProps)(LocationsContainer);