import { parseItems, parseLinks } from '../utils';

import {
    DEVICES_LOAD_START,
    DEVICES_LOAD_SUCCESS,
    DEVICES_LOAD_FAILED
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
        case DEVICES_LOAD_START:
            return handleDevicesLoadStart(state);
        case DEVICES_LOAD_SUCCESS:
            return handleDevicesLoadSuccess(state, json);
        case DEVICES_LOAD_FAILED:
            return handleDevicesLoadFailed(state, action);

        default:
            return state;
    }
}

function handleDevicesLoadStart(state) {
    return {
        ...state,
        isFetching: true
    }
}

function handleDevicesLoadSuccess(state, { collection }) {
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
        queries,
        template,
        items,
        links
    }
}

function handleDevicesLoadFailed(state, action) {
    return {
        ...state,
        isFetching: false,
        error: true,
        errorMessage: action.error.message
    }
}
