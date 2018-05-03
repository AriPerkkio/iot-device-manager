import React from 'react';

import Measurements from './Measurements';

// State
import { connect } from 'react-redux'
import { generateGetMeasurements, generateDeleteMeasurement } from '../../reducers/measurements/actions';

export class MeasurementsContainer extends React.Component {
    state = {
        selectedRow: null,
        selectedRowId: null,
        showContent: false,
    }

    constructor(props) {
        super(props);

        const { dispatch } = props;

        this.getMeasurements = generateGetMeasurements(dispatch);
        this.deleteMeasurement = generateDeleteMeasurement(dispatch);
    }

    componentDidMount() {
        this.getMeasurements();
    }

    onRowSelect(selectedRow, selectedRowId) {
        this.setState({
            selectedRow,
            selectedRowId,
            showContent: true
        });
    }

    onDeleteButtonClick(data) {
        this.deleteMeasurement(data);

        this.setState({
            selectedRow: null,
            selectedRowId: null,
        });
    }

    render() {
        const { items, links, queries, template, isFetching, hasFetched, fetchingError, fetchingErrorMessage } = this.props;
        const { selectedRow, selectedRowId, showContent } = this.state;
        const { getMeasurements, onDeleteButtonClick, onRowSelect } = this;

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
                selectedRow,
                selectedRowId,
                showContent,
                getMeasurements,
                onDeleteButtonClick: onDeleteButtonClick.bind(this),
                onRowSelect: onRowSelect.bind(this),
            }} />
        );
    }
}

function mapStateToProps(state) {
    return  state.measurements;
}

export default connect(mapStateToProps)(MeasurementsContainer);