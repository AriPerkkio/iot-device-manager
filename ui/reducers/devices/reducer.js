import { parseItems, parseLinks } from '../utils';

import {
    DEVICES_LOAD_START,
    DEVICES_LOAD_SUCCESS,
    DEVICES_LOAD_FAILED,
    DEVICES_EDIT_START,
    DEVICES_EDIT_SUCCESS,
    DEVICES_EDIT_FAILED
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
        case DEVICES_LOAD_START:
            return handleDevicesLoadStart(state);
        case DEVICES_LOAD_SUCCESS:
            return handleDevicesLoadSuccess(state, json);
        case DEVICES_LOAD_FAILED:
            return handleDevicesLoadFailed(state, action);

        case DEVICES_EDIT_START:
            return handleDevicesEditStart(state);
        case DEVICES_EDIT_SUCCESS:
            return handleDevicesEditSuccess(state, json);
        case DEVICES_EDIT_FAILED:
            return handleDevicesEditFailed(state, action);

        default:
            return state;
    }
}

function handleDevicesLoadStart(state) {
    return {
        ...state,
        isFetching: true,
        fetchingError: false
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
        fetchingError: false,
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
        fetchingError: true,
        fetchingErrorMessage: action.error.message
    }
}

function handleDevicesEditStart(state) {
    return {
        ...state,
        isUpdating: true,
        hasUpdated: false,
        updateError: false
    }
}

function handleDevicesEditSuccess(state, { collection }) {
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
        isUpdating: false,
        hasUpdated: true,
        updateError: false,
        queries,
        template,
        items,
        links
    }
}


function handleDevicesEditFailed(state, action) {
    return {
        ...state,
        isUpdating: false,
        updateError: true,
        updateErrorMessage: action.error.message
    }
}