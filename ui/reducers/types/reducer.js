import * as stateUtils from '../utils';

import {
    TYPES_LOAD_START,
    TYPES_LOAD_SUCCESS,
    TYPES_LOAD_FAILED,
    TYPES_EDIT_START,
    TYPES_EDIT_SUCCESS,
    TYPES_EDIT_FAILED,
    RESET_TYPES_EDIT_FAILED,
    TYPES_ADD_START,
    TYPES_ADD_SUCCESS,
    TYPES_ADD_FAILED,
    RESET_TYPES_ADD_FAILED,
    TYPES_DELETE_START,
    TYPES_DELETE_SUCCESS,
    TYPES_DELETE_FAILED,
} from './actions';

const initialState = {
    ...stateUtils.initialStateWithUpdate,

    // Initial template with href used in cases when API has no records of types.
    // First GET query will overwrite this and keep us updated with lates API changes.
    template: {
        href: "/api/device-types",
        data: [
            {
                name: "name",
                prompt: "Type name",
                value: ""
            },
            {
                name: "deviceIconId",
                prompt: "Device icon identifier",
                value: ""
            }
        ]
    }
}

export default function reducer(state = initialState, action) {
    const { type, json } = action;

    switch(type) {
        case TYPES_LOAD_START:
            return stateUtils.setLoadStart(state);
        case TYPES_LOAD_SUCCESS:
            return stateUtils.setLoadSuccess(state, json);
        case TYPES_LOAD_FAILED:
            return stateUtils.setLoadFailed(state, action);

        case TYPES_EDIT_START:
            return stateUtils.setEditStart(state);
        case TYPES_EDIT_SUCCESS:
            return stateUtils.setEditSuccess(state, json);
        case TYPES_EDIT_FAILED:
            return stateUtils.setEditFailed(state, action);
        case RESET_TYPES_EDIT_FAILED:
            return stateUtils.resetEditErrors(state);

        case TYPES_ADD_START:
            return stateUtils.setAddStart(state);
        case TYPES_ADD_SUCCESS:
            return stateUtils.setAddSuccess(state, json);
        case TYPES_ADD_FAILED:
            return stateUtils.setAddFailed(state, action);
        case RESET_TYPES_ADD_FAILED:
            return stateUtils.resetAddErrors(state);

        case TYPES_DELETE_START:
            return stateUtils.setDeleteStart(state);
        case TYPES_DELETE_SUCCESS:
            return stateUtils.setDeleteSuccess(state, json);
        case TYPES_DELETE_FAILED:
            return stateUtils.setDeleteFailed(state, action);

        default:
            return state;
    }
}
