import { fetchTypes } from "../../api/types";

export const TYPES_LOAD_START = 'TYPES_LOAD_START';
export const TYPES_LOAD_SUCCESS = 'TYPES_LOAD_SUCCESS';
export const TYPES_LOAD_FAILED = 'TYPES_LOAD_FAILED';

function typesLoadStart(filters) {
    return {
        type: TYPES_LOAD_START,
        filters
    }
}

function typesLoadSuccess(json) {
    return {
        type: TYPES_LOAD_SUCCESS,
        json
    }
}

function typesLoadFailed(error) {
    return {
        type: TYPES_LOAD_FAILED,
        error
    }
}

export function generateGetTypes(dispatch) {
    return filters => {
        dispatch(typesLoadStart(filters))
        return fetchTypes(filters)
            .then(json => dispatch(typesLoadSuccess(json)))
            .catch(error => dispatch(typesLoadFailed(error)))
    }
}
