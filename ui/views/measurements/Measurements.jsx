import React from 'react';
import './measurements.scss';

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
                    search: getMeasurements
                }} />
            );
        }

        return null;
    }
}
