import { parseItems, parseLinks } from '../utils';

import {
    GROUPS_LOAD_START,
    GROUPS_LOAD_SUCCESS,
    GROUPS_LOAD_FAILED,
    GROUPS_EDIT_START,
    GROUPS_EDIT_SUCCESS,
    GROUPS_EDIT_FAILED,
    RESET_GROUPS_EDIT_FAILED,
} from './actions';

const initialState = {
    items: {},
    links: {},
    queries: [],
    template: {},

    // GET methods
    isFetching: false,
    hasFetched: false,
    fetchingError: false,
    fetchingErrorMessage: "",

    // PUT methods
    isUpdating: false,
    hasUpdated: false,
    updateError: false,
    updateErrorMessage: "",
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

        case GROUPS_EDIT_START:
            return handleGroupEditStart(state);
        case GROUPS_EDIT_SUCCESS:
            return handleGroupEditSuccess(state, json);
        case GROUPS_EDIT_FAILED:
            return handleGroupEditFailed(state, action);

        case RESET_GROUPS_EDIT_FAILED:
            return resetHandleGroupEditFailed(state);
        default:
            return state;
    }
}

function handleGroupsLoadStart(state) {
    return {
        ...state,
        isFetching: true,
        fetchingError: false
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
        fetchingError: false,
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
        fetchingError: true,
        fetchingErrorMessage: action.error.message
    }
}

function handleGroupEditStart(state) {
    return {
        ...state,
        isUpdating: true,
        hasUpdated: false,
        updateError: false
    }
}

function handleGroupEditSuccess(state, { collection }) {
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
        isUpdating: false,
        hasUpdated: true,
        updateError: false,
        items,
        links
    }
}

function handleGroupEditFailed(state, action) {
    return {
        ...state,
        isUpdating: false,
        updateError: true,
        updateErrorMessage: action.error.message
    }
}

function resetHandleGroupEditFailed(state) {
    return {
        ...state,
        updateError: false,
        updateErrorMessage: ""
    }
}