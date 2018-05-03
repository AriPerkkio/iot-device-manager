import React from 'react';

import Locations from './Locations';

// State
import { connect } from 'react-redux'
import { generateGetLocations, generateDeleteLocation } from '../../reducers/locations/actions';

export class LocationsContainer extends React.Component {
    state = {
        selectedRow: null,
        selectedRowId: null,
        showMap: false,
    }

    constructor(props) {
        super(props);

        const { dispatch } = props;

        this.getLocations = generateGetLocations(dispatch);
        this.deleteLocation = generateDeleteLocation(dispatch);
    }

    componentDidMount() {
        this.getLocations();
    }

    onRowSelect(selectedRow, selectedRowId) {
        this.setState({
            selectedRow,
            selectedRowId,
            showMap: true
        });
    }


    onDeleteButtonClick(data) {
        this.deleteLocation(data);

        this.setState({
            selectedRow: null,
            selectedRowId: null,
        });
    }

    render() {
        const { items, links, queries, template, isFetching, hasFetched, fetchingError, fetchingErrorMessage } = this.props;
        const { selectedRow, selectedRowId, showMap } = this.state;
        const { getLocations, onDeleteButtonClick, onRowSelect } = this;

        return (
            <Locations { ...{
                items,
                links,
                queries,
                template,
                isFetching,
                hasFetched,
                fetchingError,
                fetchingErrorMessage,
                selectedRow,
                selectedRowId,
                showMap,
                getLocations,
                onDeleteButtonClick: onDeleteButtonClick.bind(this),
                onRowSelect: onRowSelect.bind(this),
            }} />
        );
    }
}

function mapStateToProps(state) {
    return  state.locations;
}

export default connect(mapStateToProps)(LocationsContainer);