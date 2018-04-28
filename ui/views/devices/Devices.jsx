import React from 'react';
import { Container, Row, Col } from 'reactstrap';
import LoadingIndicator from 'react-loading-indicator';
import './devices.scss';

import DataTable from '../../components/DataTable/DataTableContainer';
import DataForm from '../../components/DataForm';
import ErrorAlert from '../../components/ErrorAlert';

export default class Devices extends React.Component {

    render() {
        return (
            <Container fluid>
                <Row>
                    <Col md={12} lg={8}>
                        { this.renderTable() }
                    </Col>

                    <Col md={12} lg={4}>
                        { this.renderForm() }
                    </Col>
                </Row>
            </Container>
        );
    }

    renderTable() {
        const { items, links, queries, template, getDevices, onRowSelect,
            isFetching, hasFetched, fetchingError, fetchingErrorMessage } = this.props;

        if(isFetching) {
            return (
                <LoadingIndicator
                    segmentWidth={10}
                    segmentLength={10} />
            );
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

    renderForm() {
        const { selectedRow, selectedRowId, onSaveButtonClick,
            isUpdating, hasUpdated, updateError, updateErrorMessage } = this.props;

        return selectedRow &&
            <DataForm { ... {
                dataRow: selectedRow,
                index: selectedRowId,
                onSaveButtonClick,
                isLoading: isUpdating,
                error: updateError,
                errorMessage: updateErrorMessage
            }} />;
    }
}
