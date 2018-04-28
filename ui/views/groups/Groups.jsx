import React from 'react';

import LoadingIndicator from 'react-loading-indicator';
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
            isFetching, hasFetched, fetchingError, fetchingErrorMessage } = this.props;

        if(isFetching) {
            return <LoadingIndicator />;
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
                    search: getGroups
                }} />
            );
        }

        return null;
    }
}
