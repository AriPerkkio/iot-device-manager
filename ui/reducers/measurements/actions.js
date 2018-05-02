import { fetchMeasurements } from "../../api/measurements";

export const MEASUREMENTS_LOAD_START = 'MEASUREMENTS_LOAD_START';
export const MEASUREMENTS_LOAD_SUCCESS = 'MEASUREMENTS_LOAD_SUCCESS';
export const MEASUREMENTS_LOAD_FAILED = 'MEASUREMENTS_LOAD_FAILED';

export const MEASUREMENTS_DELETE_START = "MEASUREMENTS_DELETE_START";
export const MEASUREMENTS_DELETE_SUCCESS = "MEASUREMENTS_DELETE_SUCCESS";
export const MEASUREMENTS_DELETE_FAILED =  "MEASUREMENTS_DELETE_FAILED";

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

function measurementsDeleteStart(data) {
    return {
        type: MEASUREMENTS_DELETE_START,
        data
    }
}

function measurementsDeleteSuccess(json) {
    return {
        type: MEASUREMENTS_DELETE_SUCCESS,
        json
    }
}

function measurementsDeleteFailed(error) {
    return {
        type: MEASUREMENTS_DELETE_FAILED,
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

export function generateDeleteMeasurement(dispatch) {
    return data => {
        dispatch(measurementsDeleteStart(data));
        return deleteMeasurement(data)
            .then(id => dispatch(measurementsDeleteSuccess(id)))
            .catch(error => dispatch(measurementsDeleteFailed(error)))
    }
}
