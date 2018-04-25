import { fetchMeasurements } from "../../api/measurements";

export const MEASUREMENTS_LOAD_START = 'MEASUREMENTS_LOAD_START';
export const MEASUREMENTS_LOAD_SUCCESS = 'MEASUREMENTS_LOAD_SUCCESS';
export const MEASUREMENTS_LOAD_FAILED = 'MEASUREMENTS_LOAD_FAILED';

function measurementsLoadStart(filters) {
    return {
        type: MEASUREMENTS_LOAD_START,
        filters
    }
}

function measurementsLoadSuccess(json) {
    return {
        type: MEASUREMENTS_LOAD_SUCCESS,
        json
    }
}

function measurementsLoadFailed(error) {
    return {
        type: MEASUREMENTS_LOAD_FAILED,
        error
    }
}

export function generateGetMeasurements(dispatch) {
    return filters => {
        dispatch(measurementsLoadStart(filters))
        return fetchMeasurements(filters)
            .then(json => dispatch(measurementsLoadSuccess(json)))
            .catch(error => dispatch(measurementsLoadFailed(error)))
    }
}
