import React from 'react';
import { Row, Col } from 'reactstrap';
import LoadingIndicator from 'react-loading-indicator';

import DataTable from '../../components/DataTable/DataTableContainer';
import DataForm from '../../components/DataForm/DataFormContainer';
import ErrorAlert from '../../components/ErrorAlert';

export default class Groups extends React.Component {

    render() {
        return (
            <Row>
                <Col sm={12} md={8}>
                    { this.renderTable() }
                </Col>

                <Col sm={12} md={4}>
                    { this.renderForm() }
                </Col>
            </Row>
        );
    }

    renderTable() {
        const { items, links, queries, template,
            getGroups, onRowSelect, onTableAddButtonClick,
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
                        search: getGroups,
                        onRowSelect,
                        onAddButtonClick: onTableAddButtonClick,
                        addButtonText: "Add new group",
                    }} />
                </div>
            );
        }

        return null;
    }

    renderForm() {
        const { template, selectedRow, selectedRowId, showAddForm,
            onSaveButtonClick, onFormAddButtonClick, onDeleteButtonClick,
            isUpdating, hasUpdated, updateError, updateErrorMessage,
            isAdding, hasAdded, addError, addErrorMessage } = this.props;

        return (selectedRow || showAddForm) &&
            <DataForm { ... {
                dataRow: selectedRow,
                index: selectedRowId,
                onSaveButtonClick,
                onDeleteButtonClick,
                isLoading: (isUpdating || isAdding),
                error: (updateError || addError),
                errorMessage: updateErrorMessage || addErrorMessage,
                template,
                showAddForm,
                onAddButtonClick: onFormAddButtonClick,
                addButtonText: "Add group"
            }} />;
    }
}
