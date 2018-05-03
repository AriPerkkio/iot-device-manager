import React from 'react';
import PropTypes from 'prop-types';
import './datatable.scss';

import { Table, Button, Input, ButtonDropdown, DropdownToggle, DropdownMenu, DropdownItem  } from 'reactstrap';
import Column from './Column';

export default class DataTable extends React.Component {
    static propTypes = {
        rows: PropTypes.array,
        onRowSelect: PropTypes.func,
        selectedRowIndex: PropTypes.number
    }

    render() {
        return (
            <div className="data-table-container border">
                <Table responsive hover bordered className="data-table">
                    { this.renderHeader() }
                    { this.renderRows() }
                    { this.renderFooter() }
                </Table>
          </div>
        );
    }

    renderHeader() {
        const { rows } = this.props;
        const firstRow = rows.concat().shift();

        return firstRow && (
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

    renderFooter() {
        const { rows, onAddButtonClick, addButtonText } = this.props;
        const columnCount = (rows.concat().pop() || {}).length;

        return (
            <tfoot>
                <tr>
                    <td colSpan={columnCount}>
                        <div>
                            { this.renderFilterButton() }

                            <Input type="text" placeholder="Search"/>

                            { addButtonText &&
                            <Button
                                color="success"
                                className="border"
                                onClick={onAddButtonClick}>
                                {addButtonText}
                            </Button> }
                        </div>
                    </td>
                </tr>
            </tfoot>
        );
    }

    renderFilterButton() {
        const { queries, onFilterDropDownToggle, filterDropDownOpen } = this.props;

        const searchQuery = ((queries || [])
            .filter(({rel}) => rel.toLowerCase() === 'search') || [])
            .pop();

        const filters = ((searchQuery || {}).data || [])
            .map(({name}) => name);

        const disabled = filters.length === 0;

        return (
            <ButtonDropdown
                isOpen={filterDropDownOpen}
                toggle={onFilterDropDownToggle}
                disabled={disabled}>
                <DropdownToggle caret color="light" className="border">
                    Add filter
                </DropdownToggle>
                <DropdownMenu>
                    {filters.map(filter =>
                        <DropdownItem key={filter}>{filter}</DropdownItem>)}
                </DropdownMenu>
            </ButtonDropdown>
        );

    }
}