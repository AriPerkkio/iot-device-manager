import React from 'react';
import PropTypes from 'prop-types';

import DataTable from './DataTable';

export default class DataTableContainer extends React.Component {
    static propTypes = {
        items: PropTypes.object.isRequired,
        links: PropTypes.object.isRequired,
        queries: PropTypes.array.isRequired,
        template: PropTypes.object,
        search: PropTypes.func,
        onAddButtonClick: PropTypes.func,
        addButtonText: PropTypes.string,
    }

    state = {
        selectedRowIndex: null,
        filterDropDownOpen: false,
    }

    render() {
        const { items, links, queries, template, search, addButtonText } = this.props;
        const { selectedRowIndex, filterDropDownOpen } = this.state;
        const rows = this.generateRows();

        return (
            <DataTable { ...{
                rows,
                onRowSelect: this.onRowSelect.bind(this),
                onFilterDropDownToggle: this.onFilterDropDownToggle.bind(this),
                selectedRowIndex,
                filterDropDownOpen,
                queries,
                onAddButtonClick: this.onAddButtonClick.bind(this),
                addButtonText,
            }} />
        );
    }

    // Set selected row index into state and pass row to prop function
    onRowSelect(row, index) {
        const { onRowSelect } = this.props;

        this.setState(({selectedRowIndex}) => {
            // Selecting same row twice disables selection
            const isDisable = selectedRowIndex === index;

            onRowSelect && onRowSelect(
                isDisable ? null : this.rowDataToTemplate(row),
                isDisable ? null : index);

            return {
                selectedRowIndex: isDisable ? null : index
            };
        });
    }

    onFilterDropDownToggle() {
        this.setState(({filterDropDownOpen}) => ({
            filterDropDownOpen: !filterDropDownOpen
        }));
    }

    onAddButtonClick() {
        this.setState({
            selectedRowIndex: null,
        });

        const { onAddButtonClick } = this.props;
        onAddButtonClick && onAddButtonClick();
    }

    // Set columns that are not found in template as read-only
    rowDataToTemplate(row) {
        const { template } = this.props;
        const templateRows = template.data.map(({name}) => name);

        return row.map(col => ({
            ...col,
            readOnly: !templateRows.includes(col.name)
        }));
    }

    generateRows() {
        const { items } = this.props;

        return _.map(items, ({data, href}, id) => data
            .map(({ prompt, value, name }) => ({
                column: prompt,
                value,
                name,
                href,
                link: this.getLinkForColumn(id, prompt)
            }))
        );
    }

    getLinkForColumn(id, prompt) {
        const { links } = this.props;

        // Match link's rel into items prompt and items id. Safely return null when no match found
        return (_.find(links, link => {
            const { rel, href } = link;
            const relSplit = rel.split(" ");

            const promptRegex = new RegExp(relSplit.pop());
            const linkId = relSplit.pop();

            return id == linkId && promptRegex.test(prompt.toLowerCase());
        }) || {}).href;
    }
}