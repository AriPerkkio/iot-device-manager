import { fetchConfigurations } from "../../api/configurations";

export const CONFIGURATIONS_LOAD_START = 'CONFIGURATIONS_LOAD_START';
export const CONFIGURATIONS_LOAD_SUCCESS = 'CONFIGURATIONS_LOAD_SUCCESS';
export const CONFIGURATIONS_LOAD_FAILED = 'CONFIGURATIONS_LOAD_FAILED';

function configurationsLoadStart(filters) {
    return {
        type: CONFIGURATIONS_LOAD_START,
        filters
    }
}

function configurationsLoadSuccess(json) {
    return {
        type: CONFIGURATIONS_LOAD_SUCCESS,
        json
    }
}

function configurationsLoadFailed(error) {
    return {
        type: CONFIGURATIONS_LOAD_FAILED,
        error
    }
}

export function generateGetConfigurations(dispatch) {
    return filters => {
        dispatch(configurationsLoadStart(filters))
        return fetchConfigurations(filters)
            .then(json => dispatch(configurationsLoadSuccess(json)))
            .catch(error => dispatch(configurationsLoadFailed(error)))
    }
}
