import React from 'react';

import Measurements from './Measurements';

// State
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux';
import { generateGetMeasurements } from '../../reducers/measurements/actions';

export class MeasurementsContainer extends React.Component {

    constructor(props) {
        super(props);

        const { dispatch } = props;

        this.getMeasurements = generateGetMeasurements(dispatch);
    }

    componentDidMount() {
        this.getMeasurements();
    }

    render() {
        const { items, links, queries, template, isFetching, hasFetched, fetchingError, fetchingErrorMessage } = this.props;
        const { getMeasurements } = this;

        return (
            <Measurements { ...{
                items,
                links,
                queries,
                template,
                isFetching,
                hasFetched,
                fetchingError,
                fetchingErrorMessage,
                getMeasurements
            }} />
        );
    }
}

function mapStateToProps(state) {
    return  state.measurements;
}

export default connect(mapStateToProps)(MeasurementsContainer);