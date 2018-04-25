import React from 'react';

import Configurations from './Configurations';

// State
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux';
import { generateGetConfigurations } from '../../reducers/configurations/actions';

export class ConfigurationsContainer extends React.Component {

    constructor(props) {
        super(props);

        const { dispatch } = props;

        this.getConfigurations = generateGetConfigurations(dispatch);
    }

    componentDidMount() {
        this.getConfigurations();
    }

    render() {
        const { items, links, queries, template, isFetching, hasFetched, error, errorMessage } = this.props;
        const { getConfigurations } = this;

        return (
            <Configurations { ...{
                items,
                links,
                queries,
                template,
                isFetching,
                hasFetched,
                error,
                errorMessage,
                getConfigurations
            }} />
        );
    }
}

function mapStateToProps(state) {
    return  state.configurations;
}

export default connect(mapStateToProps)(ConfigurationsContainer);