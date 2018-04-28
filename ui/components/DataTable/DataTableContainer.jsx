import React from 'react';
import PropTypes from 'prop-types';

import DataTable from './DataTable';

export default class DataTableContainer extends React.Component {
    static propTypes = {
        items: PropTypes.object.isRequired,
        links: PropTypes.object.isRequired,
        queries: PropTypes.array.isRequired,
        template: PropTypes.object,
        search: PropTypes.func
    }

    state = {
        selectedRowIndex: null
    }

    render() {
        const { items, links, queries, template, search, ...restProps } = this.props;
        const { selectedRowIndex } = this.state;
        const rows = this.generateRows();

        return (
            <DataTable { ...{
                ... restProps,
                rows,
                onRowSelect: this.onRowSelect.bind(this),
                selectedRowIndex
            }} />
        );
    }

    // Set selected row index into state and pass row to prop function
    onRowSelect(row, index) {
        const { onRowSelect } = this.props;

        this.setState(({selectedRowIndex}) => {
            // Selecting same row twice disables selection
            const isDisable = selectedRowIndex === index;

            onRowSelect && onRowSelect(isDisable ? null : row, isDisable ? null : index);
            return {
                selectedRowIndex: isDisable ? null : index
            };
        });
    }

    generateRows() {
        const { items } = this.props;

        return _.map(items, ({data}, id) => data
            .map(({ prompt, value, name }) => ({
                column: prompt,
                value,
                name,
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