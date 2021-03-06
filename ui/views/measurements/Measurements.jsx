import React from 'react';
import { Row, Col } from 'reactstrap';
import LoadingIndicator from 'react-loading-indicator';

import DataTable from '../../components/DataTable/DataTableContainer';
import ErrorAlert from '../../components/ErrorAlert';

export default class Measurements extends React.Component {

    render() {
        return (
            <Row>
                <Col md={6} lg={4}>
                    { this.renderTable() }
                </Col>

                <Col md={6} lg={8}>
                    <pre>
                        {JSON.stringify(this.props.selectedRow, null, 2)}
                    </pre>
                </Col>
            </Row>
        );
    }

    renderTable() {
        const { items, links, queries, template,
            getMeasurements, onRowSelect,
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
                        search: getMeasurements
                    }} />
                </div>
            );
        }

        return null;
    }
}
