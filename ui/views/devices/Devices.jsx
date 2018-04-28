import React from 'react';
import { Container, Row, Col } from 'reactstrap';
import './devices.scss';

import DataTable from '../../components/DataTable/DataTableContainer';
import DataForm from '../../components/DataForm';
import ErrorAlert from '../../components/ErrorAlert';

export default class Devices extends React.Component {

    render() {
        const { selectedRow, selectedRowId, onSaveButtonClick } = this.props;

        return (
            <Container fluid>
                <Row>
                    <Col md={12} lg={8}>
                        { this.renderTable() }
                    </Col>

                    <Col md={12} lg={4}>
                        { selectedRow &&
                        <DataForm
                            dataRow={selectedRow}
                            index={selectedRowId}
                            onSaveButtonClick={onSaveButtonClick} /> }
                    </Col>
                </Row>
            </Container>
        );
    }

    renderTable() {
        const { items, links, queries, template, getDevices, onRowSelect,
            isFetching, hasFetched, fetchingError, fetchingErrorMessage } = this.props;

        if(isFetching) {
            return <p>TODO loading indicator</p>;
        } else if (fetchingError) {
            const header = fetchingErrorMessage.split("::").shift();
            const message = fetchingErrorMessage.split("::").pop();

            return (
                <ErrorAlert { ...{
                    header,
                    message
                }} />
            );
        } else if(hasFetched) {
            return (
                <DataTable { ...{
                    items,
                    links,
                    queries,
                    template,
                    search: getDevices,
                    onRowSelect,
                    size: "sm"
                }} />
            );
        }

        return null;
    }
}
