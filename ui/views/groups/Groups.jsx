import React from 'react';
import './groups.scss';

import DataTable from '../../components/DataTable/DataTableContainer';

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
            return <p>{errorMessage}</p>;
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
