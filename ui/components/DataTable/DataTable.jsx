import React from 'react';
import PropTypes from 'prop-types';
import './datatable.scss';

import { Table } from 'reactstrap';
import Column from './Column';

export default class DataTable extends React.Component {
    static propTypes = {
        rows: PropTypes.array.isRequired,
        onRowSelect: PropTypes.func,
        selectedRowIndex: PropTypes.number
    }

    render() {
        const { rows, onRowSelect, selectedRowIndex, ...restProps } = this.props;

        return (
            <div>
                <Table responsive hover
                    { ... restProps }
                    className="data-table">

                    { this.renderHeader() }
                    { this.renderRows() }
                </Table>
          </div>
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
        const { rows, onRowSelect, selectedRowIndex } = this.props;

        return (
            <tbody>
                {rows.map((row, rowIndex) =>
                    <tr key={rowIndex}
                        onClick={() => onRowSelect(row, rowIndex)}
                        className={rowIndex === selectedRowIndex ? "selected" : ""}>

                        {row.map(({value, link}, key) =>
                            <Column
                                key={key}
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