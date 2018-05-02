import * as stateUtils from '../utils';

import {
    LOCATIONS_LOAD_START,
    LOCATIONS_LOAD_SUCCESS,
    LOCATIONS_LOAD_FAILED,
    LOCATIONS_DELETE_START,
    LOCATIONS_DELETE_SUCCESS,
    LOCATIONS_DELETE_FAILED,
} from './actions';

const { initialState } = stateUtils;

export default function reducer(state = initialState, action) {
    const { type, json } = action;

    switch(type) {
        case LOCATIONS_LOAD_START:
            return stateUtils.setLoadStart(state);
        case LOCATIONS_LOAD_SUCCESS:
            return stateUtils.setLoadSuccess(state, json);
        case LOCATIONS_LOAD_FAILED:
            return stateUtils.setLoadFailed(state, action);

        case LOCATIONS_DELETE_START:
            return stateUtils.setDeleteStart(state);
        case LOCATIONS_DELETE_SUCCESS:
            return stateUtils.setDeleteSuccess(state, json);
        case LOCATIONS_DELETE_FAILED:
            return stateUtils.setDeleteFailed(state, action);

        default:
            return state;
    }
}
