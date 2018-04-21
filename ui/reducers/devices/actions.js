import { fetchDevices } from "../../api/devices";

export const DEVICES_LOAD_START = 'DEVICES_LOAD_START';
export const DEVICES_LOAD_SUCCESS = 'DEVICES_LOAD_SUCCESS';
export const DEVICES_LOAD_FAILED = 'DEVICES_LOAD_FAILED';

function devicesLoadStart(filters) {
    return {
        type: DEVICES_LOAD_START,
        filters
    }
}

function devicesLoadSuccess(json) {
    return {
        type: DEVICES_LOAD_SUCCESS,
        json
    }
}

function devicesLoadFailed(error) {
    return {
        type: DEVICES_LOAD_FAILED,
        error
    }
}

export function getDevices(filters) {
    return dispatch => {
        dispatch(devicesLoadStart(filters))
        return fetchDevices(dispatch)(filters)
            .then(json => dispatch(devicesLoadSuccess(json)))
            .catch(error => dispatch(devicesLoadFailed(error)))
    }
}
