import * as stateUtils from '../utils';

import {
    CONFIGURATIONS_LOAD_START,
    CONFIGURATIONS_LOAD_SUCCESS,
    CONFIGURATIONS_LOAD_FAILED,
    CONFIGURATIONS_EDIT_START,
    CONFIGURATIONS_EDIT_SUCCESS,
    CONFIGURATIONS_EDIT_FAILED,
    RESET_CONFIGURATIONS_EDIT_FAILED,
    CONFIGURATIONS_ADD_START,
    CONFIGURATIONS_ADD_SUCCESS,
    CONFIGURATIONS_ADD_FAILED,
    RESET_CONFIGURATIONS_ADD_FAILED,
    CONFIGURATIONS_DELETE_START,
    CONFIGURATIONS_DELETE_SUCCESS,
    CONFIGURATIONS_DELETE_FAILED,
} from './actions';

const initialState = {
    ...stateUtils.initialStateWithUpdate,

    // Initial template with href used in cases when API has no records of configurations.
    // First GET query will overwrite this and keep us updated with lates API changes.
    template: {
        href: "/api/configurations",
        data: [
            {
                name: "name",
                prompt: "Configuration name",
                value: ""
            },
            {
                name: "description",
                prompt: "Description",
                value: ""
            },
            {
                name: "content",
                prompt: "Configuration as JSON",
                value: ""
            }
        ]
    }
}

export default function reducer(state = initialState, action) {
    const { type, json } = action;

    switch(type) {
        case CONFIGURATIONS_LOAD_START:
            return stateUtils.setLoadStart(state);
        case CONFIGURATIONS_LOAD_SUCCESS:
            return stateUtils.setLoadSuccess(state, json);
        case CONFIGURATIONS_LOAD_FAILED:
            return stateUtils.setLoadFailed(state, action);

        case CONFIGURATIONS_EDIT_START:
            return stateUtils.setEditStart(state);
        case CONFIGURATIONS_EDIT_SUCCESS:
            return stateUtils.setEditSuccess(state, json);
        case CONFIGURATIONS_EDIT_FAILED:
            return stateUtils.setEditFailed(state, action);
        case RESET_CONFIGURATIONS_EDIT_FAILED:
            return stateUtils.resetEditErrors(state);

        case CONFIGURATIONS_ADD_START:
            return stateUtils.setAddStart(state);
        case CONFIGURATIONS_ADD_SUCCESS:
            return stateUtils.setAddSuccess(state, json);
        case CONFIGURATIONS_ADD_FAILED:
            return stateUtils.setAddFailed(state, action);
        case RESET_CONFIGURATIONS_ADD_FAILED:
            return stateUtils.resetAddErrors(state);

        case CONFIGURATIONS_DELETE_START:
            return stateUtils.setDeleteStart(state);
        case CONFIGURATIONS_DELETE_SUCCESS:
            return stateUtils.setDeleteSuccess(state, json);
        case CONFIGURATIONS_DELETE_FAILED:
            return stateUtils.setDeleteFailed(state, action);

        default:
            return state;
    }
}
