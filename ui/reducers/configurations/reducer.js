import * as stateUtils from '../utils';

import {
    CONFIGURATIONS_LOAD_START,
    CONFIGURATIONS_LOAD_SUCCESS,
    CONFIGURATIONS_LOAD_FAILED
} from './actions';

const { initialStateWithUpdate } = stateUtils;

export default function reducer(state = initialStateWithUpdate, action) {
    const { type, json } = action;

    switch(type) {
        case CONFIGURATIONS_LOAD_START:
            return stateUtils.setLoadStart(state);
        case CONFIGURATIONS_LOAD_SUCCESS:
            return stateUtils.setLoadSuccess(state, json);
        case CONFIGURATIONS_LOAD_FAILED:
            return stateUtils.setLoadFailed(state, action);

        default:
            return state;
    }
}
