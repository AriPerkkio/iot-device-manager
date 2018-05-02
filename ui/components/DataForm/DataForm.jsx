import React from 'react';
import PropTypes from 'prop-types';
import { Label, Input, FormGroup, Button } from 'reactstrap';

import LoadingIndicator from 'react-loading-indicator';
import ErrorAlert from '../ErrorAlert';

export default class DataForm extends React.Component {
    static propTypes = {
        dataRow: PropTypes.arrayOf(PropTypes.shape({
            column: PropTypes.string.isRequired,
            name: PropTypes.string.isRequired,
            value: PropTypes.oneOfType([ PropTypes.string, PropTypes.number ]),
            href: PropTypes.string,
            link: PropTypes.string,
            readOnly: PropTypes.bool
        }))
    }

    render() {
        const { dataRow, showAddForm, error, isLoading } = this.props;

        return (dataRow || showAddForm) && (
            <div>
                { error && this.renderError() }
                { isLoading && this.renderLoader() }
                { dataRow && dataRow.map(this.renderInputWithLabel.bind(this)) }
                {this.renderButtons()}
            </div>
        );
    }

    renderInputWithLabel(col, key) {
        const { isLoading, onInputChange } = this.props;
        const { column, value, link, readOnly } = col;
        const id = column.split(" ").join("");
        const type = /\ id/i.test(column) ? "number" : "text";

        return (
            <FormGroup key={key}>
                <Label for={id}>
                    {column}
                </Label>
                <Input {...{
                    id,
                    type,
                    value: value !== null ? value : "",
                    placeholder: "not set",
                    onChange: ({target}) => onInputChange(target.value, column),
                    disabled: readOnly || isLoading
                }}/>
            </FormGroup>
        )
    }

    renderButtons() {
        const { saveButtonText, deleteButtonText, addButtonText,
            hiddenButtons, showAddForm, isLoading,
            onDeleteButtonClick, onSaveButtonClick, onAddButtonClick } = this.props;

        return (
            <div className="data-form-button-container">
                {!hiddenButtons.includes("delete") &&
                     <Button color="danger"
                         onClick={onDeleteButtonClick}
                         disabled={isLoading}>
                        {deleteButtonText}
                    </Button> }

                {!hiddenButtons.includes("save") &&
                    <Button color="success"
                        onClick={onSaveButtonClick}
                        disabled={isLoading}>
                        {saveButtonText}
                    </Button>}

                {showAddForm && !hiddenButtons.includes("add") &&
                    <Button color="success"
                        onClick={onAddButtonClick}
                        disabled={isLoading}>
                        {addButtonText}
                    </Button>}
            </div>
        )
    }

    renderLoader() {
        return (
            <div className="loading-indicator-wrapper">
                <LoadingIndicator
                    segmentWidth={30}
                    segmentLength={30} />
            </div>
        );
    }

    renderError() {
        const { errorMessage } = this.props;

        return errorMessage && (
            <ErrorAlert errorMessage={errorMessage} />
        );
    }
}