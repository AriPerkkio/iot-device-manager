import React from 'react';
import PropTypes from 'prop-types';

import { Alert } from 'reactstrap';

export default class ErrorAlert extends React.Component {
    static propTypes = {
        header: PropTypes.string,
        message: PropTypes.string
    }

    render() {
        const { header, message } = this.props;

        return (
            <Alert color="danger">
                <h4 className="alert-heading">
                    {header}
                </h4>
                <p>
                    {message}
                </p>
            </Alert>
        );
    }
}
