import React from 'react';
import { Row, Col } from 'reactstrap';
import LoadingIndicator from 'react-loading-indicator';

import DataTable from '../../components/DataTable/DataTableContainer';
import DataForm from '../../components/DataForm/DataFormContainer';
import ErrorAlert from '../../components/ErrorAlert';

export default class Devices extends React.Component {

    render() {
        return (
            <Row>
                <Col md={12} lg={8}>
                    { this.renderTable() }
                </Col>

                <Col md={12} lg={4}>
                    { this.renderForm() }
                </Col>
            </Row>
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
        } else {
            return (
                <div>
                    {fetchingError && <ErrorAlert errorMessage={fetchingErrorMessage} /> }
                    <DataTable { ...{
                        items,
                        links,
                        queries,
                        template,
                        search: getDevices,
                        onRowSelect,
                        size: "sm",
                        onAddButtonClick: onTableAddButtonClick,
                        addButtonText: "Add new device",
                    }} />
                </div>
            );
        }

        return null;
    }

    renderForm() {
        const { template, selectedRow, selectedRowId, showAddForm,
            onSaveButtonClick, onDeleteButtonClick, onFormAddButtonClick,
            isUpdating, hasUpdated, updateError, updateErrorMessage,
            isAdding, hasAdded, addError, addErrorMessage } = this.props;

        return (selectedRow || showAddForm) &&
            <DataForm { ... {
                dataRow: selectedRow,
                index: selectedRowId,
                onSaveButtonClick,
                isLoading: (isUpdating || isAdding),
                error: (updateError || addError),
                errorMessage: updateErrorMessage || addErrorMessage,
                template,
                showAddForm,
                onAddButtonClick: onFormAddButtonClick,
                onDeleteButtonClick,
                addButtonText: "Add device"
            }} />;
    }
}
