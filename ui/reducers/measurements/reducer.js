import { parseItems, parseLinks } from '../utils';

import {
    MEASUREMENTS_LOAD_START,
    MEASUREMENTS_LOAD_SUCCESS,
    MEASUREMENTS_LOAD_FAILED
} from './actions';

const initialState = {
    items: {},
    links: {},
    queries: [],
    template: {},
    isFetching: false,
    hasFetched: false,
    fetchingError: false,
    fetchingErrorMessage: ""
};

export default function reducer(state = initialState, action) {
    const { type, json } = action;

    switch(type) {
        case MEASUREMENTS_LOAD_START:
            return handleMeasurementsLoadStart(state);
        case MEASUREMENTS_LOAD_SUCCESS:
            return handleMeasurementsLoadSuccess(state, json);
        case MEASUREMENTS_LOAD_FAILED:
            return handleMeasurementsLoadFailed(state, action);

        default:
            return state;
    }
}

function handleMeasurementsLoadStart(state) {
    return {
        ...state,
        isFetching: true,
        fetchingError: false
    }
}

function handleMeasurementsLoadSuccess(state, { collection }) {
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

function handleMeasurementsLoadFailed(state, action) {
    return {
        ...state,
        isFetching: false,
        fetchingError: true,
        fetchingErrorMessage: action.error.message
    }
}
