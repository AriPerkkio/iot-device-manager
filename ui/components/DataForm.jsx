import React from 'react';
import PropTypes from 'prop-types';
import { Label, Input, FormGroup, Button } from 'reactstrap';

export default class DataForm extends React.Component {
    static propTypes = {
        dataRow: PropTypes.array.isRequired,
        index: PropTypes.number.isRequired,
        saveButtonText: PropTypes.string,
        deleteButtonText: PropTypes.string,
        hiddenButtons: PropTypes.array,
        onSaveButtonClick: PropTypes.func,
        onDeleteButtonClick: PropTypes.func
    }

    static defaultProps = {
        saveButtonText: "Save changes",
        deleteButtonText: "Delete item",
        hiddenButtons: []
    }

    constructor(props) {
        super(props);
        const { dataRow, index } = props;
        this.state = { dataRow, index };
    }

    componentWillReceiveProps(newProps) {
        const { index, dataRow } = newProps;
        const { index: sIndex } = this.state;

        // Apply props when row changes
        if(index !== sIndex) {
            this.setState({
                index,
                dataRow
            });
        }
    }

    render() {
        const { dataRow } = this.state;

        return dataRow && (
            <div>
                {dataRow.map(this.renderInputWithLabel.bind(this))}
                {this.renderButtons()}
            </div>
        );
    }

    renderInputWithLabel(col, key) {
        const { column, value, link } = col;
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
                    onChange: ({target}) => this.onInputChange(target.value, column)
                }}/>
            </FormGroup>
        )
    }

    renderButtons() {
        const { saveButtonText, deleteButtonText, hiddenButtons } = this.props;

        return (
            <div className="data-form-button-container">
                {!hiddenButtons.includes("delete") &&
                     <Button color="danger"
                     onClick={this.onDeleteButtonClick.bind(this)}>
                        {deleteButtonText}
                    </Button> }

                {!hiddenButtons.includes("save") &&
                    <Button color="success"
                        onClick={this.onSaveButtonClick.bind(this)}>
                        {saveButtonText}
                    </Button>}
            </div>
        )
    }

    onSaveButtonClick() {
        const { dataRow } = this.state;
        const { onSaveButtonClick } = this.props;

        onSaveButtonClick && onSaveButtonClick(dataRow);
    }

    onDeleteButtonClick() {
        const { dataRow } = this.state;
        const { onDeleteButtonClick } = this.props;

        onDeleteButtonClick && onDeleteButtonClick(dataRow);
    }

    onInputChange(value, column) {
        this.setState(({ dataRow }) => {
            const newDataRow = [ ...dataRow ].map(col => {
                if(col.column === column) {
                    return {
                        ...col,
                        value
                    };
                }
                return col;
            });

            return {
                dataRow: newDataRow
            }
        });
    }
}