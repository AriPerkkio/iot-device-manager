import React from 'react';

import LoadingIndicator from 'react-loading-indicator';
import DataTable from '../../components/DataTable/DataTableContainer';
import ErrorAlert from '../../components/ErrorAlert';

export default class Measurements extends React.Component {

    render() {
        return (
            <div id="measurements">
                <h1>Measurements view</h1>
                { this.renderTable() }
            </div>
        );
    }

    renderTable() {
        const { items, links, queries, template, getMeasurements,
            isFetching, hasFetched, fetchingError, fetchingErrorMessage } = this.props;

        if(isFetching) {
            return (
                <ErrorAlert { ...{
                    header: fetchingErrorMessage.split("::").shift(),
                    message: fetchingErrorMessage.split("::").pop()
                }} />
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
                    search: getMeasurements
                }} />
            );
        }

        return null;
    }
}
