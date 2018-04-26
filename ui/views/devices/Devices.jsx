import React from 'react';
import { Container, Row, Col } from 'reactstrap';
import './devices.scss';

import DataTable from '../../components/DataTable/DataTableContainer';
import DataForm from '../../components/DataForm';
import ErrorAlert from '../../components/ErrorAlert';

export default class Devices extends React.Component {

    render() {
        const { selectedRow, selectedRowId } = this.props;

        return (
            <Container fluid>
                <Row>
                    <Col lg={12} xl={6}>
                        { this.renderTable() }
                    </Col>

                    <Col md={12} lg={6}>
                        { selectedRow && <DataForm dataRow={selectedRow} index={selectedRowId} /> }
                    </Col>

                </Row>
            </Container>
        );
    }

    renderTable() {
        const { items, links, queries, template, getDevices, onRowSelect,
            isFetching, hasFetched, error, errorMessage } = this.props;

        if(isFetching) {
            return <p>TODO loading indicator</p>;
        } else if (error) {
            const header = errorMessage.split("::").shift();
            const message = errorMessage.split("::").pop();

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
