import { parseItems, parseLinks } from '../utils';

import {
    CONFIGURATIONS_LOAD_START,
    CONFIGURATIONS_LOAD_SUCCESS,
    CONFIGURATIONS_LOAD_FAILED
} from './actions';

const initialState = {
    items: {},
    links: {},
    queries: [],
    template: {},
    isFetching: false,
    hasFetched: false,
    error: false,
    errorMessage: ""
};

export default function reducer(state = initialState, action) {
    const { type, json } = action;

    switch(type) {
        case CONFIGURATIONS_LOAD_START:
            return handleConfigurationsLoadStart(state);
        case CONFIGURATIONS_LOAD_SUCCESS:
            return handleConfigurationsLoadSuccess(state, json);
        case CONFIGURATIONS_LOAD_FAILED:
            return handleConfigurationsLoadFailed(state, action);

        default:
            return state;
    }
}

function handleConfigurationsLoadStart(state) {
    return {
        ...state,
        isFetching: true,
        error: false
    }
}

function handleConfigurationsLoadSuccess(state, { collection }) {
    const { queries, template } = collection;

    const items = {
        ...state.items,
        ...parseItems(collection)
    };

    const links = {
        ...state.links,
        ...parseLinks(collection)
    };

    return {
        ...state,
        isFetching: false,
        hasFetched: true,
        error: false,
        queries,
        template,
        items,
        links
    }
}

function handleConfigurationsLoadFailed(state, action) {
    return {
        ...state,
        isFetching: false,
        error: true,
        errorMessage: action.error.message
    }
}
