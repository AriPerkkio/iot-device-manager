import { parseItems, parseLinks } from '../utils';

import {
    GROUPS_LOAD_START,
    GROUPS_LOAD_SUCCESS,
    GROUPS_LOAD_FAILED
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
        case GROUPS_LOAD_START:
            return handleGroupsLoadStart(state);
        case GROUPS_LOAD_SUCCESS:
            return handleGroupsLoadSuccess(state, json);
        case GROUPS_LOAD_FAILED:
            return handleGroupsLoadFailed(state, action);

        default:
            return state;
    }
}

function handleGroupsLoadStart(state) {
    return {
        ...state,
        isFetching: true,
        error: false
    }
}

function handleGroupsLoadSuccess(state, { collection }) {
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

function handleGroupsLoadFailed(state, action) {
    return {
        ...state,
        isFetching: false,
        error: true,
        errorMessage: action.error.message
    }
}