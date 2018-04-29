import * as stateUtils from '../utils';

import {
    DEVICES_LOAD_START,
    DEVICES_LOAD_SUCCESS,
    DEVICES_LOAD_FAILED,
    DEVICES_EDIT_START,
    DEVICES_EDIT_SUCCESS,
    DEVICES_EDIT_FAILED,
    RESET_DEVICES_EDIT_FAILED
} from './actions';

const { initialStateWithUpdate } = stateUtils;

export default function reducer(state = initialStateWithUpdate, action) {
    const { type, json } = action;

    switch(type) {
        case DEVICES_LOAD_START:
            return stateUtils.setLoadStart(state);
        case DEVICES_LOAD_SUCCESS:
            return stateUtils.setLoadSuccess(state, json);
        case DEVICES_LOAD_FAILED:
            return stateUtils.setLoadFailed(state, action);

        case DEVICES_EDIT_START:
            return stateUtils.setEditStart(state);
        case DEVICES_EDIT_SUCCESS:
            return stateUtils.setEditSuccess(state, json);
        case DEVICES_EDIT_FAILED:
            return stateUtils.setEditFailed(state, action);

        case RESET_DEVICES_EDIT_FAILED:
            return stateUtils.resetEditErrors(state);
        default:
            return state;
    }
}
