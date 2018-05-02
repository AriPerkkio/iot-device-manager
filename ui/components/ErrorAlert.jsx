import React from 'react';
import PropTypes from 'prop-types';

import { Alert } from 'reactstrap';
import { ERROR_SEPARATOR } from '../api/common';

export default class ErrorAlert extends React.Component {
    static propTypes = {
        errorMessage: PropTypes.string
    }

    render() {
        const { errorMessage } = this.props;

        return errorMessage && (
            <Alert color="danger" className="error-alert">
                <h4 className="alert-heading">
                    {errorMessage.split(ERROR_SEPARATOR).shift()}
                </h4>
                <p>
                    {errorMessage.split(ERROR_SEPARATOR).pop()}
                </p>
            </Alert>
        );
    }
}
