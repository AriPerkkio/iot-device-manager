import React from 'react';
import { Row, Col } from 'reactstrap';
import LoadingIndicator from 'react-loading-indicator';

import DataTable from '../../components/DataTable/DataTableContainer';
import ErrorAlert from '../../components/ErrorAlert';
import Maps from '../../components/Maps/MapsContainer';

export default class Locations extends React.Component {

    render() {
        return (
            <Row className="locations-row">
                <Col md={6} lg={4}>
                    { this.renderTable() }
                </Col>

                <Col md={6} lg={8} className="map-column">
                    { this.renderMap() }
                </Col>
            </Row>
        );
    }

    renderTable() {
        const { items, links, queries, template,
            getLocations, onRowSelect,
            isFetching, hasFetched, fetchingError, fetchingErrorMessage } = this.props;

        if(isFetching) {
            return (
                <LoadingIndicator
                    segmentWidth={10}
                    segmentLength={10} />
            );
        } else {
            return (
                <div>
                    {fetchingError && <ErrorAlert errorMessage={fetchingErrorMessage} /> }
                    <DataTable { ...{
                        items,
                        links,
                        queries,
                        template,
                        onRowSelect,
                        search: getLocations
                    }} />
                </div>
            );
        }

        return null;
    }

    renderMap() {
        const { selectedRow, items } = this.props;

        return (
            <Maps { ...{
                items,
                selectedRow
            }} />
        );
    }
}
