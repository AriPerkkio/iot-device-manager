import { parseItems, parseLinks } from '../utils';

import {
    TYPES_LOAD_START,
    TYPES_LOAD_SUCCESS,
    TYPES_LOAD_FAILED
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
        case TYPES_LOAD_START:
            return handleTypesLoadStart(state);
        case TYPES_LOAD_SUCCESS:
            return handleTypesLoadSuccess(state, json);
        case TYPES_LOAD_FAILED:
            return handleTypesLoadFailed(state, action);

        default:
            return state;
    }
}

function handleTypesLoadStart(state) {
    return {
        ...state,
        isFetching: true,
        error: false
    }
}

function handleTypesLoadSuccess(state, { collection }) {
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

function handleTypesLoadFailed(state, action) {
    return {
        ...state,
        isFetching: false,
        error: true,
        errorMessage: action.error.message
    }
}
