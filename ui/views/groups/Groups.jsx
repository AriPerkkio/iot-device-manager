import React from 'react';
import './groups.scss';

import DataTable from '../../components/DataTable/DataTableContainer';
import ErrorAlert from '../../components/ErrorAlert';

export default class Groups extends React.Component {

    render() {
        return (
            <div id="groups">
                <h1>Groups view</h1>
                { this.renderTable() }
            </div>
        );
    }

    renderTable() {
        const { items, links, queries, template, getGroups,
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
                    search: getGroups
                }} />
            );
        }

        return null;
    }
}
