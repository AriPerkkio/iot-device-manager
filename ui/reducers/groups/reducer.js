import * as stateUtils from '../utils';

import {
    GROUPS_LOAD_START,
    GROUPS_LOAD_SUCCESS,
    GROUPS_LOAD_FAILED,
    GROUPS_EDIT_START,
    GROUPS_EDIT_SUCCESS,
    GROUPS_EDIT_FAILED,
    RESET_GROUPS_EDIT_FAILED,
    GROUPS_ADD_START,
    GROUPS_ADD_SUCCESS,
    GROUPS_ADD_FAILED,
    RESET_GROUPS_ADD_FAILED,
} from './actions';

const { initialStateWithUpdate } = stateUtils;

export default function reducer(state = initialStateWithUpdate, action) {
    const { type, json } = action;

    switch(type) {
        case GROUPS_LOAD_START:
            return stateUtils.setLoadStart(state);
        case GROUPS_LOAD_SUCCESS:
            return stateUtils.setLoadSuccess(state, json);
        case GROUPS_LOAD_FAILED:
            return stateUtils.setLoadFailed(state, action);

        case GROUPS_EDIT_START:
            return stateUtils.setEditStart(state);
        case GROUPS_EDIT_SUCCESS:
            return stateUtils.setEditSuccess(state, json);
        case GROUPS_EDIT_FAILED:
            return stateUtils.setEditFailed(state, action);
        case RESET_GROUPS_EDIT_FAILED:
            return stateUtils.resetEditErrors(state);

        case GROUPS_ADD_START:
            return stateUtils.setAddStart(state);
        case GROUPS_ADD_SUCCESS:
            return stateUtils.setAddSuccess(state, json);
        case GROUPS_ADD_FAILED:
            return stateUtils.setAddFailed(state, action);
        case RESET_GROUPS_ADD_FAILED:
            return stateUtils.resetAddErrors(state);
        default:
            return state;
    }
}
