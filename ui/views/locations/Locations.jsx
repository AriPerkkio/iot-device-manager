import React from 'react';

import LoadingIndicator from 'react-loading-indicator';
import DataTable from '../../components/DataTable/DataTableContainer';
import ErrorAlert from '../../components/ErrorAlert';

export default class Locations extends React.Component {

    render() {
        return (
            <div id="locations">
                <h1>Locations view</h1>
                { this.renderTable() }
            </div>
        );
    }

    renderTable() {
        const { items, links, queries, template, getLocations,
            isFetching, hasFetched, fetchingError, fetchingErrorMessage } = this.props;

        if(isFetching) {
            return <LoadingIndicator />;
        } else {
            return (
                <div>
                    {fetchingError && <ErrorAlert errorMessage={fetchingErrorMessage} /> }
                    <DataTable { ...{
                        items,
                        links,
                        queries,
                        template,
                        search: getLocations
                    }} />
                </div>
            );
        }

        return null;
    }
}
