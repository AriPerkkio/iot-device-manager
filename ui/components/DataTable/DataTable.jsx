import React from 'react';
import PropTypes from 'prop-types';

import { Table } from 'reactstrap';
import Column from './Column';

export default class DataTable extends React.Component {
    static propTypes = {
        rows: PropTypes.array.isRequired
    }

    render() {
        return (
            <Table className="table-striped">
                { this.renderHeader() }
                { this.renderRows() }
          </Table>
        );
    }

    renderHeader() {
        const { rows } = this.props;
        const firstRow = rows.concat().shift();

        return (
            <thead className="thead-light">
                <tr>
                    {firstRow.map(({column}, key) =>
                        <th key={key} scope="col">{column}</th>)}
                </tr>
            </thead>
        );
    }

    renderRows() {
        const { rows } = this.props;

        return (
            <tbody>
                {rows.map((row, key) =>
                    <tr key={key}>
                        {row.map(({value, link}, subKey) =>
                            <Column
                                key={subKey}
                                text={value}
                                link={link}
                            />
                        )}
                    </tr>
                )}
            </tbody>
        );
    }
}