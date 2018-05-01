import React from 'react';
import { Container, Row, Col } from 'reactstrap';
import LoadingIndicator from 'react-loading-indicator';

import DataTable from '../../components/DataTable/DataTableContainer';
import DataForm from '../../components/DataForm/DataFormContainer';
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
        const { items, links, queries, template,
            getDevices, onRowSelect, onTableAddButtonClick,
            isFetching, hasFetched, fetchingError, fetchingErrorMessage } = this.props;

        if(isFetching) {
            return (
                <LoadingIndicator
                    segmentWidth={10}
                    segmentLength={10} />
            );
        } else if (fetchingError) {
            return (
                <ErrorAlert { ...{
                    header: fetchingErrorMessage.split("::").shift(),
                    message: fetchingErrorMessage.split("::").pop()
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
                    size: "sm",
                    onAddButtonClick: onTableAddButtonClick,
                }} />
            );
        }

        return null;
    }

    renderForm() {
        const { template, selectedRow, selectedRowId, onSaveButtonClick, showAddForm, onFormAddButtonClick,
            isUpdating, hasUpdated, updateError, updateErrorMessage } = this.props;

        return (selectedRow || showAddForm) &&
            <DataForm { ... {
                dataRow: selectedRow,
                index: selectedRowId,
                onSaveButtonClick,
                isLoading: isUpdating,
                error: updateError,
                errorMessage: updateErrorMessage,
                template,
                showAddForm,
                onAddButtonClick: onFormAddButtonClick,
            }} />;
    }
}
