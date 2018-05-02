import * as stateUtils from '../utils';

import {
    MEASUREMENTS_LOAD_START,
    MEASUREMENTS_LOAD_SUCCESS,
    MEASUREMENTS_LOAD_FAILED,
    MEASUREMENTS_DELETE_START,
    MEASUREMENTS_DELETE_SUCCESS,
    MEASUREMENTS_DELETE_FAILED,
} from './actions';

const { initialState } = stateUtils;

export default function reducer(state = initialState, action) {
    const { type, json } = action;

    switch(type) {
        case MEASUREMENTS_LOAD_START:
            return stateUtils.setLoadStart(state);
        case MEASUREMENTS_LOAD_SUCCESS:
            return stateUtils.setLoadSuccess(state, json);
        case MEASUREMENTS_LOAD_FAILED:
            return stateUtils.setLoadFailed(state, action);

        case MEASUREMENTS_DELETE_START:
            return stateUtils.setDeleteStart(state);
        case MEASUREMENTS_DELETE_SUCCESS:
            return stateUtils.setDeleteSuccess(state, json);
        case MEASUREMENTS_DELETE_FAILED:
            return stateUtils.setDeleteFailed(state, action);

        default:
            return state;
    }
}
