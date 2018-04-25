import { parseItems, parseLinks } from '../utils';

import {
    LOCATIONS_LOAD_START,
    LOCATIONS_LOAD_SUCCESS,
    LOCATIONS_LOAD_FAILED
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
        case LOCATIONS_LOAD_START:
            return handleLocationsLoadStart(state);
        case LOCATIONS_LOAD_SUCCESS:
            return handleLocationsLoadSuccess(state, json);
        case LOCATIONS_LOAD_FAILED:
            return handleLocationsLoadFailed(state, action);

        default:
            return state;
    }
}

function handleLocationsLoadStart(state) {
    return {
        ...state,
        isFetching: true,
        error: false
    }
}

function handleLocationsLoadSuccess(state, { collection }) {
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

function handleLocationsLoadFailed(state, action) {
    return {
        ...state,
        isFetching: false,
        error: true,
        errorMessage: action.error.message
    }
}
