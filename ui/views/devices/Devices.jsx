import React from 'react';
import './devices.scss';

import DataTable from '../../components/DataTable/DataTableContainer';

export default class Devices extends React.Component {

    render() {
        return (
            <div id="devices">
                <h1>Devices view</h1>
                { this.renderTable() }
            </div>
        );
    }

    renderTable() {
        const { items, links, queries, template, getDevices,
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
                    search: getDevices
                }} />
            );
        }

        return null;
    }
}
