import React from 'react';
import PropTypes from 'prop-types';

import DataTable from './DataTable';

export default class DataTableContainer extends React.Component {
    static propTypes = {
        items: PropTypes.object.isRequired,
        links: PropTypes.object.isRequired,
        queries: PropTypes.array.isRequired,
        template: PropTypes.object
    }

    render() {
        const { queries, template } = this.props;
        const rows = this.generateRows();

        return (
            <DataTable { ...{
                rows
            }} />
        );
    }

    generateRows() {
        const { items } = this.props;

        return _.map(items, ({data}, id) => data
            .map(({ prompt, value }) => ({
                column: prompt,
                value,
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