import {
    DEVICES_LOAD_START,
    DEVICES_LOAD_SUCCESS,
    DEVICES_LOAD_FAILED
} from './actions';

const initialState = {
    devices: [],
    isFetching: false,
    hasFetched: false,
    error: false
};

export default function reducer(state = initialState, action) {
    const { type } = action;

    switch(type) {
        case DEVICES_LOAD_START:
            return handleDevicesLoadStart(state, action);
        case DEVICES_LOAD_SUCCESS:
            return handleDevicesLoadSuccess(state, action);
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

function handleDevicesLoadSuccess(state, action) {
    return {
        ...state,
        isFetching: false,
        hasFetched: true,
        // TODO devices: 
    }
}

function handleDevicesLoadFailed(state, action) {
    return {
        ...state,
        isFetching: false,
        error: true
    }
}