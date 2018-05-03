import React from 'react';
import { Container, Row, Col } from 'reactstrap';
import LoadingIndicator from 'react-loading-indicator';

import DataTable from '../../components/DataTable/DataTableContainer';
import ErrorAlert from '../../components/ErrorAlert';

export default class Locations extends React.Component {

    render() {
        return (
            <Container fluid>
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
            </Container>
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
}
