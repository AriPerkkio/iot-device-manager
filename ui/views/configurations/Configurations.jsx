import React from 'react';
import './configurations.scss';

import DataTable from '../../components/DataTable/DataTableContainer';
import ErrorAlert from '../../components/ErrorAlert';

export default class Configurations extends React.Component {

    render() {
        return (
            <div id="configurations">
                <h1>Configurations view</h1>
                { this.renderTable() }
            </div>
        );
    }

    renderTable() {
        const { items, links, queries, template, getConfigurations,
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
                    search: getConfigurations
                }} />
            );
        }

        return null;
    }
}
