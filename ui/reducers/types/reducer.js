import * as stateUtils from '../utils';

import {
    TYPES_LOAD_START,
    TYPES_LOAD_SUCCESS,
    TYPES_LOAD_FAILED
} from './actions';

const { initialStateWithUpdate } = stateUtils;

export default function reducer(state = initialStateWithUpdate, action) {
    const { type, json } = action;

    switch(type) {
        case TYPES_LOAD_START:
            return stateUtils.setLoadStart(state);
        case TYPES_LOAD_SUCCESS:
            return stateUtils.setLoadSuccess(state, json);
        case TYPES_LOAD_FAILED:
            return stateUtils.setLoadFailed(state, action);

        default:
            return state;
    }
}
