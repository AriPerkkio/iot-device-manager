import React from 'react';
import PropTypes from 'prop-types';
import DataForm from './DataForm';

export default class DataFormContainer extends React.Component {
    static propTypes = {
        index: PropTypes.number,
        saveButtonText: PropTypes.string,
        deleteButtonText: PropTypes.string,
        addButtonText: PropTypes.string,
        hiddenButtons: PropTypes.array,
        onSaveButtonClick: PropTypes.func,
        onDeleteButtonClick: PropTypes.func,
        isLoading: PropTypes.bool,
        showAddForm: PropTypes.bool,
    }

    static defaultProps = {
        dataRow: [],
        saveButtonText: "Save changes",
        deleteButtonText: "Delete item",
        addButtonText: "Add item",
        hiddenButtons: [],
        isLoading: false,
        showAddForm: false,
    }

    constructor(props) {
        super(props);
        const { dataRow, index, showAddForm, template } = props;
        let initialState = {};

        if(showAddForm) {
            // Initial call from Add button. DataRow constructed from template
            initialState = {
                dataRow: this.templateToDataRow(template),
            };
        } else {
            initialState = { dataRow, index };
        }

        this.state = initialState;
    }

    componentWillReceiveProps(newProps) {
        const { index, dataRow, showAddForm, template } = newProps;
        const { index: sIndex } = this.state;

        if(showAddForm) {
            this.setState({
                dataRow: this.templateToDataRow(template),
                index
            });
        } else if(index !== sIndex) {
            // Apply props when row changes
            this.setState({
                index,
                dataRow
            });
        }
    }

    render() {
        const { dataRow, index } = this.state;
        const { showAddForm, isLoading, error, errorMessage,
            saveButtonText, deleteButtonText, addButtonText,
            hiddenButtons: initialHiddenButtons } = this.props;
        const { onInputChange, onSaveButtonClick, onDeleteButtonClick, onAddButtonClick } = this;
        let hiddenButtons = initialHiddenButtons;

        if((hiddenButtons == null || hiddenButtons.length === 0) && showAddForm) {
            hiddenButtons = ['delete', 'save'];
        }

        return <DataForm { ...{
            dataRow,
            index,
            showAddForm,
            isLoading,
            error,
            errorMessage,
            saveButtonText,
            deleteButtonText,
            addButtonText,
            hiddenButtons,
            onInputChange: onInputChange.bind(this),
            onSaveButtonClick: onSaveButtonClick.bind(this),
            onDeleteButtonClick: onDeleteButtonClick.bind(this),
            onAddButtonClick: onAddButtonClick.bind(this),
        }} />;
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

    onAddButtonClick() {
        const { dataRow } = this.state;
        const { onAddButtonClick, template } = this.props;
        const { href } = template;

        onAddButtonClick && onAddButtonClick(dataRow, href);
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

    templateToDataRow(template) {
        const { data } = template;

        return data.map(col => ({
            ...col,
            column: col.prompt
        }));
    }
}
