import React from 'react';
import { Label, Input, Form, FormGroup } from 'reactstrap';

export default class DataForm extends React.Component {
    constructor(props) {
        super(props);

        this.state = { ...props };
    }

    render() {
        const { dataRow } = this.state;

        return dataRow && (
            <Form>
                {dataRow.map(this.renderInputWithLabel.bind(this))}
            </Form>
        );
    }

    renderInputWithLabel(col, key) {
        const { column, value, link } = col;
        const id = column.split(" ").join("");
        const type = /id/.test(column) ? "number" : "text";

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