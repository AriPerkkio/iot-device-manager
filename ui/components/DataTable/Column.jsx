import React from 'react';
import PropTypes from 'prop-types';

export default class Column extends React.Component {
    static propTypes = {
        text: PropTypes.oneOfType([
            PropTypes.string,
            PropTypes.number
        ]),
        link: PropTypes.string
    }

    render() {
        const { text, link } = this.props;

        return (
            <td data-href={link}>
                {text}
            </td>
        )
    }
}